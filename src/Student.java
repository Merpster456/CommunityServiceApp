import javafx.beans.property.*;

import java.sql.SQLException;
import java.util.Random;

import Database.DataConnect;
import Database.DataUtil;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class Student {

    private StringProperty id;
    private StringProperty grade;
    private StringProperty emailOrDate;
    private StringProperty firstName;
    private StringProperty lastName;
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
    public void setEmailOrDate(String emailOrDate){

        this.emailOrDate = new SimpleStringProperty(emailOrDate);
    }
    public String getEmailOrDate(){

        return emailOrDate.get();
    }
    public StringProperty emailOrDateProperty() {

        return emailOrDate;
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
    public void setHours(String numHours){

        this.hours = new SimpleStringProperty(numHours);
    }
    public String getHours(){

        return hours.get();
    }
    public StringProperty hoursProperty() {

        return hours;
    }

    public Student(String id, String grade, String email, String first, String last){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.emailOrDate = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
    }

    public Student(String id, String grade, String emailOrDate, String first, String last, String hours){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.emailOrDate = new SimpleStringProperty(emailOrDate);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
        this.hours = new SimpleStringProperty(hours);

    }
/*
    public Student(String id, String grade, String first, String last, String hours, String date){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
        this.hours = new SimpleStringProperty(hours);
        this.date = new SimpleStringProperty(date);
    }
 */

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
