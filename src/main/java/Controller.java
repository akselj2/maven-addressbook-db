import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.sql.*;

public class Controller {
    Model myModel;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldStreet;

    @FXML
    TextField textFieldPlz;

    @FXML
    ObservableList<String> names = FXCollections.observableArrayList();

    @FXML
    ListView<String> databaseListView = new ListView<>();

    // binds textfield text properties to the different properties in Model class

    public void setModel(Model model) {
        myModel = model;

        textFieldName.textProperty().bindBidirectional(model.nameProperty());
        textFieldStreet.textProperty().bindBidirectional(model.streetProperty());
        textFieldPlz.textProperty().bindBidirectional(model.plzProperty(), new NumberStringConverter());
        showItems();

        databaseListView.setItems(names);
    }

    // sets all fields to blank

    public void clear(ActionEvent event) {
        textFieldName.setText("");
        textFieldStreet.setText("");
        textFieldPlz.setText("");
    }

    // once button is clicked, method calls insertFromView() from Model class and sets Text for a Label to "Check Db" and makes it Green.

    public void add(ActionEvent event) {
        if (checkInputFields()){
            myModel.insertFromView();

            showItems();
        }
    }

    // checks if all textFields are empty. if false, it updates all items.

    public void edit() {
        if (checkInputFields()){
            String name = textFieldName.getText();
            String street = textFieldStreet.getText();
            int plz = Integer.parseInt(textFieldPlz.getText());
            String selectedName = databaseListView.getSelectionModel().getSelectedItem();

            /**
             * selectedName is 1st
             * name is second
             * street is third
             * plz is fourth.
             */

            myModel.edit(selectedName, name, street, plz);

            showItems();
        }
    }

    public boolean checkInputFields() {
        return (!textFieldName.getText().isEmpty()
                && !textFieldStreet.getText().isEmpty()
                && !textFieldPlz.getText().isEmpty());
    }

    public void delete() {
        delete(databaseListView.getSelectionModel().getSelectedItem());
    }

    public void delete(String name) {

        String sql = "DELETE FROM addresses WHERE name='" + name + "'";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement();

            stmt.executeQuery(sql);
            names.remove(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showItems() {
        String sql = "SELECT name FROM addresses";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(sql);

            names.clear();

            while (rs.next()) {
                String name = rs.getString("name");
                names.add(name);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public Connection connect() {
        Connection myConn = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/addressbook?user=root&password=aksel");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return myConn;
    }
}