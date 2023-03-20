package repository;

/**
 * @author Carson Crow
 * @author https://github.com/shrimpactivity/
 */
public enum Command {
    INIT(0),
    ADD(1),
    COMMIT(1),
    RM(1),
    LOG(0),
    GLOBAL_LOG(0),
    FIND(1),
    STATUS(0),
    CHECKOUT(1, 3),
    BRANCH(1),
    RM_BRANCH(1),
    RESET(1),
    MERGE(1);

    private final int minArgs, maxArgs;

    Command(int expectedArgs) {
        this.minArgs = expectedArgs;
        this.maxArgs = expectedArgs;
    }

    Command(int minArgs, int maxArgs) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    /**
     * Returns true if the number of arguments falls in the expected range for the command.
     * @param numArgs The number of arguments following the command.
     * @return
     */
    public boolean validate(int numArgs) {
        return numArgs >= minArgs && numArgs <= maxArgs;
    }
}
