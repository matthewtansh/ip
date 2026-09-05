public enum CommandType {
    HELP,
    LIST,
    TODO,
    DEADLINE,
    EVENT,
    MARK,
    UNMARK,
    DELETE,
    BYE,
    UNKNOWN;

    public static CommandType from(String input) {
        String commandWord = input.split(" ", 2)[0];
        switch (commandWord) {
            case "help":
                return HELP;
            case "list":
                return LIST;
            case "todo":
                return TODO;
            case "deadline":
                return DEADLINE;
            case "event":
                return EVENT;
            case "mark":
                return MARK;
            case "unmark":
                return UNMARK;
            case "delete":
                return DELETE;
            case "bye":
                return BYE;
            default:
                return UNKNOWN;
        }
    }
}
