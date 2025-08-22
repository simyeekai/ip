package duke;

/** Parsed command with type and raw args. */
public record ParsedCommand(CommandType type, String args) { }
