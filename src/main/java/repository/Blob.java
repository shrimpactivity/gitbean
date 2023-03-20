package repository;

import utils.FileHelper;
import utils.HashHelper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Abstraction of a file version tracked by a commit.
 */
public class Blob {

    private final byte[] content;
    private final String id;

    public Blob(File file) {
        content = FileHelper.readBytes(file);
        id = HashHelper.sha1(content);
    }

    public String getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    /**
     * Returns true if the byte content of this Blob and the other Blob are equivalent, using
     * Arrays.equals()
     * @param other
     * @return
     */
    public boolean hasSameContent(Blob other) {
        return Arrays.equals(content, other.getContent());
    }

    /**
     * Saves the blob content to a file in Repository.BLOBS_DIR with name of this.id.
     */
    public void save() {
        File file = new File(Repository.BLOBS_DIR, id);
        FileHelper.writeBytes(file, content);
    }

    /**
     * Returns a Blob from the data contained in the file under the BLOBS_DIR.
     * @param id
     * @return
     */
    public static Blob load(String id) {
        File file = new File(Repository.BLOBS_DIR, id);
        return new Blob(file);
    }
}
