import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Ollie's tasks in a text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";

    private final Path filePath;

    /**
     * Creates storage that uses the specified file path.
     *
     * @param filePath Relative path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return Tasks reconstructed from the data file, or an empty list if the file does not exist.
     * @throws OllieException If the file cannot be read or contains invalid task data.
     */
    public ArrayList<Task> load() throws OllieException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            ArrayList<Task> tasks = new ArrayList<>();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).isBlank()) {
                    tasks.add(parseTask(lines.get(i), i + 1));
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new OllieException("I couldn't load your tasks from " + filePath + ".");
        }
    }

    /**
     * Replaces the data file with the current task list.
     *
     * @param tasks Tasks to save.
     * @throws OllieException If the tasks cannot be written to the data file.
     */
    public void save(List<Task> tasks) throws OllieException {
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new OllieException("I couldn't save your tasks to " + filePath + ".");
        }
    }

    /**
     * Converts a task into one line of data for the save file.
     *
     * @param task Task to convert.
     * @return Serialized task data.
     * @throws OllieException If the task type is not supported.
     */
    private String formatTask(Task task) throws OllieException {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return String.join(FIELD_SEPARATOR, "T", status, task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, "D", status, deadline.getDescription(), deadline.getBy());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(FIELD_SEPARATOR, "E", status, event.getDescription(),
                    event.getFrom(), event.getTo());
        }

        throw new OllieException("I couldn't save an unsupported task type.");
    }

    /**
     * Reconstructs and validates a task from one line of saved data.
     *
     * @param line Saved task data.
     * @param lineNumber One-based line number used in error messages.
     * @return Reconstructed task.
     * @throws OllieException If the saved task data is invalid.
     */
    private Task parseTask(String line, int lineNumber) throws OllieException {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);
        if (fields.length < 3 || fields[2].isBlank()) {
            throw invalidData(lineNumber);
        }

        Task task;
        switch (fields[0]) {
            case "T":
                if (fields.length != 3) {
                    throw invalidData(lineNumber);
                }
                task = new Todo(fields[2]);
                break;
            case "D":
                if (fields.length != 4 || fields[3].isBlank()) {
                    throw invalidData(lineNumber);
                }
                task = new Deadline(fields[2], fields[3]);
                break;
            case "E":
                if (fields.length != 5 || fields[3].isBlank() || fields[4].isBlank()) {
                    throw invalidData(lineNumber);
                }
                task = new Event(fields[2], fields[3], fields[4]);
                break;
            default:
                throw invalidData(lineNumber);
        }

        if (fields[1].equals("1")) {
            task.mark();
        } else if (!fields[1].equals("0")) {
            throw invalidData(lineNumber);
        }
        return task;
    }

    /** Returns an exception identifying an invalid line in the data file. */
    private OllieException invalidData(int lineNumber) {
        return new OllieException("The saved task on line " + lineNumber + " is invalid.");
    }
}
