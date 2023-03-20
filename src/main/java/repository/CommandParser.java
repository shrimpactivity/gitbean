package repository;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 *
 * Reads and validates args as provided by Main.main and calls Repository methods.
 */
public class CommandParser {
    /**
     * Parses command line arguments and calls Repository methods.
     *
     * @param args The command line args.
     * @return True if the command was valid and able to execute.
     */
    public static boolean parse(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return false;
        }

        final String commandText = args[0];
        final int numArgs = args.length - 1;

        switch (commandText) {
            case "init":
                if (Command.INIT.validate(numArgs)) {
                    Repository.initialize();
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
                return false;
        }

        System.out.printf("Invalid number of arguments for command %s.%n", commandText);
        return false;
    }
}
