package repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public class ParseCommandTest {
    @Test
    public void testNoArgs() {
        String[] args = new String[0];
        assertFalse(ParseCommand.parse(args));
    }
}
