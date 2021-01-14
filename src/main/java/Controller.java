import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Controller {
    Model myModel;

    @FXML
    Label labelMessage;

    @FXML
    TextField textFieldName;

    @FXML
    TextField textFieldStreet;

    @FXML
    TextField textFieldPlz;

    @FXML
    Button backButton;

    @FXML
    ObservableList<String> names = new ObservableList<String>() {
        @Override
        public void addListener(ListChangeListener<? super String> listChangeListener) {

        }

        @Override
        public void removeListener(ListChangeListener<? super String> listChangeListener) {

        }

        @Override
        public boolean addAll(String... strings) {
            return false;
        }

        @Override
        public boolean setAll(String... strings) {
            return false;
        }

        @Override
        public boolean setAll(Collection<? extends String> collection) {
            return false;
        }

        @Override
        public boolean removeAll(String... strings) {
            return false;
        }

        @Override
        public boolean retainAll(String... strings) {
            return false;
        }

        @Override
        public void remove(int i, int i1) {

        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<String> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(String s) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends String> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public String get(int index) {
            return null;
        }

        @Override
        public String set(int index, String element) {
            return null;
        }

        @Override
        public void add(int index, String element) {

        }

        @Override
        public String remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<String> listIterator() {
            return null;
        }

        @Override
        public ListIterator<String> listIterator(int index) {
            return null;
        }

        @Override
        public List<String> subList(int fromIndex, int toIndex) {
            return null;
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {

        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {

        }
    };

    @FXML
    ListView<String> databaseListView = new ListView<>();

    // binds textfield text properties to the different properties in Model class

    public void setModel(Model model) {
        myModel = model;

        textFieldName.textProperty().bindBidirectional(model.nameProperty());
        textFieldStreet.textProperty().bindBidirectional(model.streetProperty());
        textFieldPlz.textProperty().bindBidirectional(model.plzProperty(), new NumberStringConverter());

        databaseListView.setItems(names);

        showItems();

    }

    // sets all fields to blank

    public void clear(ActionEvent event) {
        textFieldName.setText("");
        textFieldStreet.setText("");
        textFieldPlz.setText("");
    }

    // once button is clicked, method calls insertFromView() from Model class and sets Text for a Label to "Check Db" and makes it Green.

    public void add(ActionEvent event) {
        myModel.insertFromView();

        showItems();
    }

    public void edit() {
        delete(databaseListView.getSelectionModel().getSelectedItem());

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
            names.removeAll();
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
