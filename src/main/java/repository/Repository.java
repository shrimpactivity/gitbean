package repository;

import utils.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

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
     * Returns true if a gitbean repo has been initialized.
     * @return
     */
    private static boolean exists() {
        return GITBEAN_DIR.isDirectory();
    }

    /**
     * Initializes a repository in the current working directory.
     */
    public static void initialize() {
        if (exists()) {
            System.out.printf("GitBean repository already exists in %s%n", CWD);
            return;
        }

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
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

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
     * Removes file name keys from the provided Commit mapping if that file is in UNSTAGE_DIR
     * @param fileBlobs
     */
    private static void deleteUnstagedFiles(Map<String, String> fileBlobs) {
        List<String> files = FileHelper.getPlainFilenames(UNSTAGE_DIR);
        for (String name : files) {
            fileBlobs.remove(name);
        }
    }

    /**
     * Called by 'commit' command. Creates a new commit tracking files in STAGE_DIR and
     * serializes it, as well as blobs of the files.
     * @param message
     */
    public static void commitStagedFiles(String message) {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);
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

    /**
     * Called by the 'rm' command. Removes file from stage, and stages it to be removed from being
     * tracked by current commit.
     * @param fileName
     */
    public static void stageFileForRemoval(String fileName) {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);

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
     * Prints the commit history starting with the HEAD commit.
     */
    public static void printLog() {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        Commit current = getHeadCommit();
        System.out.println(current);
        while (!current.getParent().equals("")) {
            current = Commit.load(current.getParent());
            System.out.println(current);
        }
    }

    /**
     * Prints all Commits located in COMMITS_DIR.
     */
    public static void printGlobalLog() {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        List<String> commitIds = FileHelper.getPlainFilenames(COMMITS_DIR);
        if (commitIds == null) {
            System.out.println("Error: unable to read commits.");
            return;
        }

        for (String id : commitIds) {
            Commit current = Commit.load(id);
            System.out.println(current);
        }
    }

    /**
     * Prints the IDs of all Commits with the given message.
     * @param message
     */
    public static void findMessage(String message) {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        List<String> commitIds = FileHelper.getPlainFilenames(COMMITS_DIR);
        boolean messageFound = false;
        for (String id : commitIds) {
            Commit current = Commit.load(id);
            if (current.getMessage().equals(message)) {
                System.out.println(current.getId());
                messageFound = true;
            }
        }

        if (!messageFound) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * Prints all branch names and marks the current one.
     */
    private static void printBranchStatus() {
        System.out.println("=== Branches ===");
        String current = Pointer.load("HEAD").value;
        List<String> branches = FileHelper.getPlainFilenames(REFS_DIR);
        for (String branch : branches) {
            if (!branch.equals("HEAD")) {
                if (branch.equals(current)) {
                    System.out.printf("%s*%n", branch);
                }
                else {
                    System.out.println(branch);
                }
            }
        }
        System.out.println();
    }

    /**
     * Prints the file names of everything in STAGE_DIR.
     */
    private static void printStagedFiles(List<String> stagedFiles) {
        System.out.println("=== Staged Files ===");
        for (String name : stagedFiles) {
            System.out.println(name);
        }
        System.out.println();
    }

    /**
     * Prints the file names of everything in UNSTAGE_DIR.
     */
    private static void printUnstagedFiles(List<String> unstagedFiles) {
        System.out.println("=== Removed Files ===");
        for (String name : unstagedFiles) {
            System.out.println(name);
        }
        System.out.println();
    }

    /**
     * Prints status of files that have untracked changes.
     * @param stagedFiles
     */
    private static void printModifications(List<String> stagedFiles) {
        System.out.println("=== Modifications Not Staged For Commit ===");
        Map<String, String> fileStatuses = new HashMap<>();
        Commit head = getHeadCommit();
        Map<String, String> commitFileBlobs = head.getFileBlobs();

        for (String name : stagedFiles) {
            Blob stageBlob = new Blob(new File(STAGE_DIR, name));
            File cwdVersion = new File(CWD, name);
            if (!cwdVersion.isFile()) {
                fileStatuses.put(name, "deleted");
                continue;
            }

            Blob cwdBlob = new Blob(cwdVersion);
            if (!cwdBlob.hasSameContent(stageBlob)) {
                fileStatuses.put(name, "modified");
            }
        }

        for (String name : commitFileBlobs.keySet()) {
            File cwdVersion = new File(CWD, name);
            File removeVersion = new File(UNSTAGE_DIR, name);
            if (!cwdVersion.isFile()) {
                if (!removeVersion.isFile()) {
                    fileStatuses.put(name, "deleted");
                }
                continue;
            }

            if (!head.isTrackingExact(cwdVersion)) {
                fileStatuses.put(name, "modified");
            }
        }

        for (String file : fileStatuses.keySet()) {
            System.out.printf("%s (%s)%n", file, fileStatuses.get(file));
        }
        System.out.println();
    }

    /**
     * Prints untracked files.
     */
    private static void printUntrackedFiles(List<String> cwdFiles, List<String> stageFiles) {
        System.out.println("=== Untracked Files ===");
        for (String file : cwdFiles) {
            if (!getHeadCommit().isTracking(file) && !stageFiles.contains(file)) {
                System.out.println(file);
            }
        }
        System.out.println();
    }


    /**
     * Prints the status of this Repository.
     */
    public static void printStatus() {
        if (!exists()) {
            System.out.printf("GitBean repository has not been initialized in %s%n", CWD);
            return;
        }

        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);
        List<String> unstagedFiles = FileHelper.getPlainFilenames(UNSTAGE_DIR);
        List<String> cwdFiles = FileHelper.getPlainFilenames(CWD);

        printBranchStatus();
        printStagedFiles(stagedFiles);
        printUnstagedFiles(unstagedFiles);
        printModifications(stagedFiles);
        printUntrackedFiles(cwdFiles, stagedFiles);
    }

    /**
     * Helper method for checkoutBranch(). If modifications are found, prints file names and returns true.
     * @return
     */
    private static boolean checkForModificationsInCWD() {
        Commit head = getHeadCommit();
        List<String> cwdFiles = FileHelper.getPlainFilenames(CWD);
        List<String> modifiedFiles = new ArrayList<>();
        for (String name : cwdFiles) {
            File file = new File(CWD, name);
            if (head.isTrackingExact(file)) {
                modifiedFiles.add(name);
            }
        }
        if (modifiedFiles.size() > 0) {
            System.out.println("Following files have untracked changed. Stage and commit changes before switching branches.");
            for (String name : modifiedFiles) {
                System.out.println(name);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns true if any files are staged for addition/removal.
     * @return
     */
    private static boolean checkForStagedFiles() {
        List<String> stagedFiles = FileHelper.getPlainFilenames(STAGE_DIR);
        List<String> unstagedFiles = FileHelper.getPlainFilenames(UNSTAGE_DIR);
        if (stagedFiles.size() > 0 || unstagedFiles.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Deletes all plain files in CWD.
     */
    private static void deleteCWDFiles() {
        for (String name : FileHelper.getPlainFilenames(CWD)) {
            File file = new File(CWD, name);
            FileHelper.safeDelete(file);
        }
    }

    /**
     * Loads the files tracked by the given commit into CWD.
     * @param com
     */
    private static void loadCommitFiles(Commit com) throws IOException {
        Map<String, String> blobs = com.getFileBlobs();
        for (String name : blobs.keySet()) {
            File file = new File(CWD, name);
            file.createNewFile();
            Blob blob = Blob.load(blobs.get(name));
            FileHelper.writeBytes(file, blob.getContent());
        }
    }

    private static void checkoutBranch(String branch) {
        if (checkForModificationsInCWD()) {
            return;
        }

        if (checkForStagedFiles()) {
            System.out.println("Commit staged changes before switching branches.");
            return;
        }

        String current = Pointer.load("HEAD").value;
        if (current.equals(branch)) {
            System.out.println("No need to checkout current branch");
            return;
        }

        Pointer ref = Pointer.load(branch);
        if (ref == null) {
            System.out.println("No such branch exists.");
            return;
        }

        deleteCWDFiles();

        Commit newHead = Commit.load(ref.value);
        try {
            loadCommitFiles(newHead);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pointer newHeadPointer = new Pointer("HEAD", branch);
        newHeadPointer.save();
    }

    /**
     * Copies the version of the file as it exists in the HEAD commit into the CWD.
     * @param fileName
     */
    private static void checkoutHeadFile(String fileName) {
        // TODO
    }

    public static void checkout(String[] args) {
        if (args.length == 1) {
            checkoutBranch(args[0]);
        }

        if (args.length == 2) {
            checkoutHeadFile(args[1]);
        }
    }

    /**
     * Resets the repository to its initial state. Used for testing, not available as command.
     */
    protected static void clear() {
        FileHelper.safeDeleteDir(GITBEAN_DIR);
        initialize();
    }

}
