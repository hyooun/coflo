package com.reviewping.coflo.domain.review.entity;

import java.util.HashMap;
import java.util.Map;
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
    private static final Map<String, LanguageType> LANGUAGE_TYPE_MAP = new HashMap<>();

    static {
        for (LanguageType lang : LanguageType.values()) {
            LANGUAGE_TYPE_MAP.put(lang.type, lang);
        }
    }

    LanguageType(String type) {
        this.type = type;
    }

    public static LanguageType fromType(String type) {
        LanguageType result = LANGUAGE_TYPE_MAP.get(type.toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException("지원하지 않는 언어입니다: " + type);
        }
        return result;
    }
}
