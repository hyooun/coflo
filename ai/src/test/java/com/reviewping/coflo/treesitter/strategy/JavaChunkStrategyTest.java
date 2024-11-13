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

public class JavaChunkStrategyTest {

    private JavaChunkStrategy strategy;
    private File javaFile;

    @BeforeEach
    public void setUp() throws IOException {
        TSParser parser = new TSParser();
        strategy = new JavaChunkStrategy(parser);
        javaFile = Paths.get("src/test/resources/TestClass.java").toFile();
    }

    private String normalize(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }

    @Test
    public void testChunkingJavaFile() {
        List<ChunkedCode> chunks = strategy.chunk(javaFile);

        System.out.println("Extracted chunks size: " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("[Chunk " + i + " content] \n" + chunks.get(i).getContent() + "\n");
        }

        assertEquals(4, chunks.size(), "Expected 4 methods to be extracted.");

        assertEquals(
                normalize("public void methodOne() {\n    System.out.println(\"Method One\");\n}"),
                normalize(chunks.get(0).getContent()));
        assertEquals(
                normalize("private int methodTwo() {\n    int result = 42;\n    return result;\n}"),
                normalize(chunks.get(1).getContent()));
        assertEquals(
                normalize(
                        "public static String staticMethod(String input) {\n"
                                + "    return \"Hello \" + input;\n"
                                + "}"),
                normalize(chunks.get(2).getContent()));
        assertEquals(
                normalize(
                        "private void methodWithInnerClass() {\n"
                                + "    class InnerClass {\n"
                                + "        public void innerMethod() {\n"
                                + "            System.out.println(\"Inner Method\");\n"
                                + "        }\n"
                                + "    }\n"
                                + "    InnerClass inner = new InnerClass();\n"
                                + "    inner.innerMethod();\n"
                                + "}"),
                normalize(chunks.get(3).getContent()));

        assertEquals(javaFile.getName(), chunks.get(0).getFileName());
        assertEquals("java", chunks.get(0).getLanguage());
    }
}
