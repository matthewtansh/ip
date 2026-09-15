package ollie;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controls Ollie's main chat window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Ollie ollie;

    @FXML
    private void initialize() {
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) ->
                scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the chatbot that processes commands for this window.
     *
     * @param ollie Chatbot used to process commands.
     */
    public void setOllie(Ollie ollie) {
        this.ollie = ollie;
        dialogContainer.getChildren().add(
                DialogBox.getOllieDialog("Hello! I'm Ollie. Type help to see what I can do."));
        userInput.requestFocus();
    }

    /**
     * Sends the current input to Ollie and displays both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = ollie.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getOllieDialog(response));
        userInput.clear();

        if (ollie.isExitCommand(input)) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
