import java.util.ArrayList;
import java.util.Scanner;

public class Ollie {
    private static final String UI_HORIZONTAL_LINE = "------------------------------------------------------------";
    private static final String UI_INDENTATION = "    ";

    public static void main(String[] args) {
        String banner = "  ___  _ _ _      \n"
                + " / _ \\| | (_) ___ \n"
                + "| | | | | | |/ _ \\\n"
                + "| |_| | | | |  __/\n"
                + " \\___/|_|_|_|\\___|\n";
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.print(banner);
        System.out.println("Hello! I'm Ollie.");
        System.out.println("What can I do for you?");
        System.out.println(UI_HORIZONTAL_LINE);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                boolean isExit = false;

                try {
                    isExit = handleCommand(command, tasks);
                } catch (OllieException e) {
                    System.out.println(UI_INDENTATION + "OOPS! " + e.getMessage());
                }

                System.out.println(UI_HORIZONTAL_LINE);

                if (isExit) {
                    break;
                }
            }
        }
    }

    private static boolean handleCommand(String command, ArrayList<Task> tasks) throws OllieException {
        if (command.equals("bye")) {
            System.out.println(UI_INDENTATION + "Bye. Hope to see you again soon!");
            return true;
        } else if (command.equals("help")) {
            System.out.println(UI_INDENTATION + "help");
            System.out.println(UI_INDENTATION + "list");
            System.out.println(UI_INDENTATION + "todo <description>");
            System.out.println(UI_INDENTATION + "deadline <description> /by <date>");
            System.out.println(UI_INDENTATION + "event <description> /from <date> /to <date>");
            System.out.println(UI_INDENTATION + "mark <task number>");
            System.out.println(UI_INDENTATION + "unmark <task number>");
            System.out.println(UI_INDENTATION + "bye");
        } else if (command.equals("list")) {
            System.out.println(UI_INDENTATION + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(UI_INDENTATION + (i + 1) + ". " + tasks.get(i));
            }
        } else if (command.equals("mark") || command.startsWith("mark ")) {
            int taskIndex = getTaskIndex(command, "mark", tasks.size());
            tasks.get(taskIndex).mark();
            System.out.println(UI_INDENTATION + "Nice! I've marked this task as done.");
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            int taskIndex = getTaskIndex(command, "unmark", tasks.size());
            tasks.get(taskIndex).unmark();
            System.out.println(UI_INDENTATION + "Nice! I've marked this task as undone.");
        } else {
            addTask(tasks, createTask(command));
        }

        return false;
    }

    private static int getTaskIndex(String command, String action, int taskCount) throws OllieException {
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

    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println(UI_INDENTATION + "Got it. I've added this task.");
    }

    private static Task createTask(String command) throws OllieException {
        if (command.isEmpty()) {
            throw new OllieException("Please enter a command. Type help to see the available commands.");
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            return createTodo(command);
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            return createDeadline(command);
        } else if (command.equals("event") || command.startsWith("event ")) {
            return createEvent(command);
        }

        throw new OllieException("I don't recognize that command. Type help to see what I understand.");
    }

    private static Todo createTodo(String command) throws OllieException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("A todo needs a description. Try: todo <description>.");
        }

        return new Todo(description);
    }

    private static Deadline createDeadline(String command) throws OllieException {
        String details = command.substring("deadline".length()).trim();
        int byIndex = details.indexOf("/by");

        if (byIndex < 0) {
            throw new OllieException("A deadline needs /by followed by a date.");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("A deadline needs a description before /by.");
        } else if (by.isEmpty()) {
            throw new OllieException("A deadline needs a date after /by.");
        }

        return new Deadline(description, by);
    }

    private static Event createEvent(String command) throws OllieException {
        String details = command.substring("event".length()).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + "/from".length());

        if (fromIndex < 0) {
            throw new OllieException("An event needs /from followed by a start time.");
        } else if (toIndex < 0) {
            throw new OllieException("An event needs /to followed by an end time.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = details.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new OllieException("An event needs a description before /from.");
        } else if (from.isEmpty()) {
            throw new OllieException("An event needs a start time after /from.");
        } else if (to.isEmpty()) {
            throw new OllieException("An event needs an end time after /to.");
        }

        return new Event(description, from, to);
    }
}
