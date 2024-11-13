package com.reviewping.coflo.treesitter.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.reviewping.coflo.service.dto.ChunkedCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.treesitter.TSParser;

class TypeScriptChunkStrategyTest {

    private TypeScriptChunkStrategy strategy;
    private File tsFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        strategy = new TypeScriptChunkStrategy(parser);
        tsFile = Paths.get("src/test/resources/TestTypescript.ts").toFile();
    }

    private String normalize(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }

    @Test
    public void testChunkingTypeScriptFile() {
        List<ChunkedCode> chunks = strategy.chunk(tsFile);

        System.out.println("chunks size: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("[Chunk " + i + " content] \n" + chunks.get(i).getContent() + "\n");
        }

        assertEquals(5, chunks.size());

        assertEquals(
                normalize("function greet(name: string) {\n    console.log(`Hello, ${name}!`);\n}"),
                normalize(chunks.get(0).getContent()));
        assertEquals(
                normalize("const multiply = (a: number, b: number): number => a * b;"),
                normalize(chunks.get(1).getContent()));
        assertTrue(
                normalize(chunks.get(2).getContent()).contains("class TestClass"),
                "Chunk 2 should contain 'TestClass' definition.");
        assertEquals(
                normalize(
                        "const anonymousFunction = function() {\n"
                                + "    console.log('This is an anonymous function');\n"
                                + "};"),
                normalize(chunks.get(3).getContent()));
        assertEquals(
                normalize("const simpleArrow = () => console.log('Simple arrow function');"),
                normalize(chunks.get(4).getContent()));

        assertEquals(tsFile.getName(), chunks.get(0).getFileName());
        assertEquals("typescript", chunks.get(0).getLanguage());
    }
}
