package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class HashHelper {

    public static String sha1(String... vals) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (String val : vals) {
                md.update(val.getBytes(StandardCharsets.UTF_8));
            }
            Formatter result = new Formatter();
            for (byte b : md.digest()) {
                result.format("%02x", b);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }
}
