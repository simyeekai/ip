package duke;

import java.util.Scanner;

/**
 * Duke main program: reads commands, delegates to TaskList/Parser/Ui.
 */
public class Duke {
    private static final String BOT_NAME = "SimBot"; // <- customize me!
    private final Ui ui;
    private final TaskList tasks;

    public Duke() {
        this.ui = new Ui(BOT_NAME);
        this.tasks = new TaskList();
    }

    public static void main(String[] args) {
        new Duke().run();
    }

    private void run() {
        ui.showWelcome();
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                String line = sc.nextLine().trim();
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
                    case TODO:
                        Task t = new Todo(pc.args());
                        tasks.add(t);
                        ui.showAdded(t, tasks.size());
                        break;
                    case DEADLINE: {
                        String[] parts = Parser.splitOnce(pc.args(), "/by");
                        if (parts[0].isBlank() || parts[1].isBlank()) {
                            throw new DukeException("OOPS!!! Deadline requires description and /by time.");
                        }
                        Task d = new Deadline(parts[0].trim(), parts[1].trim());
                        tasks.add(d);
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
                        ui.showAdded(e, tasks.size());
                        break;
                    }
                    case MARK:
                    case UNMARK: {
                        int idx = Parser.requireIndex(pc.args(), tasks.size());
                        Task target = tasks.get(idx);
                        if (pc.type() == CommandType.MARK) {
                            target.mark();
                            ui.showMarked(target, true);
                        } else {
                            target.unmark();
                            ui.showMarked(target, false);
                        }
                        break;
                    }
                    case DELETE: {
                        int idx = Parser.requireIndex(pc.args(), tasks.size());
                        Task removed = tasks.remove(idx);
                        ui.showDeleted(removed, tasks.size());
                        break;
                    }
                    case ECHO: // Level-1 behavior (only used before Level-5 error handling kicks in)
                        ui.showEcho(pc.args());
                        break;
                    default:
                        throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            }
        } catch (DukeException e) {
            ui.showError(e.getMessage());
            // Keep running after error (optional). For simplicity, exit on fatal parse issues:
        }
    }
}
