package com.reviewping.coflo.treesitter.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.treesitter.TSParser;

@Component
public class ChunkStrategyFactory {

    private final Map<String, ChunkStrategy> strategies = new HashMap<>();

    public ChunkStrategyFactory(TSParser parser) {
        strategies.put("base", new BaseChunkStrategy());
        strategies.put("java", new JavaChunkStrategy(parser));
        strategies.put("js", new JavaScriptChunkStrategy(parser));
        strategies.put("ts", new TypeScriptChunkStrategy(parser));
    }

    public ChunkStrategy getStrategy(String extension) {
        return strategies.getOrDefault(extension, strategies.get("base"));
    }
}
