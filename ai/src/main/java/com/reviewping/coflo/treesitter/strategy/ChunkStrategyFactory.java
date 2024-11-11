package com.reviewping.coflo.treesitter.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.treesitter.TSParser;

@Component
public class ChunkStrategyFactory {

    private final Map<String, ChunkStrategy> strategies = new HashMap<>();

    public ChunkStrategyFactory(TSParser parser) {
        strategies.put("java", new JavaChunkStrategy(parser));
        strategies.put("js", new JavaScriptChunkStrategy(parser));
        strategies.put("html", new HtmlChunkStrategy(parser));
        strategies.put("ts", new TypeScriptChunkStrategy(parser));
    }

    public ChunkStrategy getStrategy(String extension) {
        if (!strategies.containsKey(extension)) {
            throw new UnsupportedOperationException("Unsupported file type: " + extension);
        }
        return strategies.get(extension);
    }
}
