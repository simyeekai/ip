# Luke (SimBot) — Task Manager

Luke (aka **SimBot**) is a simple task manager with a friendly text UI and a lightweight persistence layer. It began from the Duke template and has been refactored around a single-turn handler suitable for both CLI and GUI front‑ends.

---

## Quick start

**Prerequisites**

* JDK **17**
* (Optional) IntelliJ IDEA (latest)

**Set up & run in IntelliJ**

1. Open the project folder in IntelliJ.
2. Ensure **Project SDK = 17** and **Language level = SDK default**.
3. Run the GUI launcher (if present): `Launcher.main()`.

   * If your project has only a CLI entry point, run the main class accordingly (e.g., `Luke.main()` / `Main.main()`), depending on your setup.

**Build a runnable JAR (Gradle-based projects)**

```bash
./gradlew clean shadowJar   # or 'jar' depending on your build script
java -jar build/libs/*-all.jar
```

> Note: The exact task (`shadowJar` vs `jar`) depends on your `build.gradle`.

---

## What’s inside

* `luke.Luke` — the core app logic. Exposes `getResponse(String input)` for GUI/CLI integration and maintains state (tasks, storage, exit flag).
* `Ui` — formatting/strings helper (kept for compatibility if needed).
* `Storage` — loads/saves the task list to local storage (file path is configured within `Storage`).
* `TaskList` — in‑memory list of tasks with convenience APIs (add, remove, find, get by index, size…).
* `Parser` — converts raw user input to a `ParsedCommand` (command type + args) and provides helpers like index parsing and token splitting.
* `Task` hierarchy — `Task`, `Todo`, `Deadline`, `Event`.
* `LukeException` — user‑facing errors with friendly messages.

> The exact package/class names above reflect the current code; adjust this section if you rename or reorganize files.

---

## Using SimBot

Type a command and the bot responds with a single message. The app keeps running until you use `bye`.

### Command summary

#### General

* `help` — Show help
* `bye` — Exit the app
* `echo <text>` — Echo back your text

#### Viewing & searching

* `list` — Show all tasks
* `find <keyword>` — Show tasks whose description contains `<keyword>`

#### Adding tasks

* `todo <desc>` — Add a to‑do
* `deadline <desc> /by <yyyy-mm-dd|free text>` — Add a deadline
* `event <desc> /from <start> /to <end>` — Add an event

#### Updating tasks

* `mark <index>` — Mark task at the given 1‑based index as done
* `unmark <index>` — Mark task as **not** done
* `delete <index>` — Delete task

### Examples

```text
help
```

Shows a concise command reference.

```text
todo read book
```

Adds a to‑do named "read book".

```text
deadline CS2103T iP /by 2025-09-30
```

Adds a deadline with a parsable ISO date.

```text
deadline submit taxes /by end of month
```

Adds a deadline using free‑text time (display only) if date parsing fails.

```text
event Hackathon /from 2025-10-10 09:00 /to 2025-10-11 18:00
```

Adds an event with start/end strings.

```text
list
```

Lists current tasks with 1‑based indices.

```text
mark 2
unmark 2
```

Toggles completion for task #2.

```text
delete 3
```

Removes task #3 and shows the remaining count.

```text
find book
```

Shows tasks whose description contains `book`.

```text
bye
```

Exits the application.

---

## Behavior & notes

* **Indices are 1‑based** (as shown in `list`).
* **Dates** for `deadline` accept `yyyy-mm-dd`. If parsing fails, the raw text is stored for display.
* **Persistence** is handled by `Storage` and writes to a local file. Open `Storage` to change the file path or format.
* **Error handling** uses `LukeException` to produce user‑friendly messages (e.g., empty command, missing `/by`, out‑of‑range indices).

---

## Extending Luke

* Add a new command by:

   1. Introducing a new `CommandType` in your parser/model.
   2. Teaching `Parser` to recognize the syntax and return a `ParsedCommand`.
   3. Handling the new type in `Luke#getResponse` with the desired state changes and a formatted response.

* Add a new task type by:

   1. Creating a subclass of `Task` (e.g., `FixedTimeTask`).
   2. Updating `Storage` (serialize/deserialize) and any formatting as needed.

---

## Troubleshooting

* **Nothing runs / red squiggles everywhere** → Check Project SDK = 17. Invalidate caches & restart if needed.
* **`NumberFormatException` / `IndexOutOfBoundsException` when marking/deleting** → Ensure you are using the index shown in `list` and that the list is not empty.
* **Date parsing errors** → Use `yyyy-mm-dd` for `deadline`. Free‑text still works but will be displayed as‑is.

---

## Acknowledgements

* Based on the **Duke** project template used in CS2103/T.
* Java 17 and IntelliJ documentation by JetBrains/Oracle.
