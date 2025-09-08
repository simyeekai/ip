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
        String q = keyword == null ? "" : keyword.trim();
        if (q.isEmpty()) {
            return List.of();
        }
        String k = q.toLowerCase();

        return tasks.stream()
                .filter(t -> t.toString().toLowerCase().contains(k))
                .toList();
    }
}
