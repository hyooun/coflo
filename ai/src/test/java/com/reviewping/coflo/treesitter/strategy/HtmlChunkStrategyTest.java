package com.reviewping.coflo.treesitter.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.reviewping.coflo.service.dto.ChunkedCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.treesitter.TSParser;

class HtmlChunkStrategyTest {

    private HtmlChunkStrategy strategy;
    private File htmlFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        strategy = new HtmlChunkStrategy(parser);
        htmlFile = Paths.get("src/test/resources/TestHTML.html").toFile();
    }

    private String normalize(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }

    @Test
    public void testChunkingHTMLFile() {
        List<ChunkedCode> chunks = strategy.chunk(htmlFile);

        System.out.println("\nchunks size: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("[Chunk " + i + " content] \n" + chunks.get(i).getContent() + "\n");
        }

        assertEquals(1, chunks.size(), "Expected 4 chunks to be extracted.");

        assertEquals(htmlFile.getName(), chunks.get(0).getFileName());
        assertEquals("html", chunks.get(0).getLanguage());
    }
}
