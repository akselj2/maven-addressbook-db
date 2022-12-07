import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main Class
 *
 * @author Aksel Jessen
 * @version 09/01/2021
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            HBox root = myLoader.load();

            Model myModel = new Model();

            LoginController controller = myLoader.getController();
            controller.setModel(myModel);

            Scene scene = new Scene(root);
            primaryStage.setTitle("PROBEIPA");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
