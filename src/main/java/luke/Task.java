package luke;

public abstract class Task {
    private final String description;
    private boolean isDone;

    protected Task(String description) {
        if (description == null || description.isBlank()) {
            throw new LukeException("OOPS!!! The description of a task cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    public void mark() { this.isDone = true; }
    public void unmark() { this.isDone = false; }
    public boolean isDone() { return isDone; }
    public String description() { return description; }

    protected abstract TaskType type();

    protected String statusBox() { return isDone ? "[X]" : "[ ]"; }

    @Override
    public String toString() {
        return "[" + type().tag() + "]" + statusBox() + " " + description;
    }
}
