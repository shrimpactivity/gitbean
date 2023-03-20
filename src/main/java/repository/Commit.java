package repository;

import utils.FileHelper;
import utils.HashHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
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
    private final Map<String, String> fileBlobs;
    private final String id;

    /** Creates an initial root commit. */
    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = "";
        fileBlobs = new HashMap<>();
        id = generateId();
    }

    /** Creates a new commit with the specified parent, message, and file blobs. */
    public Commit(String parentID, String message, Map<String, String> blobs) {
        this.message = message;
        timestamp = new Date();
        parent = parentID;
        fileBlobs = blobs;
        id = generateId();
    }

    /** Generates this Commit's ID by hashing its attribute values. */
    private String generateId() {
        String result = HashHelper.sha1(
                message,
                timestamp.toString(),
                parent,
                fileBlobs.toString()
        );
        return result;
    }

    public String getId() { return id; }

    public Map<String, String> getFileBlobs() { return fileBlobs; }

    public String getMessage() {
        return message;
    }

    public String getParent() { return parent; }

    /**
     * Returns the file Blob if the given file is tracked by this Commit, or null if it isn't tracked.
     * @param fileName
     * @return
     */
    public Blob getBlob(String fileName) {
        String id = fileBlobs.get(fileName);
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

    /**
     * Deserializes the specified Commit and returns it.
     * @param id The commit id
     * @return
     */
    public static Commit load(String id) {
        File file = new File(Repository.COMMITS_DIR, id);
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
