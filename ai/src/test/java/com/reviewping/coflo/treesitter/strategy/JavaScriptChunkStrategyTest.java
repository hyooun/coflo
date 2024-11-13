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

public class JavaScriptChunkStrategyTest {

    private JavaScriptChunkStrategy strategy;
    private File jsFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        strategy = new JavaScriptChunkStrategy(parser);
        jsFile = Paths.get("src/test/resources/TestJavascript.js").toFile();
    }

    private String normalize(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }

    @Test
    public void testChunkingJavaScriptFile() {
        List<ChunkedCode> chunks = strategy.chunk(jsFile);

        System.out.println("Extracted chunks size: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("[Chunk " + i + " content] \n" + chunks.get(i).getContent() + "\n");
        }

        assertEquals(4, chunks.size(), "Expected 5 functions to be extracted.");

        assertEquals(
                normalize("function functionOne() {\n    console.log('Function One');\n}"),
                normalize(chunks.get(0).getContent()));
        assertEquals(
                normalize("const functionTwo = (param) => {\n    return param * 2;\n};"),
                normalize(chunks.get(1).getContent()));
        assertTrue(
                normalize(chunks.get(2).getContent()).contains("class TestClass"),
                "Chunk 2 should contain 'TestClass' definition.");
        assertEquals(
                normalize(
                        "const functionThree = function() {\n"
                                + "    console.log('Anonymous Function');\n"
                                + "};"),
                normalize(chunks.get(3).getContent()));

        assertEquals(jsFile.getName(), chunks.get(0).getFileName());
        assertEquals("javascript", chunks.get(0).getLanguage());
    }
}
