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
        String extension = FileUtil.getFileExtension(file);
        ChunkStrategy strategy = chunkStrategyFactory.getStrategy(extension);
        return strategy.chunk(file);
    }
}
