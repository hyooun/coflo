package com.reviewping.coflo.domain.review.entity;

import lombok.Getter;

@Getter
public enum LanguageType {
    TYPESCRIPT("ts"),
    JAVASCRIPT("js"),
    PYTHON("py"),
    JAVA("java"),
    C("c"),
    CPP("cpp"),
    CSHARP("cs"),
    GO("go"),
    RUBY("rb"),
    RUST("rs"),
    SWIFT("swift"),
    PHP("php"),
    SHELL("sh"),
    SQL("sql"),
    HTML("html"),
    CSS("css"),
    PLAINTEXT("txt");

    private final String type;

    LanguageType(String type) {
        this.type = type;
    }
}
