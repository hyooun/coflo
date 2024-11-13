package com.reviewping.coflo.treesitter;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.strategy.*;
import java.io.File;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TreeSitterUtil {

    private final ChunkStrategyFactory chunkStrategyFactory;

    public List<ChunkedCode> chunk(File file) {
        String extension = getFileExtension(file);
        ChunkStrategy strategy = chunkStrategyFactory.getStrategy(extension);
        return strategy.chunk(file);
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf(".");
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
    }
}
