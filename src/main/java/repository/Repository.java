package repository;

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

    public static void initialize() {
        GITBEAN_DIR.mkdir();

    }
}
