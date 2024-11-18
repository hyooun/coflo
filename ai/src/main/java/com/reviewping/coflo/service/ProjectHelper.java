package com.reviewping.coflo.service;

import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.EmbeddingResponse;
import com.reviewping.coflo.repository.ChunkedCodeRepository;
import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.TreeSitterUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectHelper {

    private static final int BATCH_SIZE = 10;
    private final ChunkedCodeRepository chunkedCodeRepository;
    private final TreeSitterUtil treeSitterUtil;
    private final OpenaiClient openaiClient;

    @Transactional
    public void preprocessAndSave(Long branchInfoId, Stream<Path> filePathStream) {
        List<ChunkedCode> buffer = new ArrayList<>(BATCH_SIZE);

        filePathStream
                .filter(Files::isRegularFile) // 파일만 선택
                .filter(this::isCodeFile)
                .flatMap(filePath -> preprocessCode(filePath.toFile()).stream())
                .map(this::addEmbedding)
                .forEach(
                        chunkedCode -> {
                            buffer.add(chunkedCode);
                            if (buffer.size() >= BATCH_SIZE) {
                                chunkedCodeRepository.saveAllChunkedCodes(branchInfoId, buffer);
                                buffer.clear();
                            }
                        });

        // 남아있는 데이터가 있으면 마지막으로 저장
        if (!buffer.isEmpty()) {
            chunkedCodeRepository.saveAllChunkedCodes(branchInfoId, buffer);
        }
    }

    private boolean isCodeFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName.endsWith(".java")
                || fileName.endsWith(".js")
                || fileName.endsWith(".tsx")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".css")
                || fileName.endsWith(".html")
                || fileName.endsWith(".md");
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
