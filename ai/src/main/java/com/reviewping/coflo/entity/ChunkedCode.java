package com.reviewping.coflo.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChunkedCode {
    private String content;
    private String fileName;
    private String filePath;
    private float[] embedding;

    public ChunkedCode(String content, String fileName, String filePath) {
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void addEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}
