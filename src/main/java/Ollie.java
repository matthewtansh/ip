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

                if (command.equals("help")) {
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
                } else if (command.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(command.substring("mark ".length())) - 1;
                    Task task = tasks.get(taskIndex);
                    task.mark();
                    System.out.println(UI_INDENTATION + "Nice! I've marked this task as done.");
                } else if (command.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(command.substring("unmark ".length())) - 1;
                    Task task = tasks.get(taskIndex);
                    task.unmark();
                    System.out.println(UI_INDENTATION + "Nice! I've marked this task as undone.");
                } else if (command.startsWith("todo ")) {
                    String description = command.substring("todo ".length());
                    addTask(tasks, new Todo(description));
                } else if (command.startsWith("deadline ")) {
                    String[] deadlineParts = command.substring("deadline ".length()).split(" /by ", 2);
                    addTask(tasks, new Deadline(deadlineParts[0], deadlineParts[1]));
                } else if (command.startsWith("event ")) {
                    String[] eventParts = command.substring("event ".length()).split(" /from ", 2);
                    String[] timeParts = eventParts[1].split(" /to ", 2);
                    addTask(tasks, new Event(eventParts[0], timeParts[0], timeParts[1]));
                }

                System.out.println(UI_HORIZONTAL_LINE);
            }
        }
    }

    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println(UI_INDENTATION + "Got it. I've added this task.");
    }
}
