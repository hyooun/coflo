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
import org.treesitter.TreeSitterJava;

public class JavaChunkStrategyTest {

    private JavaChunkStrategy strategy;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        parser.setLanguage(new TreeSitterJava());

        strategy = new JavaChunkStrategy(parser);

        tempFile = File.createTempFile("TestClass", ".java");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(
                    "public class TestClass {\n"
                            + "    public void methodOne() {\n"
                            + "        System.out.println(\"Method One\");\n"
                            + "    }\n\n"
                            + "    private int methodTwo() {\n"
                            + "        return 42;\n"
                            + "    }\n"
                            + "}");
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    public void testChunkingJavaFile() {
        List<ChunkedCode> chunks = strategy.chunk(tempFile);

        assertEquals(2, chunks.size(), "Expected 2 methods to be extracted.");

        assertEquals(
                "public void methodOne() {\n        System.out.println(\"Method One\");\n    }",
                chunks.get(0).getContent().trim());
        assertEquals(
                "private int methodTwo() {\n        return 42;\n    }",
                chunks.get(1).getContent().trim());

        assertEquals(tempFile.getName(), chunks.get(0).getFileName());
        assertEquals("java", chunks.get(0).getLanguage());
    }
}
