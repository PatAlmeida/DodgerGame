import javafx.application.Application;
import javafx.stage.Stage;

// Inspired from these pages
// https://bit.ly/1rf8S5n
// https://bit.ly/2tcwZ9L

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        GameWindow window = new GameWindow(stage);
    }

}
