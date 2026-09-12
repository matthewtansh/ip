import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {
    public CommandType parseCommand(String input) {
        String commandWord = input.split(" ", 2)[0];
        switch (commandWord) {
            case "help":
                return CommandType.HELP;
            case "list":
                return CommandType.LIST;
            case "todo":
                return CommandType.TODO;
            case "deadline":
                return CommandType.DEADLINE;
            case "event":
                return CommandType.EVENT;
            case "mark":
                return CommandType.MARK;
            case "unmark":
                return CommandType.UNMARK;
            case "delete":
                return CommandType.DELETE;
            case "bye":
                return CommandType.BYE;
            default:
                return CommandType.UNKNOWN;
        }
    }

    public int parseTaskIndex(String command, String action, int taskCount) throws OllieException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new OllieException("Tell me which task to " + action + ". Try: " + action + " <task number>.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new OllieException("The task number must be a whole number.");
        }

        if (taskCount == 0) {
            throw new OllieException("There are no tasks to " + action + ".");
        } else if (taskNumber < 1 || taskNumber > taskCount) {
            throw new OllieException("Choose a task number between 1 and " + taskCount + ".");
        }

        return taskNumber - 1;
    }

    public Task parseTask(String command) throws OllieException {
        CommandType commandType = parseCommand(command);
        if (commandType == CommandType.TODO) {
            return createTodo(command);
        } else if (commandType == CommandType.DEADLINE) {
            return createDeadline(command);
        } else if (commandType == CommandType.EVENT) {
            return createEvent(command);
        } else if (command.isEmpty()) {
            throw new OllieException("Please enter a command. Type help to see the available commands.");
        }

        throw new OllieException("I don't recognize that command. Type help to see what I understand.");
    }

    private Todo createTodo(String command) throws OllieException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("A todo needs a description. Try: todo <description>.");
        }

        return new Todo(description);
    }

    private Deadline createDeadline(String command) throws OllieException {
        String details = command.substring("deadline".length()).trim();
        int byIndex = details.indexOf("/by");

        if (byIndex < 0) {
            throw new OllieException("A deadline needs /by followed by a date.");
        }

        String description = details.substring(0, byIndex).trim();
        String byText = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("A deadline needs a description before /by.");
        } else if (byText.isEmpty()) {
            throw new OllieException("A deadline needs a date after /by.");
        }

        LocalDate by = parseDate(byText, "deadline date");
        return new Deadline(description, by);
    }

    private Event createEvent(String command) throws OllieException {
        String details = command.substring("event".length()).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + "/from".length());

        if (fromIndex < 0) {
            throw new OllieException("An event needs /from followed by a start date.");
        } else if (toIndex < 0) {
            throw new OllieException("An event needs /to followed by an end date.");
        }

        String description = details.substring(0, fromIndex).trim();
        String fromText = details.substring(fromIndex + "/from".length(), toIndex).trim();
        String toText = details.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("An event needs a description before /from.");
        } else if (fromText.isEmpty()) {
            throw new OllieException("An event needs a start date after /from.");
        } else if (toText.isEmpty()) {
            throw new OllieException("An event needs an end date after /to.");
        }

        LocalDate from = parseDate(fromText, "event start date");
        LocalDate to = parseDate(toText, "event end date");
        return new Event(description, from, to);
    }

    private LocalDate parseDate(String dateText, String dateDescription) throws OllieException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new OllieException("The " + dateDescription + " must use yyyy-MM-dd format, e.g., 2019-12-02.");
        }
    }
}
