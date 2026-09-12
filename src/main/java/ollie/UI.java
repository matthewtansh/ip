package ollie;

import java.util.Scanner;

public class UI {
    private static final String HORIZONTAL_LINE = "------------------------------------------------------------";
    private static final String INDENTATION = "    ";

    private final Scanner scanner;

    public UI() {
        scanner = new Scanner(System.in);
    }

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

    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showGoodbye() {
        System.out.println(INDENTATION + "Bye. Hope to see you again soon!");
    }

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

    public void showTaskList(TaskList tasks) {
        System.out.println(INDENTATION + "Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(INDENTATION + (i + 1) + ". " + tasks.get(i));
        }
    }

    public void showTaskMarked() {
        System.out.println(INDENTATION + "Nice! I've marked this task as done.");
    }

    public void showTaskUnmarked() {
        System.out.println(INDENTATION + "Nice! I've marked this task as undone.");
    }

    public void showTaskDeleted() {
        System.out.println(INDENTATION + "Noted. I've removed this task:");
    }

    public void showTaskAdded() {
        System.out.println(INDENTATION + "Got it. I've added this task.");
    }

    public void showError(String message) {
        System.out.println(INDENTATION + "OOPS! " + message);
    }

    public void showDivider() {
        System.out.println(HORIZONTAL_LINE);
    }
}
