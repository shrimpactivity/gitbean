package repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import utils.FileHelper;

import java.io.File;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class RepositoryTest {

    @Test
    public void testRepositoryInitializes() {
        FileHelper.safeDeleteDir(Repository.CWD);

        Repository.initialize();
        assertTrue(Repository.GITBEAN_DIR.isDirectory());
        assertTrue(Repository.COMMITS_DIR.isDirectory());
        assertTrue(Repository.REFS_DIR.isDirectory());
        assertTrue(Repository.BLOBS_DIR.isDirectory());
        assertTrue(Repository.STAGE_DIR.isDirectory());
        assertTrue(Repository.UNSTAGE_DIR.isDirectory());

        Pointer head = Pointer.load("HEAD");
        Pointer master = Pointer.load("master");
        assertEquals(head.value, master.name);

        File initCommit = new File(Repository.COMMITS_DIR, master.value);
        assertTrue(initCommit.isFile());
    }

    @Test
    public void testStageNewFile() {
        Repository.clear();

        File testFile = new File(Repository.CWD, "testFile");
        FileHelper.writeString(testFile,"this is a test");
        assertTrue(testFile.isFile());

        Repository.stageFile("testFile");
        File stageFile = new File(Repository.STAGE_DIR, "testFile");
        assertTrue(stageFile.isFile());

        testFile.delete();
        stageFile.delete();
    }

    @Test
    public void testCommit() {
        Repository.clear();

        File testFile = new File(Repository.CWD, "testFile");
        FileHelper.writeString(testFile,"this is a test\nit goes it goes it goes huh");
        assertTrue(testFile.isFile());

        Commit initCommit = Repository.getHeadCommit();

        Repository.stageFile("testFile");
        String message = "the second commit";
        Repository.commitStagedFiles("the second commit");

        Commit secondCommit = Repository.getHeadCommit();

        // Test commit has correct parent
        assertEquals(initCommit.getId(), secondCommit.getParent());

        // Test commit has correct message
        assertEquals(message, secondCommit.getMessage());

        // Test commit has correct Blob
        Blob testBlob = new Blob(testFile);
        Blob commitBlob = secondCommit.getBlob("testFile");
        assertEquals(testBlob.getContentString(), commitBlob.getContentString());

        // Test file can't be staged when already tracked
        Repository.stageFile("testFile");
        assertFalse(new File(Repository.STAGE_DIR, "testFile").isFile());
    }
}
