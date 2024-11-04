package com.reviewping.coflo.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChunkedCode {
    private String content;
    private String fileName;
    private String filePath;
    private String language;
    private float[] embedding;

    public ChunkedCode(String content, String fileName, String filePath, String language) {
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
        this.language = language;
    }

    public void addEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}
