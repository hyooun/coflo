package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
        traverseAndCollectNodes(rootNode, chunks, code, file);
        return chunks;
    }

    private void traverseAndCollectNodes(
            TSNode node, List<ChunkedCode> chunks, byte[] code, File file) {
        if ("class_declaration".equals(node.getType())) {
            String classCode =
                    new String(Arrays.copyOfRange(code, node.getStartByte(), node.getEndByte()));
            chunks.add(new ChunkedCode(classCode, file.getName(), file.getPath(), "ts"));
            return;
        } else if ("function_declaration".equals(node.getType())) {
            String functionCode =
                    new String(Arrays.copyOfRange(code, node.getStartByte(), node.getEndByte()));
            chunks.add(new ChunkedCode(functionCode, file.getName(), file.getPath(), "ts"));
            return;
        } else if ("lexical_declaration".equals(node.getType())) {
            String lexicalCode =
                    new String(Arrays.copyOfRange(code, node.getStartByte(), node.getEndByte()));
            chunks.add(new ChunkedCode(lexicalCode, file.getName(), file.getPath(), "ts"));
            return;
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            TSNode childNode = node.getChild(i);
            traverseAndCollectNodes(childNode, chunks, code, file);
        }
    }
}
