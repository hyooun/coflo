package com.reviewping.coflo.treesitter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public static byte[] getCodeBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FileReadException("파일을 불러오지 못했습니다.", e);
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf(".");
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
    }
}
