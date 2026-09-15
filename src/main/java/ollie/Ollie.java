package ollie;

import java.nio.file.Path;

/**
 * Coordinates Ollie's user interface, command parsing, task list, and storage.
 */
public class Ollie {
    private static final Path DATA_FILE_PATH = Path.of("data", "ollie.txt");

    private final Parser parser;
    private final Storage storage;
    private final UI ui;
    private TaskList tasks;

    /**
     * Creates an Ollie chatbot that stores tasks at the given file path.
     *
     * @param filePath Path of the task data file.
     */
    public Ollie(Path filePath) {
        parser = new Parser();
        storage = new Storage(filePath);
        ui = new UI();
    }

    /**
     * Starts Ollie's command-processing loop.
     */
    public void run() {
        ui.showWelcome();
        tasks = loadTasks();
        ui.showDivider();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            boolean isExit = false;

            try {
                isExit = handleCommand(command);
            } catch (OllieException e) {
                ui.showError(e.getMessage());
            }

            ui.showDivider();
            if (isExit) {
                break;
            }
        }
    }

    /**
     * Starts Ollie using the default task data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Ollie(DATA_FILE_PATH).run();
    }

    /**
     * Performs the action represented by a user command.
     *
     * @param command Command entered by the user.
     * @return True if Ollie should exit after handling the command.
     * @throws OllieException If the command is invalid or its action fails.
     */
    private boolean handleCommand(String command) throws OllieException {
        CommandType commandType = parser.parseCommand(command);

        switch (commandType) {
            case BYE:
                ui.showGoodbye();
                return true;
            case HELP:
                ui.showHelp();
                break;
            case LIST:
                ui.showTaskList(tasks);
                break;
            case MARK:
                int markIndex = parser.parseTaskIndex(command, "mark", tasks.size());
                tasks.mark(markIndex);
                saveTasks();
                ui.showTaskMarked();
                break;
            case UNMARK:
                int unmarkIndex = parser.parseTaskIndex(command, "unmark", tasks.size());
                tasks.unmark(unmarkIndex);
                saveTasks();
                ui.showTaskUnmarked();
                break;
            case DELETE:
                int deleteIndex = parser.parseTaskIndex(command, "delete", tasks.size());
                tasks.delete(deleteIndex);
                saveTasks();
                ui.showTaskDeleted();
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
            case UNKNOWN:
                addTask(parser.parseTask(command));
                break;
            default:
                throw new OllieException("I don't recognize that command.");
        }

        return false;
    }

    /**
     * Loads saved tasks, or starts with an empty task list if loading fails.
     *
     * @return Loaded task list, or an empty task list.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (OllieException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Adds a task, saves the task list, and reports the addition.
     *
     * @param task Task to add.
     * @throws OllieException If the task list cannot be saved.
     */
    private void addTask(Task task) throws OllieException {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded();
    }

    /**
     * Saves the current task list.
     *
     * @throws OllieException If the task list cannot be saved.
     */
    private void saveTasks() throws OllieException {
        storage.save(tasks.getTasks());
    }
}
