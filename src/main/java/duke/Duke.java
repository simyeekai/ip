package duke; //ai

import java.util.List;

public class Duke {
    private static final String BOT_NAME = "SimBot";
    private boolean shouldExit = false;

    // Kept for compatibility if you still print from Ui elsewhere
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Duke() {
        this.ui = new Ui(BOT_NAME);
        this.storage = new Storage();
        this.tasks = storage.load();
    }

    /** Whether the app should exit (after "bye"). */
    public boolean shouldExit() {
        return shouldExit;
    }

    /**
     * GUI-friendly single-turn handler.
     * Parses a single input, mutates state, and returns a response String.
     */
    public String getResponse(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                throw new DukeException("OOPS!!! Empty command.");
            }

            ParsedCommand pc = Parser.parse(input.trim());

            switch (pc.type()) {
                case BYE:
                    shouldExit = true;
                    return "Bye. Hope to see you again soon!";

                case LIST:
                    return formatList(tasks);

                case TODO: {
                    Task t = new Todo(pc.args());
                    tasks.add(t);
                    storage.save(tasks);
                    return formatAdded(t, tasks.size());
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
                    return formatAdded(d, tasks.size());
                }

                case EVENT: {
                    String[] parts = Parser.splitTwo(pc.args(), "/from", "/to");
                    if (parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
                        throw new DukeException("OOPS!!! Event requires description, /from and /to.");
                    }
                    Task e = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
                    tasks.add(e);
                    storage.save(tasks);
                    return formatAdded(e, tasks.size());
                }

                case MARK: {
                    int idx = Parser.requireIndex(pc.args(), tasks.size());
                    Task target = tasks.get(idx);
                    target.mark();
                    storage.save(tasks);
                    return "Nice! I've marked this task as done:\n  " + target;
                }

                case UNMARK: {
                    int idx = Parser.requireIndex(pc.args(), tasks.size());
                    Task target = tasks.get(idx);
                    target.unmark();
                    storage.save(tasks);
                    return "OK, I've marked this task as not done yet:\n  " + target;
                }

                case DELETE: {
                    int idx = Parser.requireIndex(pc.args(), tasks.size());
                    Task removed = tasks.remove(idx);
                    storage.save(tasks);
                    return "Noted. I've removed this task:\n  " + removed
                            + "\nNow you have " + tasks.size() + " tasks in the list.";
                }

                case FIND: {
                    String keyword = pc.args().trim();
                    List<Task> matches = tasks.find(keyword);
                    return formatMatches(matches, keyword);
                }

                case ECHO:
                    return pc.args();

                case HELP:
                    return getHelpText();

                default:
                    throw new DukeException("OOPS!! I'm sorry, but I don't know what that means :(");
            }
        } catch (DukeException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    /* ---------------- Formatting helpers ---------------- */

    private String formatList(TaskList tasks) {
        if (tasks.size() == 0) {
            return "Your list is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("\n").append(i + 1).append(". ").append(tasks.get(i));
        }
        return sb.toString();
    }

    private String formatAdded(Task t, int newSize) {
        return "Got it. I've added this task:\n  " + t
                + "\nNow you have " + newSize + " tasks in the list.";
    }

    private String formatMatches(List<Task> matches, String keyword) {
        if (keyword.isEmpty()) {
            return "Please provide a keyword to find.";
        }
        if (matches.isEmpty()) {
            return "No matching tasks found for \"" + keyword + "\".";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            sb.append("\n").append(i + 1).append(". ").append(matches.get(i));
        }
        return sb.toString();
    }

    private String getHelpText() {
        return String.join("\n",
                "SimBot Help",
                "-------------------------",
                "General:",
                "  help                    Show this help",
                "  bye                     Exit the app",
                "",
                "Viewing & Searching:",
                "  list                    Show all tasks",
                "  find <keyword>          Find tasks containing keyword",
                "",
                "Adding:",
                "  todo <desc>             Add a todo",
                "  deadline <desc> /by <yyyy-mm-dd|free text>",
                "  event <desc> /from <start> /to <end>",
                "",
                "Updating:",
                "  mark <index>            Mark task done",
                "  unmark <index>          Mark task not done",
                "  delete <index>          Delete task",
                "",
                "Notes:",
                "  • Indices refer to positions shown in 'list' (starting from 1).",
                "  • Dates accept ISO format (yyyy-mm-dd) or free text for display."
        );
    }

}
