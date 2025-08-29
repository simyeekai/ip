package duke;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>(100);

    public void add(Task t) { tasks.add(t); }
    public Task get(int oneBasedIndex) { return tasks.get(oneBasedIndex - 1); }
    public Task remove(int oneBasedIndex) { return tasks.remove(oneBasedIndex - 1); }
    public int size() { return tasks.size(); }
    public boolean isEmpty() { return tasks.isEmpty(); }
    public Task getZero(int zeroIdx) { return tasks.get(zeroIdx); }

    public List<Task> find(String keyword) {
        String k = keyword.toLowerCase(Locale.ROOT).trim();
        List<Task> out = new ArrayList<>();
        for (Task t : tasks) {
            if (t.description().toLowerCase(Locale.ROOT).contains(k)) {
                out.add(t);
            }
        }
        return out;
    }
}
