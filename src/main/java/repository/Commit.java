package repository;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class Commit implements Serializable {

    private final String message;
    private final Date timestamp;
    private final String parent;
    private final String mergeParent = "";
    private final Map<String, String> fileBlobs = new HashMap<>();
    private final String id;

    /** Creates an initial root commit. */
    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
        parent = "";
        id = generateID();
    }

    /** Creates a new commit with the specified parent and message. */
    public Commit(String parentID, String message) {
        this.message = message;
        timestamp = new Date();
        parent = parentID;
        id = generateID();
    }


    /** Generates this Commit's ID by hashing its attribute values. */
    private String generateID() {
        return String.format(message, timestamp.toString(), parent);
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void saveCommit() {
//        File f = join(Repository.COMMITS_DIR, this.ID);
//        writeObject(f, this);
    }

    public String toString() {
//        String result = "===" + "\n" + "commit " + ID + "\n";
//        if (parents.size() > 1) {
//            result += "Merge:";
//            for (String p : parents) {
//                result += " " + p.substring(0, 7);
//            }
//        }
//        result += "Date: " + timestamp + "\n" + message + "\n";
//        return result;
        return "commit";
    }


}
