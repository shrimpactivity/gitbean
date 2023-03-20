package utils;

import java.io.*;
import org.apache.commons.io.FileUtils;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class FileHelper {

    /**
     * Writes the string to the specified file. Creates the file if it doesn't exist.
     * @param file
     * @param content
     */
    public static void writeString(File file, String content) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the text content of the specified file and returns it as a string.
     * @param file
     * @return
     */
    public static String readString(File file) {
        try {
            BufferedReader stream = new BufferedReader(new FileReader(file));
            String result = stream.readLine();
            stream.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Writes the serializable object to the specified file.
     * @param file
     * @param obj
     */
    public static void writeObject(File file, Serializable obj) {
        try {
            if (file.isDirectory()) {
                throw new IllegalArgumentException("Cannot overwrite directory.");
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(fileOut));
            objectOut.writeObject(obj);
            objectOut.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file and returns the serialized Object it contains.
     * @param file
     * @return Object
     */
    public static Object readObject(File file) {
        try {
            if (!file.isFile()) {
                throw new IllegalArgumentException("File is not normal.");
            }

            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(fileIn));
            Object result = objectIn.readObject();

            fileIn.close();
            objectIn.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes a file only if it exists in the same folder as the .gitbean directory.
     * @param file
     * @return True if the file was deleted, false otherwise.
     */
    public static boolean safeDelete(File file) {
        File gitbean = new File(file.getParent(), ".gitbean");
        if (!gitbean.isDirectory() || file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    /**
     * Deletes the directory only if it is a child of .gitbean or is .gitbean.
     * @param dir
     * @return True if the directory was deleted, false otherwise.
     */
    public static boolean safeDeleteDir(File dir) {
        File parent = new File(dir.getParent());
        boolean ableToDelete = dir.isDirectory() && (dir.getName().equals(".gitbean") || parent.getName().equals(".gitbean"));
        if (!ableToDelete) {
            return false;
        }
        try {
            FileUtils.deleteDirectory(dir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
