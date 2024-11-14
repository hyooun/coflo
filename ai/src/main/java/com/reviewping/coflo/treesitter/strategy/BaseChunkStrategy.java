package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.List;

public class BaseChunkStrategy implements ChunkStrategy {

    @Override
    public List<ChunkedCode> chunk(File file) {
        byte[] code = FileUtil.getCodeBytes(file);
        String content = new String(code);
        String extension = FileUtil.getFileExtension(file);
        return List.of(new ChunkedCode(content, file.getName(), file.getPath(), extension));
    }
}
