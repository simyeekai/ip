package duke;

/** Custom exception for Duke errors. */
public class DukeException extends RuntimeException {
    public DukeException(String message) {
        super(message);
    }
}
