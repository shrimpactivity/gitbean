package repository;

import utils.FileHelper;

import java.io.File;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class Repository {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITBEAN_DIR = new File(CWD, ".gitbean");
    public static final File COMMITS_DIR = new File(GITBEAN_DIR, "commits");
    public static final File REFS_DIR = new File(GITBEAN_DIR, "refs");
    public static final File BLOBS_DIR = new File(GITBEAN_DIR, "blobs");
    public static final File STAGE_DIR = new File(GITBEAN_DIR, "stage");
    public static final File UNSTAGE_DIR = new File(GITBEAN_DIR, "unstage");

    /**
     * Creates the necessary .gitbean directory and sub-directories.
     */
    private static void createDirectories() {
        GITBEAN_DIR.mkdir();
        COMMITS_DIR.mkdir();
        REFS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGE_DIR.mkdir();
        UNSTAGE_DIR.mkdir();
    }

    /**
     * Initializes a repository in the current working directory.
     */
    public static void initialize() {
        createDirectories();
        Commit initCommit = new Commit();
        Pointer head = new Pointer("HEAD", initCommit.getId());
    }

    /**
     * Resets the repository to its initial state, deleting everything in .gitbean. Used for testing purposes.
     */
    public static void clear() {
        FileHelper.safeDeleteDir(GITBEAN_DIR);
        initialize();
    }

}
