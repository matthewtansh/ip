package ollie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    public void parseCommand_supportedAndUnknownCommands_returnsCorrectTypes() {
        assertEquals(CommandType.HELP, parser.parseCommand("help"));
        assertEquals(CommandType.LIST, parser.parseCommand("list"));
        assertEquals(CommandType.FIND, parser.parseCommand("find book"));
        assertEquals(CommandType.TODO, parser.parseCommand("todo read book"));
        assertEquals(CommandType.DEADLINE, parser.parseCommand("deadline return book /by 2019-12-02"));
        assertEquals(CommandType.EVENT, parser.parseCommand("event meeting /from 2019-12-03 /to 2019-12-05"));
        assertEquals(CommandType.MARK, parser.parseCommand("mark 1"));
        assertEquals(CommandType.UNMARK, parser.parseCommand("unmark 1"));
        assertEquals(CommandType.DELETE, parser.parseCommand("delete 1"));
        assertEquals(CommandType.BYE, parser.parseCommand("bye"));
        assertEquals(CommandType.UNKNOWN, parser.parseCommand("unknown"));
    }

    @Test
    public void parseFindKeyword_validAndMissingKeyword_returnsKeywordOrThrowsException()
            throws OllieException {
        assertEquals("book", parser.parseFindKeyword("find book"));
        assertThrows(OllieException.class, () -> parser.parseFindKeyword("find"));
    }

    @Test
    public void parseTask_validDeadline_returnsParsedDeadline() throws OllieException {
        Task task = parser.parseTask("deadline return book /by 2019-12-02");

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDate.of(2019, 12, 2), deadline.getBy());
    }

    @Test
    public void parseTask_validEvent_returnsParsedEvent() throws OllieException {
        Task task = parser.parseTask("event meeting /from 2019-12-03 /to 2019-12-05");

        Event event = assertInstanceOf(Event.class, task);
        assertEquals("meeting", event.getDescription());
        assertEquals(LocalDate.of(2019, 12, 3), event.getFrom());
        assertEquals(LocalDate.of(2019, 12, 5), event.getTo());
    }

    @Test
    public void parseTask_invalidOrIncompleteCommands_throwsOllieException() {
        assertThrows(OllieException.class, () -> parser.parseTask(""));
        assertThrows(OllieException.class, () -> parser.parseTask("unknown"));
        assertThrows(OllieException.class, () -> parser.parseTask("todo"));
        assertThrows(OllieException.class, () -> parser.parseTask("deadline return book"));
        assertThrows(OllieException.class, () -> parser.parseTask("deadline return book /by 2019-02-30"));
        assertThrows(OllieException.class, () -> parser.parseTask("event meeting /from 2019-12-03"));
    }

    @Test
    public void parseTaskIndex_validTaskNumber_returnsZeroBasedIndex() throws OllieException {
        assertEquals(1, parser.parseTaskIndex("mark 2", "mark", 3));
    }

    @Test
    public void parseTaskIndex_invalidTaskNumbers_throwOllieException() {
        assertThrows(OllieException.class, () -> parser.parseTaskIndex("mark", "mark", 3));
        assertThrows(OllieException.class, () -> parser.parseTaskIndex("mark two", "mark", 3));
        assertThrows(OllieException.class, () -> parser.parseTaskIndex("mark 0", "mark", 3));
        assertThrows(OllieException.class, () -> parser.parseTaskIndex("mark 4", "mark", 3));
        assertThrows(OllieException.class, () -> parser.parseTaskIndex("mark 1", "mark", 0));
    }
}
