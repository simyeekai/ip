package duke;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    protected TaskType type() {
        return TaskType.TODO;
    }
}
