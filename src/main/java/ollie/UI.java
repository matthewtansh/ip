package ollie;

import java.util.Scanner;

/**
 * Handles console input and output for Ollie.
 */
public class UI {
    private static final int HORIZONTAL_LINE_LENGTH = 60;
    private static final String HORIZONTAL_LINE = "-".repeat(HORIZONTAL_LINE_LENGTH);
    private static final String INDENTATION = "    ";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public UI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Ollie's welcome message.
     */
    public void showWelcome() {
        String banner = "  ___  _ _ _      \n"
                + " / _ \\| | (_) ___ \n"
                + "| | | | | | |/ _ \\\n"
                + "| |_| | | | |  __/\n"
                + " \\___/|_|_|_|\\___|\n";

        System.out.print(banner);
        System.out.println("Hello! I'm Ollie.");
        System.out.println("What can I do for you?");
    }

    /**
     * Returns whether another command is available from standard input.
     *
     * @return True if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command from standard input.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays Ollie's goodbye message.
     */
    public void showGoodbye() {
        System.out.println(INDENTATION + "Bye. Hope to see you again soon!");
    }

    /**
     * Displays the supported command formats.
     */
    public void showHelp() {
        System.out.println(INDENTATION + "help");
        System.out.println(INDENTATION + "list");
        System.out.println(INDENTATION + "todo <description>");
        System.out.println(INDENTATION + "deadline <description> /by <yyyy-MM-dd>");
        System.out.println(INDENTATION + "event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>");
        System.out.println(INDENTATION + "mark <task number>");
        System.out.println(INDENTATION + "unmark <task number>");
        System.out.println(INDENTATION + "delete <task number>");
        System.out.println(INDENTATION + "bye");
    }

    /**
     * Displays all tasks with one-based numbering.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(INDENTATION + "Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(INDENTATION + (i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays a successful mark message.
     */
    public void showTaskMarked() {
        System.out.println(INDENTATION + "Nice! I've marked this task as done.");
    }

    /**
     * Displays a successful unmark message.
     */
    public void showTaskUnmarked() {
        System.out.println(INDENTATION + "Nice! I've marked this task as undone.");
    }

    /**
     * Displays a successful deletion message.
     */
    public void showTaskDeleted() {
        System.out.println(INDENTATION + "Noted. I've removed this task:");
    }

    /**
     * Displays a successful task addition message.
     */
    public void showTaskAdded() {
        System.out.println(INDENTATION + "Got it. I've added this task.");
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message Explanation of the error.
     */
    public void showError(String message) {
        System.out.println(INDENTATION + "OOPS! " + message);
    }

    /**
     * Displays the horizontal output divider.
     */
    public void showDivider() {
        System.out.println(HORIZONTAL_LINE);
    }
}
