package ollie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description Description of the deadline.
     * @param by Date by which the deadline should be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date of this deadline.
     *
     * @return Due date of this deadline.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns a display-friendly representation of this deadline.
     *
     * @return Formatted deadline.
     */
    @Override
    public String toString() {
        return "[deadline]" + super.toString() + " (by: " + by.format(DATE_FORMATTER) + ")";
    }
}
