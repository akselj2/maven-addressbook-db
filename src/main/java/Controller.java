import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import javax.swing.*;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

public class Controller {
    Model myModel;

    @FXML
    TextField textFieldTitle;

    @FXML
    TextArea textAreaContent;

    ObservableList<Note> notes = FXCollections.observableArrayList();

    @FXML
    ListView<String> databaseListView = new ListView<>();

    // binds textfield text properties to the different properties in Model class

    public void setModel(Model model) {
        myModel = model;

        textFieldTitle.textProperty().bindBidirectional(model.noteTitleProperty());
        textAreaContent.textProperty().bindBidirectional(model.noteContentProperty());
        showItems();

        List<String> titlesList = notes.stream().map(Note -> Note.getTitle()).collect(Collectors.toList());

        ObservableList<String> titles = FXCollections.observableArrayList(titlesList);

        databaseListView.setItems(titles);
    }

    // sets all fields to blank

    public void clear() {
        textFieldTitle.setText("");
        textAreaContent.setText("");
    }

    // once button is clicked, method calls insertFromView() from Model class and sets Text for a Label to "Check Db" and makes it Green.

    public void add() {
        if (checkInputFields()) {
            UUID guid = randomUUID();
            System.out.println(guid);
            notes.add(new Note(guid, textFieldTitle.getText(), textAreaContent.getText()));
            myModel.insertNote(guid);
            showItems();
        }
    }

    // checks if all textFields are empty. if false, it updates all items.

    public void edit() {
        if (checkInputFields()) {
            String title = textFieldTitle.getText();
            String content = textAreaContent.getText();
            int id = databaseListView.getSelectionModel().getSelectedIndex();

            UUID guid = notes.get(id).getGUID();

            if (verifyUUID(guid)) {
                myModel.edit(title, content, guid);
                showItems();
            }

        }
    }

    public boolean checkInputFields() {
        return (!textFieldTitle.getText().isEmpty()
                && !textAreaContent.getText().isEmpty());
    }

    public boolean verifyUUID(UUID guid) {
        boolean is_uuid_verified = false;
        UUID uuid;
        String sql = "SELECT guid FROM notes WHERE guid='" + guid + "'";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            String guidString = rs.getString("guid");
            uuid = UUID.fromString(guidString);

            if (uuid.equals(guid)) {
                is_uuid_verified = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return is_uuid_verified;
    }

    /*public void delete() {
        int id = databaseListView.getSelectionModel().getSelectedIndex();



        for(Note note : notes) {
            if(note.getGUID().equals(guid)) {
                delete(note.getGUID());
            }
        }
    }*/

    public void delete(UUID guid) {

        String sql = "DELETE FROM notes WHERE guid='" + guid + "'";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement();

            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showNote() {
        textAreaContent.setText(notes.get(databaseListView.getSelectionModel().getSelectedIndex()).getContent());
        textFieldTitle.setText(notes.get(databaseListView.getSelectionModel().getSelectedIndex()).getTitle());
    }

    public void showItems() {
        String sql = "SELECT * FROM notes";

        Connection myConn = connect();

        try {
            Statement stmt = myConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(sql);

            notes.clear();

            while (rs.next()) {
                String uuidString = rs.getString("guid");
                UUID uuid = UUID.fromString(uuidString);

                String content = rs.getString("content");
                String title = rs.getString("title");

                notes.add(new Note(uuid, title, content));
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