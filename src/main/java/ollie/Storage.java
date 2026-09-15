package ollie;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";

    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() throws OllieException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
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

    private String formatTask(Task task) throws OllieException {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return String.join(FIELD_SEPARATOR, "T", status, task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, "D", status, deadline.getDescription(),
                    deadline.getDueDate().toString());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(FIELD_SEPARATOR, "E", status, event.getDescription(),
                    event.getStartDate().toString(), event.getEndDate().toString());
        }

        throw new OllieException("I couldn't save an unsupported task type.");
    }

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
                task = new Deadline(fields[2], parseDate(fields[3], lineNumber));
                break;
            case "E":
                if (fields.length != 5 || fields[3].isBlank() || fields[4].isBlank()) {
                    throw invalidData(lineNumber);
                }
                task = new Event(fields[2], parseDate(fields[3], lineNumber),
                        parseDate(fields[4], lineNumber));
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

    private LocalDate parseDate(String dateText, int lineNumber) throws OllieException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw invalidData(lineNumber);
        }
    }

    private OllieException invalidData(int lineNumber) {
        return new OllieException("The saved task on line " + lineNumber + " is invalid.");
    }
}
