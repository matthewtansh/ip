package ollie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that takes place between two dates.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an event with the given description, start date, and end date.
     *
     * @param description Description of the event.
     * @param from Date on which the event starts.
     * @param to Date on which the event ends.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date of this event.
     *
     * @return Start date of this event.
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns the end date of this event.
     *
     * @return End date of this event.
     */
    public LocalDate getTo() {
        return to;
    }

    /**
     * Returns a display-friendly representation of this event.
     *
     * @return Formatted event.
     */
    @Override
    public String toString() {
        return "[event]" + super.toString() + " (from: " + from.format(DATE_FORMATTER)
                + " to: " + to.format(DATE_FORMATTER) + ")";
    }
}
