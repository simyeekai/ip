package luke;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Storage {
    private final Path filePath;

    public Storage() {
        this.filePath = Paths.get("data", "luke.txt");
    }

    public TaskList load() {
        try {
            if (!Files.exists(filePath)) {
                return new TaskList();
            }
            TaskList tasks = new TaskList();
            try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("\\|");
                    for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                    if (parts.length < 3) continue;

                    String kind = parts[0];
                    boolean done = "1".equals(parts[1]);
                    String desc = parts[2];
                    Task t;
                    switch (kind) {
                        case "T":
                            t = new Todo(desc);
                            break;
                        case "D":
                            if (parts.length < 4) continue;
                            Task d;
                            try {
                                d = new Deadline(desc, java.time.LocalDate.parse(parts[3]));
                            } catch (Exception ex) {
                                d = new Deadline(desc, parts[3]);
                            }
                            t = d;
                            break;
                        case "E":
                            if (parts.length < 5) continue;
                            t = new Event(desc, parts[3], parts[4]);
                            break;
                        default:
                            continue;
                    }
                    if (done) t.mark();
                    tasks.add(t);
                }
            }
            return tasks;
        } catch (IOException e) {
            return new TaskList();
        }
    }

    public void save(TaskList tasks) {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (int i = 0; i < tasks.size(); i++) {
                    Task t = tasks.get(i + 1);
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (IOException e) {

        }
    }

    private String serialize(Task t) {
        String done = t.isDone() ? "1" : "0";
        if (t instanceof Todo) {
            return String.join(" | ", "T", done, t.description());
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return String.join(" | ", "D", done, d.description(), d.getByIsoOrText());
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return String.join(" | ", "E", done, e.description(), e.getFrom(), e.getTo());
        } else {
            // Fallback: store as a Todo line
            return String.join(" | ", "T", done, t.description());
        }
    }
}
