package duke;

public class Ui {
    private final String botName;
    private static final String LINE = "____________________________________________________________";

    public Ui(String botName) {
        this.botName = botName;
    }

    private void box(String... lines) {
        System.out.println(LINE);
        for (String s : lines) {
            System.out.println(" " + s);
        }
        System.out.println(LINE);
    }

    public void showWelcome() {
        box("Hello! I'm " + botName, "What can I do for you?");
    }

    public void showGoodbye() {
        box("Bye. Hope to see you again soon!");
    }

    public void showEcho(String msg) {
        box(msg);
    }

    public void showList(TaskList tasks) {
        if (tasks.isEmpty()) {
            box("Your list is empty.");
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("\n ").append(i + 1).append(".").append(tasks.getZero(i));
        }
        box(sb.toString());
    }

    public void showAdded(Task t, int count) {
        box("Got it. I've added this task:",
                "  " + t,
                "Now you have " + count + " task" + (count == 1 ? "" : "s") + " in the list.");
    }

    public void showDeleted(Task t, int count) {
        box("Noted. I've removed this task:",
                "  " + t,
                "Now you have " + count + " task" + (count == 1 ? "" : "s") + " in the list.");
    }

    public void showMarked(Task t, boolean done) {
        if (done) {
            box("Nice! I've marked this task as done:", "  " + t);
        } else {
            box("OK, I've marked this task as not done yet:", "  " + t);
        }
    }

    public void showMatches(java.util.List<Task> matches) {
        if (matches.isEmpty()) {
            box("No matching tasks found.");
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            sb.append("\n ").append(i + 1).append(".").append(matches.get(i));
        }
        box(sb.toString());
    }


    public void showError(String msg) {
        box(msg);
    }
}
