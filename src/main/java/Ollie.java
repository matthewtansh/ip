import java.util.Scanner;

public class Ollie {
    private static final String HORIZONTAL_LINE = "------------------------------------------------------------";

    public static void main(String[] args) {
        String banner = "  ___  _ _ _      \n"
                + " / _ \\| | (_) ___ \n"
                + "| | | | | | |/ _ \\\n"
                + "| |_| | | | |  __/\n"
                + " \\___/|_|_|_|\\___|\n";

        System.out.print(banner);
        System.out.println("Hello! I'm Ollie.");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                if (command.equals("bye")) {
                    System.out.println("    Bye. Hope to see you again soon!");
                    System.out.println(HORIZONTAL_LINE);
                    break;
                }

                System.out.println("    " + command);
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }
}
