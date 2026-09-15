package ollie;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class OllieTest {
    @TempDir
    private Path tempDirectory;

    @Test
    public void getResponse_addListAndFindCommands_returnsExpectedResponses() {
        Ollie ollie = new Ollie(tempDirectory.resolve("data").resolve("tasks.txt"));

        assertTrue(ollie.getResponse("todo Read Book").contains("added this task"));
        assertTrue(ollie.getResponse("list").contains("[todo][ ] Read Book"));
        assertTrue(ollie.getResponse("find book").contains("[todo][ ] Read Book"));
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorResponse() {
        Ollie ollie = new Ollie(tempDirectory.resolve("tasks.txt"));

        Ollie.CommandResponse response = ollie.getCommandResponse("unknown");

        assertTrue(response.message().contains("OOPS!"));
        assertTrue(response.isError());
        assertFalse(response.isExit());
    }

    @Test
    public void isExitCommand_byeCommand_returnsTrue() {
        Ollie ollie = new Ollie(tempDirectory.resolve("tasks.txt"));

        assertTrue(ollie.isExitCommand("bye"));
        assertFalse(ollie.isExitCommand("todo say bye"));
    }

    @Test
    public void getResponse_helpCommand_returnsUnindentedReadableUsage() {
        Ollie ollie = new Ollie(tempDirectory.resolve("tasks.txt"));

        String response = ollie.getResponse("help");

        assertTrue(response.startsWith("Here are the commands I understand:"));
        assertTrue(response.contains("• deadline <description>"
                + System.lineSeparator() + "    /by <yyyy-MM-dd>"));
        assertTrue(response.contains("• event <description>"
                + System.lineSeparator() + "    /from <yyyy-MM-dd>"
                + System.lineSeparator() + "    /to <yyyy-MM-dd>"));
    }
}
