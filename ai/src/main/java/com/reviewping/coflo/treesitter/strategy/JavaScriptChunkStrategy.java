package com.reviewping.coflo.treesitter.strategy;

import com.reviewping.coflo.service.dto.ChunkedCode;
import com.reviewping.coflo.treesitter.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treesitter.*;

public class JavaScriptChunkStrategy implements ChunkStrategy {

    private static final Logger log = LoggerFactory.getLogger(JavaScriptChunkStrategy.class);
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
        traverseAndCollectNodes(rootNode, chunks, code, file);
        return chunks;
    }

    private void traverseAndCollectNodes(
            TSNode node, List<ChunkedCode> chunks, byte[] code, File file) {
        if ("function_declaration".equals(node.getType())) {
            String functionCode =
                    new String(Arrays.copyOfRange(code, node.getStartByte(), node.getEndByte()));
            chunks.add(new ChunkedCode(functionCode, file.getName(), file.getPath(), "javascript"));
        } else if ("variable_declarator".equals(node.getType())) {
            boolean hasArrowFunction = false;
            TSNode parent = node.getParent();
            for (int i = 0; i < node.getChildCount(); i++) {
                TSNode child = node.getChild(i);
                if ("arrow_function".equals(child.getType())) {
                    hasArrowFunction = true;
                    break;
                }
            }
            if (hasArrowFunction && parent != null) {
                String functionCode =
                        new String(
                                Arrays.copyOfRange(
                                        code, parent.getStartByte(), parent.getEndByte()));
                chunks.add(
                        new ChunkedCode(
                                functionCode, file.getName(), file.getPath(), "javascript"));
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            traverseAndCollectNodes(node.getChild(i), chunks, code, file);
        }
    }
}
