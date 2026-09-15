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
        ensureTasksLoaded();
        ui.showDivider();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandResult result = null;

            try {
                result = handleCommand(command);
                ui.showResponse(result.response());
            } catch (OllieException e) {
                ui.showError(e.getMessage());
            }

            ui.showDivider();
            if (result != null && result.isExit()) {
                break;
            }
        }
    }

    /**
     * Processes a command and returns Ollie's response for a graphical interface.
     *
     * @param command Command entered by the user.
     * @return User-facing response to the command.
     */
    public String getResponse(String command) {
        ensureTasksLoaded();
        try {
            return handleCommand(command.trim()).response();
        } catch (OllieException e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    /**
     * Returns whether the command ends the current chatbot session.
     *
     * @param command User command to inspect.
     * @return True if the command is the bye command.
     */
    public boolean isExitCommand(String command) {
        return parser.parseCommand(command.trim()) == CommandType.BYE;
    }

    /**
     * Creates an Ollie chatbot that uses the default task data file.
     *
     * @return Ollie configured with the default storage path.
     */
    public static Ollie createDefault() {
        return new Ollie(DATA_FILE_PATH);
    }

    /**
     * Starts Ollie using the default task data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        createDefault().run();
    }

    /**
     * Performs the action represented by a user command.
     *
     * @param command Command entered by the user.
     * @return Response and exit state produced by the command.
     * @throws OllieException If the command is invalid or its action fails.
     */
    private CommandResult handleCommand(String command) throws OllieException {
        CommandType commandType = parser.parseCommand(command);

        switch (commandType) {
            case BYE:
                return new CommandResult(ui.getGoodbyeMessage(), true);
            case HELP:
                return new CommandResult(ui.getHelpMessage(), false);
            case LIST:
                return new CommandResult(ui.getTaskListMessage(tasks), false);
            case FIND:
                String keyword = parser.parseFindKeyword(command);
                return new CommandResult(ui.getMatchingTasksMessage(tasks.find(keyword)), false);
            case MARK:
                int markIndex = parser.parseTaskIndex(command, "mark", tasks.size());
                tasks.mark(markIndex);
                saveTasks();
                return new CommandResult(ui.getTaskMarkedMessage(), false);
            case UNMARK:
                int unmarkIndex = parser.parseTaskIndex(command, "unmark", tasks.size());
                tasks.unmark(unmarkIndex);
                saveTasks();
                return new CommandResult(ui.getTaskUnmarkedMessage(), false);
            case DELETE:
                int deleteIndex = parser.parseTaskIndex(command, "delete", tasks.size());
                tasks.delete(deleteIndex);
                saveTasks();
                return new CommandResult(ui.getTaskDeletedMessage(), false);
            case TODO, DEADLINE, EVENT, UNKNOWN:
                addTask(parser.parseTask(command));
                return new CommandResult(ui.getTaskAddedMessage(), false);
            default:
                throw new OllieException("I don't recognize that command.");
        }
    }

    /**
     * Loads saved tasks before processing the first command.
     */
    private void ensureTasksLoaded() {
        if (tasks == null) {
            tasks = loadTasks();
        }
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
     * Adds a task and saves the task list.
     *
     * @param task Task to add.
     * @throws OllieException If the task list cannot be saved.
     */
    private void addTask(Task task) throws OllieException {
        tasks.add(task);
        saveTasks();
    }

    /**
     * Saves the current task list.
     *
     * @throws OllieException If the task list cannot be saved.
     */
    private void saveTasks() throws OllieException {
        storage.save(tasks.getTasks());
    }

    /**
     * Contains the response and exit state produced by a command.
     *
     * @param response User-facing response.
     * @param isExit Whether the command ends a console session.
     */
    private record CommandResult(String response, boolean isExit) {
    }
}
