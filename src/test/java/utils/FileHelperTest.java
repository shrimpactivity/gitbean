package utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import repository.Commit;
import repository.Repository;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class FileHelperTest {
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
        FileHelper.writeObject(file, content);
        Assertions.assertTrue(file.isFile());
        file.delete();
    }

    @Test
    public void testWriteCommit() {
        Commit content = new Commit();
        File file = new File(TESTING_DIR, "testCommit");
        FileHelper.writeObject(file, content);
        Assertions.assertTrue(file.isFile());
        file.delete();
    }

    @Test
    public void testReadString() {
        String content = "Hello world!";
        File file = new File(TESTING_DIR, "testString");
        FileHelper.writeObject(file, content);

        String result = (String) FileHelper.readObject(file);
        Assertions.assertEquals("Hello world!", result);

        file.delete();
    }

    @Test
    public void testReadCommit() {
        Commit content = new Commit();
        File file = new File(TESTING_DIR, "testCommit");
        FileHelper.writeObject(file, content);

        Commit result = (Commit) FileHelper.readObject(file);
        Assertions.assertEquals("initial commit", result.getMessage());


        file.delete();
    }

    @Test
    public void testSafeDelete() {
        // Delete valid file
        File file = new File(CWD, "testingFile");
        Assertions.assertFalse(file.exists());
        try {
            file.createNewFile();
            Assertions.assertTrue(file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(FileHelper.safeDeleteDir(file));
        file.delete();

        // Can't delete directory
        Assertions.assertFalse(FileHelper.safeDelete(TESTING_DIR));

        // Can't delete file not in .gitbean working directory
        File file2 = new File(Repository.GITBEAN_DIR, "temp-dir");
        file2.mkdir();
        Assertions.assertTrue(file2.isDirectory());
        Assertions.assertTrue(FileHelper.safeDeleteDir(file2));
        Assertions.assertFalse(file2.exists());
    }

    @Test
    public void testSafeDeleteDir() {
        // Can't delete file
        File file = new File(CWD, "testingFile");
        Assertions.assertFalse(file.exists());
        try {
            file.createNewFile();
            Assertions.assertTrue(file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(FileHelper.safeDeleteDir(file));
        file.delete();

        // Can't delete directory not in .gitbean
        Assertions.assertFalse(FileHelper.safeDeleteDir(TESTING_DIR));

        // Delete valid directory
        File file2 = new File(TESTING_DIR, "testingFile");
        try {
            file2.createNewFile();
            Assertions.assertTrue(file2.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(FileHelper.safeDelete(file2));
        file2.delete();
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
