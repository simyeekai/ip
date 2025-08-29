package duke;

/** Parses user input into command + args. */
public class Parser {

    public static ParsedCommand parse(String line) {
        String[] firstSplit = line.trim().split("\\s+", 2);
        String cmd = firstSplit[0].toLowerCase();
        String args = firstSplit.length > 1 ? firstSplit[1] : "";

        switch (cmd) {
            case "bye": return new ParsedCommand(CommandType.BYE, "");
            case "list": return new ParsedCommand(CommandType.LIST, "");
            case "todo":
                if (args.isBlank()) throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
                return new ParsedCommand(CommandType.TODO, args);
            case "deadline":
                if (args.isBlank()) throw new DukeException("OOPS!!! The description of a deadline cannot be empty.");
                return new ParsedCommand(CommandType.DEADLINE, args);
            case "event":
                if (args.isBlank()) throw new DukeException("OOPS!!! The description of an event cannot be empty.");
                return new ParsedCommand(CommandType.EVENT, args);
            case "mark":
                if (args.isBlank()) throw new DukeException("OOPS!!! Provide an index to mark.");
                return new ParsedCommand(CommandType.MARK, args);
            case "unmark":
                if (args.isBlank()) throw new DukeException("OOPS!!! Provide an index to unmark.");
                return new ParsedCommand(CommandType.UNMARK, args);
            case "delete":
                if (args.isBlank()) throw new DukeException("OOPS!!! Provide an index to delete.");
                return new ParsedCommand(CommandType.DELETE, args);
            case "find":
                if (args.isBlank()) throw new DukeException("OOPS!!! Provide a keyword to find.");
                return new ParsedCommand(CommandType.FIND, args);
            default:
                // For Level-1 echo: treat unknown token as echo of the whole line.
                // After Level-5 this path will throw instead (we do that in Duke switch).
                return new ParsedCommand(CommandType.UNKNOWN, line);
        }
    }

    /** Splits "desc /by when" once. Returns [desc, when]. */
    public static String[] splitOnce(String src, String delimiter) {
        int idx = src.indexOf(delimiter);
        if (idx < 0) return new String[] { src, "" };
        String left = src.substring(0, idx).trim();
        String right = src.substring(idx + delimiter.length()).trim();
        return new String[] { left, right };
    }

    /** Splits "desc /from a /to b". Returns [desc, a, b]. */
    public static String[] splitTwo(String src, String d1, String d2) {
        int i1 = src.indexOf(d1);
        int i2 = src.indexOf(d2);
        if (i1 < 0 || i2 < 0 || i2 <= i1) {
            return new String[] { src, "", "" };
        }
        String desc = src.substring(0, i1).trim();
        String from = src.substring(i1 + d1.length(), i2).trim();
        String to = src.substring(i2 + d2.length()).trim();
        return new String[] { desc, from, to };
    }

    /** Parses a 1-based index and bounds-checks against size. */
    public static int requireIndex(String arg, int size) {
        try {
            int idx = Integer.parseInt(arg.trim());
            if (idx < 1 || idx > size) throw new DukeException("OOPS!!! Index out of range.");
            return idx;
        } catch (NumberFormatException e) {
            throw new DukeException("OOPS!!! That's not a valid index.");
        }
    }
}
