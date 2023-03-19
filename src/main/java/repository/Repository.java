package repository;

import java.io.File;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class Repository {
    public static final File CWD = new File(System.getProperty("user.dir"));
    //public static final File GITLET_DIR = join(CWD, ".gitlet");
//    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
//    public static final File REFS_DIR = join(CWD, ".gitlet", "refs");
//    public static final File BLOBS_DIR = join(CWD, ".gitlet", "blobs");
//    public static final File ADD_STAGE_DIR = join(CWD, ".gitlet", "add-stage");
//    public static final File RM_STAGE_DIR = join(CWD, ".gitlet", "rm-stage");

    public static void initialize() {
        System.out.println(CWD);
    }
}
