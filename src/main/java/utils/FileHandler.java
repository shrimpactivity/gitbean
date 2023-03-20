package utils;

import java.io.*;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class FileHandler {

    /**
     * Writes the serializable object to the specified file.
     * @param file
     * @param obj
     */
    static void writeObject(File file, Serializable obj) {
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
    static Object readObject(File file) {
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
}
