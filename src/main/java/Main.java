import repository.CommandParser;
/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Driver class for GitBean. Calls CommandParser on args.
 */
public class Main {
    public static void main(String[] args) {
        CommandParser.parse(args);
    }
}
