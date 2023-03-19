package repository;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Reads and validates args as provided by Main.main and calls Repository methods.
 */
public class ParseCommand {

    /**
     * Returns true if the length of args is equal to expected. Otherwise, prints message and
     * returns false.
     * @param args The command line arguments.
     * @param expected The number of expected arguments.
     * @return
     */
    private static boolean validateCommandArgs(String[] args, int expected) {
        if (args.length == expected) {
            return true;
        }
        System.out.printf("Invalid number of arguments following command, expected %d%n", expected);
        return false;
    }

    /**
     * Returns true if the length of args is between the expected range (inclusive). Otherwise,
     * prints message and returns false.
     * @param args The command line arguments.
     * @param expectedMin The number of expected arguments.
     * @param expectedMax The number of expected arguments.
     * @return
     */
    private static boolean validateCommandArgs(String[] args, int expectedMin, int expectedMax) {
        if (args.length >= expectedMin && args.length <= expectedMax) {
            return true;
        }
        System.out.printf("Invalid number of arguments following command, expected between %d and %d%n", expectedMin, expectedMax);
        return false;
    }

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

        String command = args[0];

        switch (command) {
            case "init":
                if (validateCommandArgs(args, 1)) {
                    // Repository.initialize();
                    return true;
                }
                break;
//            case "add":
//                if (validateCommandArgs(args, 2)){
//                    Repository.stageFileForAddition(args[1]);
//                }
//                break;
//            case "commit":
//                if (validateCommandArgs(args, 2)){
//                    Repository.commitStagedFiles(args[1]);
//                }
//                break;
//            case "rm":
//                if (validateCommandArgs(args, 2)) {
//                    Repository.stageFileForRemoval(args[1]);
//                }
//                break;
//            case "log":
//                if (validateCommandArgs(args, 1)) {
//                    Repository.printHeadLog();
//                }
//                break;
//            case "global-log":
//                if (validateCommandArgs(args, 1)) {
//                    Repository.printGlobalLog();
//                }
//                break;
//            case "find":
//                if (validateCommandArgs(args, 2)) {
//                    Repository.printCommitsWithMessage(args[1]);
//                }
//                break;
//            case "status":
//                if (validateCommandArgs(args, 1)) {
//                    Repository.printStatus();
//                }
//                break;
//            case "checkout":
//                // TODO
//                break;
//            case "branch":
//                // TODO
//                break;
//            case "rm-branch":
//                // TODO
//                break;
//            case "reset":
//                // TODO
//                break;
//            case "merge":
//                // TODO
//                break;
            default:
                System.out.println("Command does not exist.");
        }


        return false;
    }
}
