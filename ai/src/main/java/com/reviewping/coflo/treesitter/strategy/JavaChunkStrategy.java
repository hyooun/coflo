package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.treesitter.*;

public class JavaChunkStrategy implements ChunkStrategy {

    private final TSParser parser;

    public JavaChunkStrategy(TSParser parser) {
        this.parser = parser;
    }

    @Override
    public List<ChunkedCode> chunk(File file) {
        byte[] code = FileUtil.getCodeBytes(file);
        TSLanguage java = new TreeSitterJava();
        parser.setLanguage(java);
        TSTree tree = parser.parseString(null, new String(code));
        TSNode rootNode = tree.getRootNode();
        List<ChunkedCode> chunks = new ArrayList<>();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            TSNode classNode = rootNode.getChild(i);
            if ("class_declaration".equals(classNode.getType())) {

                for (int j = 0; j < classNode.getChildCount(); j++) {
                    TSNode classBodyNode = classNode.getChild(j);
                    for (int k = 0; k < classBodyNode.getChildCount(); k++) {
                        TSNode methodNode = classBodyNode.getChild(k);

                        if ("method_declaration".equals(methodNode.getType())) {
                            String methodCode =
                                    new String(
                                            Arrays.copyOfRange(
                                                    code,
                                                    methodNode.getStartByte(),
                                                    methodNode.getEndByte()));

                            chunks.add(
                                    new ChunkedCode(
                                            methodCode, file.getName(), file.getPath(), "java"));
                        }
                    }
                }
            }
        }
        return chunks;
    }
}
