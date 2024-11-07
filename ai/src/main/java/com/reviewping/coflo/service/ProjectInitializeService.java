package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.git.GitUtil;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.EmbeddingResponse;
import com.reviewping.coflo.repository.VectorRepository;
import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.service.dto.request.InitRequestMessage;
import com.reviewping.coflo.treesitter.TreeSitterUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class ProjectInitializeService {

    private static final int BATCH_SIZE = 10;

    private final GitUtil gitUtil;
    private final OpenaiClient openaiClient;
    private final TreeSitterUtil treeSitterUtil;
    private final VectorRepository vectorRepository;
    private final String gitCloneDirectory;
    private final JsonUtil jsonUtil;

    public ProjectInitializeService(
            GitUtil gitUtil,
            OpenaiClient openaiClient,
            TreeSitterUtil treeSitterUtil,
            VectorRepository vectorRepository,
            @Value("${git-clone-directory}") String gitCloneDirectory,
            JsonUtil jsonUtil) {
        this.gitUtil = gitUtil;
        this.openaiClient = openaiClient;
        this.treeSitterUtil = treeSitterUtil;
        this.vectorRepository = vectorRepository;
        this.gitCloneDirectory = gitCloneDirectory;
        this.jsonUtil = jsonUtil;
    }

    @ServiceActivator(inputChannel = "initializeChannel")
    public void initializeKnowledgeBase(String initRequestMessage) {
        InitRequestMessage initRequest =
                jsonUtil.fromJson(initRequestMessage, new TypeReference<>() {});
        Long projectId = initRequest.projectId();
        Long branchId = initRequest.branchId();
        String gitUrl = initRequest.gitUrl();
        String branch = initRequest.branch();
        String token = initRequest.token();
        // 1. git clone
        String localPath = gitCloneDirectory + projectId + "/" + branch;
        gitUtil.shallowCloneOrPull(gitUrl, branch, token, localPath);
        // 2. 전처리: 메소드 단위 청킹 + 메타데이터 추가
        // 3. 벡터DB에 저장
        preprocessAndSave(projectId, branchId, localPath);
    }

    private void preprocessAndSave(Long projectId, Long branchId, String localPath) {
        List<ChunkedCode> buffer = new ArrayList<>(BATCH_SIZE);

        try (Stream<Path> filePathStream = Files.walk(Path.of(localPath))) {
            filePathStream
                    .filter(Files::isRegularFile) // 파일만 선택
                    .filter(this::isCodeFile)
                    .flatMap(filePath -> preprocessCode(filePath.toFile()).stream())
                    .map(this::addEmbedding)
                    .forEach(
                            chunkedCode -> {
                                buffer.add(chunkedCode);
                                if (buffer.size() >= BATCH_SIZE) {
                                    vectorRepository.saveAllChunkedCodes(
                                            projectId, branchId, buffer);
                                    buffer.clear();
                                }
                            });

            // 남아있는 데이터가 있으면 마지막으로 저장
            if (!buffer.isEmpty()) {
                vectorRepository.saveAllChunkedCodes(projectId, branchId, buffer);
            }
        } catch (IOException e) {
            throw new PreProcessException("파일 전처리 중 예외가 발생했습니다.", e);
        }
    }

    private boolean isCodeFile(Path filePath) {
        // 특정 확장자 필터링 (예: .java, .py 등)
        String fileName = filePath.getFileName().toString();
        return fileName.endsWith(".java") || fileName.endsWith(".py");
    }

    private List<ChunkedCode> preprocessCode(File file) {
        // 1. 코드 파일에 대해서 메소드 단위 청킹 후 메타데이터 저장
        return treeSitterUtil.chunk(file);
    }

    private ChunkedCode addEmbedding(ChunkedCode code) {
        // 1. openai embedding 정보 추가
        // input: string or array... TODO: embed multiple inputs in a request
        // refer: https://platform.openai.com/docs/api-reference/embeddings/create
        EmbeddingResponse embedding = openaiClient.generateEmbedding(code.getContent());
        code.addEmbedding(embedding.data().getFirst().embedding());
        return code;
    }
}
