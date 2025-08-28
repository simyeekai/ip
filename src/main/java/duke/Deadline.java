package duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate byDate;
    private final String byText;

    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, LocalDate byDate) {
        super(description);
        if (byDate == null) {
            throw new DukeException("OOPS!!! Deadline date cannot be null.");
        }
        this.byDate = byDate;
        this.byText = byDate.toString();
    }

    public Deadline(String description, String byText) {
        super(description);
        if (byText == null || byText.isBlank()) {
            throw new DukeException("OOPS!!! Deadline must have a /by value.");
        }
        this.byText = byText.trim();
        LocalDate parsed = null;
        try { parsed = LocalDate.parse(this.byText); } catch (Exception ignored) { }
        this.byDate = parsed;
    }

    public boolean hasDate() { return byDate != null; }
    public LocalDate getByDate() { return byDate; }
    public String getByText() { return byText; }
    public String getByIsoOrText() { return hasDate() ? byDate.toString() : byText; }

    @Override
    protected TaskType type() { return TaskType.DEADLINE; }

    @Override
    public String toString() {
        String shown = hasDate() ? byDate.format(DISPLAY_FMT) : byText;
        return super.toString() + " (by: " + shown + ")";
    }
}
