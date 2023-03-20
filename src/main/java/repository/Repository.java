package repository;

import utils.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        initCommit.save();
        Pointer head = new Pointer("HEAD", "master");
        head.save();
        Pointer master = new Pointer("master", initCommit.getId());
        master.save();
    }

    /**
     * Returns the Commit object referenced by the HEAD pointer.
     */
    public static Commit getHeadCommit() {
        Pointer headRef = Pointer.load("HEAD");
        Pointer branchRef = Pointer.load(headRef.value);
        return Commit.load(branchRef.value);
    }

    /**
     * Adds the specified file to the staging area if it's not already tracked by the HEAD commit.
     * @param fileName
     */
    public static void stageFile(String fileName) {
        File fileToStage = new File(CWD, fileName);
        if (!fileToStage.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Commit head = getHeadCommit();
        if (head.isTrackingExact(fileToStage)) {
            return;
        }

        File target = new File(STAGE_DIR, fileName);
        target.delete();
        try {
            Files.copy(fileToStage.toPath(), target.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates Blobs from the filenames in STAGE_DIR, saves them, and returns a mapping
     * of the file names to the blob ids.
     * @param stagedFileNames
     * @return
     */
    private static Map<String, String> createAndSaveBlobs(List<String> stagedFileNames) {
        Map<String, String> fileBlobs = new HashMap<>();
        for (String fileName : stagedFileNames) {
            File file = new File(STAGE_DIR, fileName);
            Blob blob = new Blob(file);
            fileBlobs.put(fileName, blob.getId());
            blob.save();
        }
        return fileBlobs;
    }

    /**
     * Removes file name keys from the mapping if that file is in UNSTAGE_DIR
     * @param fileBlobs
     */
    private static void deleteUnstagedFiles(Map<String, String> fileBlobs) {
        List<String> files = FileHelper.getPlainFilenames(UNSTAGE_DIR);
        if (files == null) {
            System.out.println("Error: unable to read unstaged files.");
            return;
        }
        for (String name : files) {
            fileBlobs.remove(name);
        }
    }

    /**
     * Creates a new commit tracking files in STAGE_DIR and serializes it, as well as blobs of the files.
     * @param message
     */
    public static void commitStagedFiles(String message) {
        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);
        if (stagedFiles == null) {
            System.out.println("Error: unable to read staged files.");
            return;
        }
        if (stagedFiles.size() == 0) {
            System.out.println("No changes staged to commit.");
            return;
        }

        Pointer head = Pointer.load("HEAD");
        Commit prevCommit = getHeadCommit();

        Map<String, String> combinedBlobs = new HashMap<>(prevCommit.getFileBlobs());
        Map<String, String> newBlobs = createAndSaveBlobs(stagedFiles);
        combinedBlobs.putAll(newBlobs);
        deleteUnstagedFiles(combinedBlobs);

        Pointer branch = Pointer.load(head.value);
        Commit newCommit = new Commit(branch.value, message, combinedBlobs);
        newCommit.save();

        branch.value = newCommit.getId();
        branch.save();

        FileHelper.safeDeleteDir(STAGE_DIR);
        STAGE_DIR.mkdir();
        FileHelper.safeDeleteDir(UNSTAGE_DIR);
        UNSTAGE_DIR.mkdir();
    }

    public static void stageFileForRemoval(String fileName) {
        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);
        if (stagedFiles == null) {
            System.out.println("Error: unable to read staged files.");
            return;
        }

        if (stagedFiles.contains(fileName)) {
            File fileToRemove = new File(STAGE_DIR, fileName);
            fileToRemove.delete();
        }

        Commit head = getHeadCommit();
        if (head.isTracking(fileName)) {
            File fileToRemove = new File(CWD, fileName);
            File target = new File(UNSTAGE_DIR, fileName);
            target.delete();
            try {
                Files.copy(fileToRemove.toPath(), target.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileToRemove.delete();
        }
    }

    /**
     * Resets the repository to its initial state. Used for testing, not available as command.
     */
    public static void clear() {
        FileHelper.safeDeleteDir(GITBEAN_DIR);
        initialize();
    }

}
