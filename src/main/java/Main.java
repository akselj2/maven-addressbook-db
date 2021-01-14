import javafx.application.Application;
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
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
            HBox root = myLoader.load();

            Model myModel = new Model();

            Controller controller = myLoader.getController();
            controller.setModel(myModel);

            Scene scene = new Scene(root);
            primaryStage.setTitle("AddressBook");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
