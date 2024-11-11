package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.treesitter.*;

public class JavaScriptChunkStrategy implements ChunkStrategy {

    private final TSParser parser;

    public JavaScriptChunkStrategy(TSParser parser) {
        this.parser = parser;
    }

    @Override
    public List<ChunkedCode> chunk(File file) {
        byte[] code = FileUtil.getCodeBytes(file);
        TSLanguage javascript = new TreeSitterJavascript();
        parser.setLanguage(javascript);
        TSTree tree = parser.parseString(null, new String(code));
        TSNode rootNode = tree.getRootNode();
        List<ChunkedCode> chunks = new ArrayList<>();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            TSNode node = rootNode.getChild(i);
            if ("function_declaration".equals(node.getType())
                    || "method_definition".equals(node.getType())) {
                String functionCode =
                        new String(
                                Arrays.copyOfRange(code, node.getStartByte(), node.getEndByte()));
                chunks.add(
                        new ChunkedCode(
                                functionCode, file.getName(), file.getPath(), "javascript"));
            }
        }
        return chunks;
    }
}
