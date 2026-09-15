package ollie;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks and provides operations that modify their state.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing copies of the given task references.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given zero-based index as completed.
     *
     * @param index Zero-based task index.
     */
    public void mark(int index) {
        tasks.get(index).mark();
    }

    /**
     * Marks the task at the given zero-based index as incomplete.
     *
     * @param index Zero-based task index.
     */
    public void unmark(int index) {
        tasks.get(index).unmark();
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index Zero-based task index.
     * @return Task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable copy of the stored tasks.
     *
     * @return Copy of the stored tasks.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
}
