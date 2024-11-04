package com.reviewping.coflo.service;

import com.reviewping.coflo.entity.ChunkedCode;
import com.reviewping.coflo.git.GitUtil;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.EmbeddingResponse;
import com.reviewping.coflo.repository.VectorRepository;
import com.reviewping.coflo.service.dto.InitRequestMessage;
import com.reviewping.coflo.treesitter.TreeSitterUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class ProjectInitializeService {

    private final GitUtil gitUtil;
    private final OpenaiClient openaiClient;
    private final TreeSitterUtil treeSitterUtil;
    private final VectorRepository vectorRepository;
    private final String gitCloneDirectory;

    public ProjectInitializeService(
            GitUtil gitUtil,
            OpenaiClient openaiClient,
            TreeSitterUtil treeSitterUtil,
            VectorRepository vectorRepository,
            @Value("${git-clone-directory}") String gitCloneDirectory) {
        this.gitUtil = gitUtil;
        this.openaiClient = openaiClient;
        this.treeSitterUtil = treeSitterUtil;
        this.vectorRepository = vectorRepository;
        this.gitCloneDirectory = gitCloneDirectory;
    }

    @ServiceActivator(inputChannel = "initializeChannel")
    public void initializeKnowledgeBase(InitRequestMessage initRequest) {
        Long projectId = initRequest.projectId();
        String gitUrl = initRequest.gitUrl();
        String branch = initRequest.branch();
        String token = initRequest.token();
        // 1. git clone
        String localPath = gitCloneDirectory + projectId + "/" + branch;
        gitUtil.shallowCloneOrPull(gitUrl, branch, token, localPath);
        // 2. 전처리: 메소드 단위 청킹 + 메타데이터 추가
        List<ChunkedCode> chunkedCodes = preprocessData(localPath);
        // 3. 벡터DB에 저장: batch로 작업
        vectorRepository.saveAllChunkedCodes(projectId, chunkedCodes);
    }

    private List<ChunkedCode> preprocessData(String localPath) {
        try (Stream<Path> filePathStream = Files.walk(Path.of(localPath))) {
            return filePathStream
                    .filter(Files::isRegularFile) // 파일만 선택
                    .filter(this::isCodeFile)
                    .flatMap(filePath -> preprocessCode(filePath.toFile()).stream())
                    .map(this::addEmbedding)
                    .toList();
        } catch (IOException e) {
            return List.of();
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
