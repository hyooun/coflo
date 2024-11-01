package com.reviewping.coflo.domain.review.entity;

import lombok.Getter;

@Getter
public enum LanguageType {
    TYPESCRIPT("typescript"),
    JAVASCRIPT("javascript"),
    PYTHON("python"),
    JAVA("java"),
    C("c"),
    CPP("cpp"),
    CSHARP("csharp"),
    GO("go"),
    RUBY("ruby"),
    RUST("rust"),
    SWIFT("swift"),
    PHP("php"),
    SHELL("shell"),
    SQL("sql"),
    HTML("html"),
    CSS("css"),
    TEXT("text");

    private final String type;

    LanguageType(String type) {
        this.type = type;
    }
}
