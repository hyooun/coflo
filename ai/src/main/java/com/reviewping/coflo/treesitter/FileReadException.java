package com.reviewping.coflo.treesitter;

public class FileReadException extends RuntimeException {
    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
