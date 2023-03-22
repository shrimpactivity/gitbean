package repository;

import java.util.Arrays;

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
            case "add":
                if (Command.ADD.validate(numArgs)){
                    Repository.stageFile(args[1]);
                    return true;
                }
                break;
            case "commit":
                if (Command.COMMIT.validate(numArgs)){
                    Repository.commitStagedFiles(args[1]);
                    return true;
                }
                break;
            case "rm":
                if (Command.RM.validate(numArgs)) {
                    Repository.stageFileForRemoval(args[1]);
                    return true;
                }
                break;
            case "log":
                if (Command.LOG.validate(numArgs)) {
                    Repository.printLog();
                    return true;
                }
                break;
            case "global-log":
                if (Command.GLOBAL_LOG.validate(numArgs)) {
                    Repository.printGlobalLog();
                    return true;
                }
                break;
            case "find":
                if (Command.FIND.validate(numArgs)) {
                    Repository.findMessage(args[1]);
                    return true;
                }
                break;
            case "status":
                if (Command.STATUS.validate(numArgs)) {
                    Repository.printStatus();
                    return true;
                }
                break;
            case "checkout":
                if (Command.CHECKOUT.validate(numArgs)) {
                    Repository.checkout(Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
                break;
            case "branch":
                if (Command.BRANCH.validate(numArgs)) {
                    Repository.createBranch(args[1]);
                    return true;
                }
                break;
            case "rm-branch":
                if (Command.RM_BRANCH.validate(numArgs)) {
                    Repository.removeBranch(args[1]);
                    return true;
                }
                break;
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
