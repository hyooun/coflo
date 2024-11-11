package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import java.io.File;
import java.util.List;

public interface ChunkStrategy {
    List<ChunkedCode> chunk(File file);
}
