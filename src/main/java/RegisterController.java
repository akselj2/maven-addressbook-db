import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterController {
    Model myModel;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldLastName;

    @FXML
    TextField textFieldEmail;

    @FXML
    PasswordField passwordField;

    public void setModel(Model model){
        myModel = model;

        textFieldName.textProperty().bindBidirectional(model.firstnameProperty());
        textFieldLastName.textProperty().bindBidirectional(model.lastnameProperty());
        textFieldEmail.textProperty().bindBidirectional(model.emailProperty());
        passwordField.textProperty().bindBidirectional(model.passwordProperty());


    }



    public void login() throws IOException {
        Stage stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        HBox root = loader.load();

        LoginController controller = loader.getController();
        controller.setModel(myModel);

        stage = (Stage) textFieldEmail.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public boolean regexMyAss(String firstname, String lastname, String email, String password) {

        Pattern patternName = Pattern.compile("[A-Za-z]+", Pattern.CASE_INSENSITIVE);
        Pattern patternEmail = Pattern.compile("[a-zA-Z]+\\.[a-zA-Z]+@cognizant\\.com", Pattern.CASE_INSENSITIVE);
        Pattern patternPassword = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", Pattern.CASE_INSENSITIVE);

        Matcher matchName = patternName.matcher(new StringBuilder().append(firstname).append(" ").append(lastname).toString());
        Matcher matchEmail = patternEmail.matcher(email);
        Matcher matchPassword = patternPassword.matcher(password);

        boolean matchFoundName = matchName.find();
        boolean matchFoundEmail = matchEmail.find();
        boolean matchFoundPassword = matchPassword.find();

        if (matchFoundName && matchFoundEmail && matchFoundPassword) {
            return true;
        } else {
            System.out.println("u done fucked up <3");
            return false;

        }
    }

    public boolean checkInputFields() {
        String firstname = textFieldName.getText();
        String lastname = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String password =passwordField.getText();

        if (regexMyAss(firstname, lastname, email, password)
                && !textFieldName.getText().isEmpty()
                && !textFieldLastName.getText().isEmpty()
                && !textFieldEmail.getText().isEmpty()
                && !passwordField.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public void register() {
        if (checkInputFields()) {
            myModel.register();
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
