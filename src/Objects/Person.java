package Objects;

import javafx.beans.property.*;

import java.sql.SQLException;
import java.util.Random;

import Database.DataConnect;
import Database.DataUtil;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class Person {

    private StringProperty id;
    private StringProperty grade;
    private StringProperty email;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty pass;
    private StringProperty isAdmin;
    private StringProperty hours;

    public void setId(String id) {

        this.id = new SimpleStringProperty(id);
    }
    public String getId(){

        return id.get();
    }
    public StringProperty idProperty() {

        return id;
    }
    public void setGrade(String grade){

        this.grade = new SimpleStringProperty(grade);
    }
    public String getGrade(){

        return grade.get();
    }
    public StringProperty gradeProperty() {

        return grade;
    }
    public void setEmail(String email){

        this.email = new SimpleStringProperty(email);
    }
    public String getEmail(){

        return email.get();
    }
    public StringProperty emailProperty() {

        return email;
    }
    public void setFirst(String first){

        this.firstName = new SimpleStringProperty(first);
    }
    public String getFirst(){

        return firstName.get();
    }
    public StringProperty firstNameProperty() {

        return firstName;
    }
    public void setLast(String last){

        this.lastName = new SimpleStringProperty(last);
    }
    public String getLast(){

        return lastName.get();
    }
    public StringProperty lastNameProperty() {

        return lastName;
    }
    public void setIsAdmin(String isAdmin){

        this.isAdmin = new SimpleStringProperty(isAdmin);
    }
    public String getIsAdmin(){

        return isAdmin.get();
    }
    public StringProperty isAdminProperty() {

        return isAdmin;
    }
    public void setPass(String pass){

        this.pass = new SimpleStringProperty(pass);
    }
    public String getPass(){

        return pass.get();
    }
    public StringProperty passProperty() {

        return pass;
    }
    public void setHours(String hours){

        this.hours = new SimpleStringProperty(hours);
    }
    public String getHours(){

        return hours .get();
    }
    public StringProperty hoursProperty() {

        return hours;
    }

    /**
    public Person(String id, String grade, String email, String first, String last, String pass, String isAdmin){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
        this.pass = new SimpleStringProperty(pass);

        if (isAdmin.equals("1")) this.isAdmin = new SimpleStringProperty("True");
        else this.isAdmin = new SimpleStringProperty("False");
    }
    */
    public Person(String id, String grade, String email, String first, String last, String pass, String isAdmin, String hours){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
        this.pass = new SimpleStringProperty(pass);
        this.hours = new SimpleStringProperty(hours);

        if (isAdmin.equals("1")) this.isAdmin = new SimpleStringProperty("True");
        else this.isAdmin = new SimpleStringProperty("False");
    }


    public static String GenerateID(String First, String Last) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Random rand = new Random();
        Boolean control = true;
        String result = null;

        String first = First.toLowerCase();
        String last = Last.toLowerCase();

        int n = rand.nextInt(8);
        n = n + 1;
        int n2 = rand.nextInt(8);
        n2 = n2 + 1;
        int n3 = rand.nextInt(8);
        n3 = n3 + 1;

        String ID = first + "." + last + n + n2 + n3;

        if (check(ID)) return ID;
        else return GenerateID(first, last);
    }

    public static String GeneratePass(){

        Random rand = new Random();

        int n = rand.nextInt(8);
        n = n + 1;
        int n2 = rand.nextInt(8);
        n2 = n2 + 1;
        int n3 = rand.nextInt(8);
        n3 = n3 + 1;
        int n4 = rand.nextInt(8);
        n4 = n4 +1;

        String pass = "0000" + n + n2 + n3 + n4;

        return pass;
    }

    private static Boolean check(String ID) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        String result = null;
        int test = 0;

        try {

            connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();

            String sql = "Select * FROM Persons WHERE id='" + ID + "';";

            rs = statement.executeQuery(sql);

            result = rs.getString(1);

            return false;

        } catch (SQLException e) {

            return true;
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }
}
