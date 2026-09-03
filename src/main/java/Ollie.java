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
                String command = scanner.nextLine();

                if (command.equals("bye")) {
                    System.out.println(UI_INDENTATION + "Bye. Hope to see you again soon!");
                    System.out.println(UI_HORIZONTAL_LINE);
                    break;
                }

                if (command.equals("list")) {
                    System.out.println(UI_INDENTATION + "Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(UI_INDENTATION + (i + 1) + ". " + tasks.get(i));
                    }
                } else if (command.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(command.substring("mark ".length())) - 1;
                    Task task = tasks.get(taskIndex);
                    task.mark();
                    System.out.println(UI_INDENTATION + "Nice! I've marked this task as done.");
                    System.out.println(UI_INDENTATION + "  " + task);
                } else if (command.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(command.substring("unmark ".length())) - 1;
                    Task task = tasks.get(taskIndex);
                    task.unmark();
                    System.out.println(UI_INDENTATION + "Nice! I've marked this task as undone.");
                    System.out.println(UI_INDENTATION + "  " + task);
                } else {
                    tasks.add(new Task(command));
                    System.out.println(UI_INDENTATION + "added: " + command);
                }

                System.out.println(UI_HORIZONTAL_LINE);
            }
        }
    }
}
