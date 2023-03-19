package repository;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Reads and validates args as provided by Main.main and calls Repository methods.
 */
public class ParseCommand {

    /**
     * Parses command line arguments and calls Repository methods.
     * @param args The command line args.
     * @return True if the command was valid and able to execute.
     */
    public static boolean parse(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return false;
        }


        return true;
    }
}
