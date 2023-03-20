package repository;

import utils.FileHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Abstraction of a commit reference pointer which can be written to a file in the /refs directory.
 * Filename will be this.name, and file contents will be this.value.
 */
public class Pointer {

    public final String name;
    public String value;

    /**
     * Create a new pointer.
     * @param name
     * @param value
     */
    public Pointer(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Saves the Pointer into the specified directory.
     */
    public void save() {
        File file = new File(Repository.REFS_DIR, name);
        FileHelper.writeString(file, value);
    }

    /**
     * Loads pointer with the given name.
     * @param name
     * @return A new Pointer object.
     */
    public static Pointer load(String name) {
        File file = new File(Repository.REFS_DIR, name);
        String value = FileHelper.readString(file);
        return new Pointer(name, value);
    }
}
