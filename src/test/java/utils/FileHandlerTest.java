package utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repository.Commit;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class FileHandlerTest {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File TESTING_DIR = new File(CWD, "testingFileHandler");

    @BeforeAll
    public static void createTestingDir() {
        TESTING_DIR.mkdir();
    }

    @Test
    public void testWriteString() {
        String content = "Hello world!";
        File file = new File(TESTING_DIR, "testString");
        FileHandler.writeObject(file, content);
        Assertions.assertTrue(file.isFile());
        file.delete();
    }

    @Test
    public void testWriteCommit() {
        Commit content = new Commit();
        File file = new File(TESTING_DIR, "testCommit");
        FileHandler.writeObject(file, content);
        Assertions.assertTrue(file.isFile());
        file.delete();
    }

    @Test
    public void testReadString() {
        String content = "Hello world!";
        File file = new File(TESTING_DIR, "testString");
        FileHandler.writeObject(file, content);

        String result = (String) FileHandler.readObject(file);
        Assertions.assertEquals("Hello world!", result);

        file.delete();
    }

    @Test
    public void testReadCommit() {
        Commit content = new Commit();
        File file = new File(TESTING_DIR, "testCommit");
        FileHandler.writeObject(file, content);

        Commit result = (Commit) FileHandler.readObject(file);
        Assertions.assertEquals("initial commit", result.getMessage());


        file.delete();
    }

    @AfterAll
    public static void deleteTestingDir() {
        try {
            FileUtils.deleteDirectory(TESTING_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
