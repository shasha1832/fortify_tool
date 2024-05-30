package test;

import org.junit.jupiter.api.*;

import com.main.LineEditor;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LineEditorTest {
    private static final String TEST_FILE_PATH = "testfile.txt";
    private LineEditor editor;

    @BeforeEach
    void setUp() throws IOException {
        // Create a test file with initial content
        List<String> lines = Arrays.asList("first line", "second line", "last line");
        Files.write(Paths.get(TEST_FILE_PATH), lines);
        editor = new LineEditor(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the test file after each test
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    void testListLines() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        editor.listLines();

        String expectedOutput = "1: first line" + System.lineSeparator() +
                                "2: second line" + System.lineSeparator() +
                                "3: last line" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the original System.out
        System.setOut(System.out);
    }

    @Test
    void testDeleteLine() {
        editor.deleteLine(2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        editor.listLines();

        String expectedOutput = "1: first line" + System.lineSeparator() +
                                "2: last line" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the original System.out
        System.setOut(System.out);
    }

    @Test
    void testInsertLine() {
        editor.insertLine(2, "new line");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        editor.listLines();

        String expectedOutput = "1: first line" + System.lineSeparator() +
                                "2: new line" + System.lineSeparator() +
                                "3: second line" + System.lineSeparator() +
                                "4: last line" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the original System.out
        System.setOut(System.out);
    }

    @Test
    void testSaveFile() throws IOException {
        editor.insertLine(2, "new line");
        editor.saveFile();

        List<String> expectedLines = Arrays.asList("first line", "new line", "second line", "last line");
        List<String> actualLines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertEquals(expectedLines, actualLines);
    }

    @Test
    void testDeleteNonExistentLine() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        editor.deleteLine(5);
        String expectedOutput = "Invalid line number" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the original System.out
        System.setOut(System.out);
    }

    @Test
    void testInsertAtInvalidLineNumber() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        editor.insertLine(5, "invalid line");
        String expectedOutput = "Invalid line number" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());

        // Restore the original System.out
        System.setOut(System.out);
    }
}
