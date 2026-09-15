package ollie;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Displays Ollie's JavaFX graphical interface.
 */
public class Main extends Application {
    private static final double WINDOW_HEIGHT = 640.0;
    private static final double WINDOW_WIDTH = 520.0;

    private final Ollie ollie = Ollie.createDefault();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        BorderPane root = fxmlLoader.load();
        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setOllie(ollie);

        stage.setScene(new Scene(root));
        stage.setTitle("Ollie");
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.show();
    }
}
