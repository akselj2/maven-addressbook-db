import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    Model myModel;

    @FXML
    TextField textFieldEmail;

    @FXML
    PasswordField passwordField;

    Stage secondaryStage;

    public void setModel(Model model) {
        myModel = model;

        textFieldEmail.textProperty().bindBidirectional(model.emailProperty());
        passwordField.textProperty().bindBidirectional(model.passwordProperty());

    }

    public void login() throws IOException {
        //checks first if the input fields are empty or not
        if (checkInputFields()) {
            //proceeds to verify the user & load the next stage once verified
            if (myModel.verifyUser(textFieldEmail.getText(), passwordField.getText())) {
                Stage stage;

                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
                HBox root = myLoader.load();

                Controller controller = myLoader.getController();
                controller.setModel(myModel);

                stage = (Stage) textFieldEmail.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Login error");
                a.setContentText("Please check if you spelt either your email or password correctly.");
            }
        } else {
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Login warning");
            a.setContentText("You didn't enter anything.");
        }

    }


    public boolean checkInputFields() {
        return (!textFieldEmail.getText().isEmpty()
                && !passwordField.getText().isEmpty());
    }

    public void register() throws IOException {
        Stage stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterView.fxml"));
        HBox root = loader.load();

        RegisterController controller = loader.getController();
        controller.setModel(myModel);

        stage = (Stage) textFieldEmail.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }



}
