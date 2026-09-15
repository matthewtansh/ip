package ollie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void find_mixedTaskTypesAndCase_returnsMatchingTasksInOriginalOrder() {
        TaskList tasks = new TaskList(List.of(
                new Todo("Read Book"),
                new Deadline("return book", LocalDate.of(2019, 12, 2)),
                new Event("project meeting", LocalDate.of(2019, 12, 3),
                        LocalDate.of(2019, 12, 5))));

        TaskList matchingTasks = tasks.find("BOOK");

        assertEquals(2, matchingTasks.size());
        assertEquals("Read Book", matchingTasks.get(0).getDescription());
        assertEquals("return book", matchingTasks.get(1).getDescription());
    }

    @Test
    public void find_noMatchingTasks_returnsEmptyTaskList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        TaskList matchingTasks = tasks.find("meeting");

        assertEquals(0, matchingTasks.size());
    }
}
