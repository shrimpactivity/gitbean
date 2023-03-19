import repository.ParseCommand;
/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Driver class for GitBean. Reads command line args and repository parser on them.
 */
public class Main {
    public static void main(String[] args) {
        ParseCommand.parse(args);
    }
}
