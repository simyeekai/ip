package duke;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        if (by == null || by.isBlank()) {
            throw new DukeException("OOPS!!! Deadline must have a /by value.");
        }
        this.by = by;
    }

    @Override
    protected TaskType type() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
