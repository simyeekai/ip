package luke;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Luke luke;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image lukeImage = new Image(this.getClass().getResourceAsStream("/images/DaLuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Luke instance */
    public void setLuke(Luke d) {
        luke = d;
        // Optional greeting on startup
        dialogContainer.getChildren().add(
                DialogBox.getLukeDialog("Hello! I'm SimBot\nWhat can I do for you?", lukeImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Luke's reply,
     * then appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = luke.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLukeDialog(response, lukeImage)
        );
        userInput.clear();

        if (luke.shouldExit()) {
            // Show the goodbye response for a moment before exiting
            new Thread(() -> {
                try { Thread.sleep(250); } catch (InterruptedException ignored) {}
                Platform.runLater(Platform::exit);
            }).start();
        }
    }
}
