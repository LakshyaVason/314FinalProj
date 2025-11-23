import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FxCheck extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Label("JavaFX is working!"), 300,
                150));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}