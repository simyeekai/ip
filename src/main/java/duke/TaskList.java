package duke;

import java.util.ArrayList;
import java.util.List;

/** Holds the user's tasks. */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>(100); // compliant with ≤100 idea; scalable anyway

    public void add(Task t) { tasks.add(t); }
    public Task get(int oneBasedIndex) { return tasks.get(oneBasedIndex - 1); }
    public Task remove(int oneBasedIndex) { return tasks.remove(oneBasedIndex - 1); }
    public int size() { return tasks.size(); }
    public boolean isEmpty() { return tasks.isEmpty(); }
    public Task getZero(int zeroIdx) { return tasks.get(zeroIdx); } // unused, but handy
}
