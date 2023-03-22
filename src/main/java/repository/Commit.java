package repository;

import utils.FileHelper;
import utils.HashHelper;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class Commit implements Serializable {

    private static final long serialVersionUID = 0;

    private final String message;
    private final Date timestamp;
    private final String parent;
    private final String mergeParent = "";
    private final Map<String, String> fileMap;
    private final String id;

    /** Creates an initial root commit. */
    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = "";
        fileMap = new HashMap<>();
        id = generateId();
    }

    /** Creates a new commit with the specified parent, message, and file blobs. */
    public Commit(String parentID, String message, Map<String, String> fileBlobIds) {
        this.message = message;
        timestamp = new Date();
        parent = parentID;
        fileMap = fileBlobIds;
        id = generateId();
    }

    /** Generates this Commit's ID by hashing its attribute values. */
    private String generateId() {
        String result = HashHelper.sha1(
                message,
                timestamp.toString(),
                parent,
                fileMap.toString()
        );
        return result;
    }

    public String getId() { return id; }

    public Map<String, String> getFileMap() { return fileMap; }

    public String getMessage() {
        return message;
    }

    public String getParent() { return parent; }

    /**
     * Returns true if this commit is tracking a file with the given name.
     * @param fileName
     * @return
     */
    public boolean isTracking(String fileName) {
        return fileMap.containsKey(fileName);
    }

    /**
     * Returns true if the commit is tracking the given file's name and contents.
     * @param file
     * @return
     */
    public boolean isTrackingExact(File file) {
        Blob fileBlob = new Blob(file);
        Blob commitBlob = getBlob(file.getName());
        if (commitBlob != null && commitBlob.hasSameContent(fileBlob)) {
            return true;
        }
        return false;
    }


    /**
     * Returns the file Blob if the given file is tracked by this Commit, or null if it isn't tracked.
     * @param fileName
     * @return
     */
    public Blob getBlob(String fileName) {
        String id = fileMap.get(fileName);
        if (id == null) {
            return null;
        }
        return Blob.load(id);
    }

    /**
     * Saves the Commit under the COMMITS_DIR with a file name of its ID.
     */
    public void save() {
        File file = new File(Repository.COMMITS_DIR, id);
        FileHelper.writeObject(file, this);
    }

    private static File getCommitFileStartingWith(String id) {
        List<String> commitIds = FileHelper.getPlainFilenames(Repository.COMMITS_DIR);
        for (String commitId : commitIds) {
            if (commitId.startsWith(id)) {
                return new File(Repository.COMMITS_DIR, commitId);
            }
        }
        return null;
    }

    /**
     * Deserializes the specified Commit and returns it. The id can be abbreviated down to six characters.
     * @param id The commit id
     * @return
     */
    public static Commit load(String id) {
        if (id.length() < 6) {
            throw new IllegalArgumentException("Commit ID must be six or more characters long.");
        }

        File file = new File(Repository.COMMITS_DIR, id);
        if (!file.isFile()) {
            file = getCommitFileStartingWith(id);
        }

        try {
            return (Commit) FileHelper.readObject(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        String result = String.format("===%ncommit %s%n", id);
        result += String.format("Date: %s%n%s%n", timestamp.toString(), message);
        if (mergeParent != "") {
            result += String.format("Merge: %s", mergeParent.substring(0, 7));
        }
        return result;
    }


}
