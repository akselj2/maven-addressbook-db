import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

/**
 * Model
 *
 * @author Aksel Jessen
 * @version 09/01/2021
 *
 * I attempted to seperate Models so that each Scene had its own. JavaFX doesn't like that. Next thing i tried is to centralize everything. 1 Model, 1 Controller for all Views.
 * Didn't work either. So, i went with 1 Model for all Views but each View has it's own Controller.
 * That works. I love myself.
 */

public class Model {

    //variables for AddController.java
    public static int workload = 12;

    private StringProperty noteContent = new SimpleStringProperty();

    private StringProperty noteTitle = new SimpleStringProperty();
    private StringProperty firstname = new SimpleStringProperty();
    private StringProperty lastname = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private ObservableList<String> names = new SimpleListProperty<>();

    //variables for ViewController.java

    private IntegerProperty selectedIndex = new SimpleIntegerProperty();

    /*
    method gets all variables after having been changed from property to normal datatype
    then calls the insert method with the params in a try catch
     */


    public void register() {
        String firstname = getFirstname();
        String lastname = getLastname();
        String email = getEmail();
        String password = getPassword();

        String username = new StringBuilder().append(firstname).append(".").append(lastname).toString();

        String salt = BCrypt.gensalt(workload);

        String hashedPassword = BCrypt.hashpw(password, salt);

        try {
            insertRegister(username, firstname, lastname, email, hashedPassword);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertRegister(String username, String firstname, String lastname, String email, String password) {
        String sql = "INSERT INTO users(username, firstname, lastname, email, password) VALUES(?,?,?,?,?)";

        try (Connection myConn = this.connect();
        PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, firstname);
            pstmt.setString(3, lastname);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNote() {

        String title = getNoteTitle();
        String content = getNoteContent();

        try {
            insert(title, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUser(String email, String password) {
        boolean is_user_verified = false;

        String email2 = email;
        String password2 = password;
        String sql = "SELECT password FROM users WHERE (email = ?)";
        String hash = new String();

        try (Connection myConn = this.connect();
        PreparedStatement pstmt = myConn.prepareStatement(sql)){

            pstmt.setString(1, email2);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                hash = rs.getString("password");
            }

            if( verifyPassword(password2, hash) ) {
                is_user_verified = true;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return is_user_verified;
    }

    public static boolean verifyPassword(String password, String hash) {
        boolean password_verified = false;

        if(null == hash || !hash.startsWith("$2a$")) throw new java.lang.IllegalArgumentException("Invalid hash provided for verification");

        password_verified = BCrypt.checkpw(password, hash);

        return password_verified;
    }

    /**
     *
     * insert method
     *
     * bindings sql style.
     *
     * @param title    String variable consisting of the firstname and lastname. unique?
     * @param content   String variable for firstnames
     */

    public void insert(String title, String content){

        String sql = "INSERT INTO notes(title, content) VALUES(?,?)";

        try (Connection myConn = this.connect();
             PreparedStatement pstmt = myConn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*public void edit(String selectedName, String name, String street, int plz){
        String sql = "UPDATE users SET name = ?, street = ?, plz = ? WHERE name = ?";

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
    }*/

    // connects to database with username and password

    public Connection connect() {

        Connection myConn = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/probeIPA?user=root&password=aksel");
        } catch (SQLException e){
            e.printStackTrace();
        }

        return myConn;
    }

    // getters and setters


    public String getFirstname() {
        return firstname.get();
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getNoteContent() {
        return noteContent.get();
    }

    public StringProperty noteContentProperty() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent.set(noteContent);
    }

    public String getNoteTitle() {
        return noteTitle.get();
    }

    public StringProperty noteTitleProperty() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle.set(noteTitle);
    }
}
