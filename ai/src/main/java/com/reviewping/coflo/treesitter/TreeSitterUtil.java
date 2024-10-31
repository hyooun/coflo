package com.reviewping.coflo.treesitter;

import com.reviewping.coflo.entity.ChunkedCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.treesitter.*;

@Component
@RequiredArgsConstructor
public class TreeSitterUtil {

    private final TSParser parser;

    public List<ChunkedCode> chunk(File file) {
        parser.reset();
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".java")) {
            return chunkJava(file);
        } else if (fileName.endsWith(".js")) {
            return chunkJavascript(file);
        } else {
            return null;
        }
    }

    private List<ChunkedCode> chunkJava(File file) {
        String code = getCode(file);
        TSLanguage java = new TreeSitterJava();
        parser.setLanguage(java);
        TSTree tree = parser.parseString(null, code);
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
                                    code.substring(
                                            methodNode.getStartByte(), methodNode.getEndByte());

                            chunks.add(new ChunkedCode(methodCode, file.getName(), file.getPath()));
                        }
                    }
                }
            }
        }
        return chunks;
    }

    private String getCode(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: 다른 언어들에 대한 구현
    private List<ChunkedCode> chunkJavascript(File file) {
        return null;
    }
}
