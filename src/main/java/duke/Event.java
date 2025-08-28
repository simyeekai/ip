package duke;

public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from == null || from.isBlank() || to == null || to.isBlank()) {
            throw new DukeException("OOPS!!! Event must have /from and /to.");
        }
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    protected TaskType type() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
