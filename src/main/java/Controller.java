import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import javax.xml.transform.Result;
import java.sql.*;

public class Controller {
    Model myModel;

    @FXML
    TextField textFieldTitle;

    @FXML
    TextField textFieldContent;
    
    @FXML
    ObservableList<String> noteTitles = FXCollections.observableArrayList();

    String noteContent = new String();

    @FXML
    ListView<String> databaseListView = new ListView<>();

    // binds textfield text properties to the different properties in Model class

    public void setModel(Model model) {
        myModel = model;

        textFieldTitle.textProperty().bindBidirectional(model.noteTitleProperty());
        textFieldContent.textProperty().bindBidirectional(model.noteContentProperty());
        showItems();

        databaseListView.setItems(noteTitles);
    }

    // sets all fields to blank

    public void clear() {
        textFieldTitle.setText("");
        textFieldContent.setText("");
    }

    // once button is clicked, method calls insertFromView() from Model class and sets Text for a Label to "Check Db" and makes it Green.

    public void add() {
        if (checkInputFields()){
            myModel.insertNote();
            showItems();
        }
    }

    // checks if all textFields are empty. if false, it updates all items.

    /*public void edit() {
        if (checkInputFields()){
            String name = textFieldName.getText();
            String street = textFieldStreet.getText();
            int plz = Integer.parseInt(textFieldPlz.getText());
            String selectedName = databaseListView.getSelectionModel().getSelectedItem();


             * selectedName is 1st
             * name is second
             * street is third
             * plz is fourth.


            myModel.edit(selectedName, name, street, plz);

            showItems();
        }
    }*/

    public boolean checkInputFields() {
        return (!textFieldTitle.getText().isEmpty()
                && !textFieldContent.getText().isEmpty());
    }

    /*public void delete() {
        delete(databaseListView.getSelectionModel().getSelectedItem());
    }*/

    /*public void delete(String name) {

        String sql = "DELETE FROM notes WHERE name='" + name + "'";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement();

            stmt.executeQuery(sql);
            names.remove(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public void showNote() {
        textFieldContent.setText(retrieveContent(databaseListView.getSelectionModel().getSelectedIndex()+1));
        textFieldTitle.setText(retrieveTitle(databaseListView.getSelectionModel().getSelectedIndex()+1));
    }

    public String retrieveContent(int id) {
        String content;

        String sql = "SELECT content FROM notes WHERE (noteID=?)";

        try (Connection myConn = connect();
             PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                content = rs.getString("content");
                return content;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Couldn't retrieve content. Check whether which index you've selected.";
    }

    public String retrieveTitle(int id) {
        String title;

        String sql = "SELECT title FROM notes WHERE (noteID=?)";

        try (Connection myConn = connect();
             PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                title = rs.getString("title");
                return title;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Couldn't retrieve title. Check which index you have selected.";
    }

    public void showItems() {
        String sql = "SELECT title FROM notes";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(sql);

            noteTitles.clear();

            while (rs.next()) {
                String title = rs.getString("title");
                noteTitles.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() {
        Connection myConn = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/probeIPA?user=root&password=aksel");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return myConn;
    }
}