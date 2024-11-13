package com.reviewping.coflo.treesitter.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.reviewping.coflo.service.dto.ChunkedCode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.treesitter.TSParser;

public class JavaScriptChunkStrategyTest {

    private JavaScriptChunkStrategy strategy;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        strategy = new JavaScriptChunkStrategy(parser);

        tempFile = File.createTempFile("TestScript", ".js");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(
                    "function functionOne() {\n"
                            + "    console.log('Function One');\n"
                            + "}\n\n"
                            + "const functionTwo = () => {\n"
                            + "    return 42;\n"
                            + "};");
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    public void testChunkingJavaScriptFile() {
        List<ChunkedCode> chunks = strategy.chunk(tempFile);

        System.out.println("Extracted chunks size: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("[Chunk " + i + " content] \n" + chunks.get(i).getContent() + "\n");
        }

        assertEquals(2, chunks.size(), "Expected 2 functions to be extracted.");

        assertEquals(
                "function functionOne() {\n    console.log('Function One');\n}",
                chunks.get(0).getContent().trim());
        assertEquals(
                "const functionTwo = () => {\n    return 42;\n};",
                chunks.get(1).getContent().trim());

        assertEquals(tempFile.getName(), chunks.get(0).getFileName());
        assertEquals("javascript", chunks.get(0).getLanguage());
    }
}
