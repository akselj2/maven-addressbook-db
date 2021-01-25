import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Model
 *
 * @author Aksel Jessen
 * @version 09/01/2021
 */

public class Model {

    //variables for AddController.java

    private StringProperty name = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private IntegerProperty plz = new SimpleIntegerProperty();

    private ObservableList<String> names = new SimpleListProperty<>();

    //variables for ViewController.java

    private IntegerProperty selectedIndex = new SimpleIntegerProperty();

    /*
    method gets all variables after having been changed from property to normal datatype
    then calls the insert method with the params in a try catch
     */

    public void insertFromView(){

        String name = getName();
        String street = getStreet();
        int plz = getPlz();

        try {
            insert(name, street, plz);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * insert method
     *
     * bindings sql style.
     *
     * @param name      String variable for name in address
     * @param street    String variable for street in address
     * @param plz       integer for zip code in address
     */

    public void insert(String name, String street, int plz){

        String sql = "INSERT INTO addresses(name,street,plz) VALUES(?,?,?)";

        try (Connection myConn = this.connect();
             PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, street);
            pstmt.setInt(3, plz);
            pstmt.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void edit(String selectedName, String name, String street, int plz){
        String sql = "UPDATE addresses SET name = ?, street = ?, plz = ? WHERE name = ?";

        try (Connection myConn = this.connect();
             PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, street);
            pstmt.setInt(3, plz);
            pstmt.setString(4, selectedName);
            pstmt.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    // connects to database with username and password

    public Connection connect() {

        Connection myConn = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/addressbook?user=root&password=aksel");
        } catch (SQLException e){
            e.printStackTrace();
        }

        return myConn;
    }

    // getters and setters

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getPlz() {
        return plz.get();
    }

    public IntegerProperty plzProperty() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz.set(plz);
    }
}
