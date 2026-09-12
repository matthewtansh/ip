import java.nio.file.Path;

public class Ollie {
    private static final Path DATA_FILE_PATH = Path.of("data", "ollie.txt");

    private final Parser parser;
    private final Storage storage;
    private final UI ui;
    private TaskList tasks;

    public Ollie(Path filePath) {
        parser = new Parser();
        storage = new Storage(filePath);
        ui = new UI();
    }

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

    public static void main(String[] args) {
        new Ollie(DATA_FILE_PATH).run();
    }

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

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (OllieException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    private void addTask(Task task) throws OllieException {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded();
    }

    private void saveTasks() throws OllieException {
        storage.save(tasks.getTasks());
    }
}
