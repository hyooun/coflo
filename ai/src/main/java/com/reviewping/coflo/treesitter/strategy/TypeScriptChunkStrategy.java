package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.treesitter.*;

public class TypeScriptChunkStrategy implements ChunkStrategy {

    private final TSParser parser;

    public TypeScriptChunkStrategy(TSParser parser) {
        this.parser = parser;
    }

    @Override
    public List<ChunkedCode> chunk(File file) {
        byte[] code = FileUtil.getCodeBytes(file);
        TSLanguage typescript = new TreeSitterTypescript();
        parser.setLanguage(typescript);
        TSTree tree = parser.parseString(null, new String(code));
        TSNode rootNode = tree.getRootNode();
        List<ChunkedCode> chunks = new ArrayList<>();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            TSNode node = rootNode.getChild(i);
            if ("function_declaration".equals(node.getType())
                    || "interface_declaration".equals(node.getType())) {
                String blockCode =
                        new String(
                                code, node.getStartByte(), node.getEndByte() - node.getStartByte());
                chunks.add(
                        new ChunkedCode(blockCode, file.getName(), file.getPath(), "typescript"));
            }
        }
        return chunks;
    }
}
