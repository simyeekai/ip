package duke; //update 4

import java.util.Scanner;

public class Duke {
    private static final String BOT_NAME = "SimBot";
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Duke() {
        this.ui = new Ui(BOT_NAME);
        this.storage = new Storage();
        this.tasks = storage.load();
    }

    public static void main(String[] args) {
        new Duke().run();
    }

    private void run() {
        ui.showWelcome();
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                String line = sc.hasNextLine() ? sc.nextLine().trim() : "bye";
                if (line.isEmpty()) {
                    ui.showError("OOPS!!! Empty command.");
                    continue;
                }
                ParsedCommand pc = Parser.parse(line);
                switch (pc.type()) {
                    case BYE:
                        ui.showGoodbye();
                        return;
                    case LIST:
                        ui.showList(tasks);
                        break;
                    case TODO: {
                        Task t = new Todo(pc.args());
                        tasks.add(t);
                        storage.save(tasks);
                        ui.showAdded(t, tasks.size());
                        break;
                    }
                    case DEADLINE: {
                        String[] parts = Parser.splitOnce(pc.args(), "/by");
                        if (parts[0].isBlank() || parts[1].isBlank()) {
                            throw new DukeException("OOPS!!! Deadline requires description and /by time.");
                        }
                        String desc = parts[0].trim();
                        String raw = parts[1].trim();

                        Task d;
                        try {
                            d = new Deadline(desc, java.time.LocalDate.parse(raw));
                        } catch (Exception e) {
                            d = new Deadline(desc, raw);
                        }

                        tasks.add(d);
                        storage.save(tasks);
                        ui.showAdded(d, tasks.size());
                        break;
                    }

                    case EVENT: {
                        String[] parts = Parser.splitTwo(pc.args(), "/from", "/to");
                        if (parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
                            throw new DukeException("OOPS!!! Event requires description, /from and /to.");
                        }
                        Task e = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
                        tasks.add(e);
                        storage.save(tasks);
                        ui.showAdded(e, tasks.size());
                        break;
                    }
                    case MARK: {
                        int idx = Parser.requireIndex(pc.args(), tasks.size());
                        Task target = tasks.get(idx);
                        target.mark();
                        storage.save(tasks);
                        ui.showMarked(target, true);
                        break;
                    }
                    case UNMARK: {
                        int idx = Parser.requireIndex(pc.args(), tasks.size());
                        Task target = tasks.get(idx);
                        target.unmark();
                        storage.save(tasks);
                        ui.showMarked(target, false);
                        break;
                    }
                    case DELETE: {
                        int idx = Parser.requireIndex(pc.args(), tasks.size());
                        Task removed = tasks.remove(idx);
                        storage.save(tasks);
                        ui.showDeleted(removed, tasks.size());
                        break;
                    }
                    case FIND: {
                        String keyword = pc.args().trim();
                        java.util.List<Task> matches = tasks.find(keyword);
                        ui.showMatches(matches);
                        break;
                    }
                    case ECHO:
                        ui.showEcho(pc.args());
                        break;
                    default:
                        throw new DukeException("OOPS!! I'm sorry, but I don't know what that means :(");
                }
            }
        } catch (DukeException e) {
            ui.showError(e.getMessage());
        }
    }
}
