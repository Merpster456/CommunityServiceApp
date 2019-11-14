package AdminInterface;

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
    private StringProperty email;
    private StringProperty firstName;
    private StringProperty lastName;
    private BooleanProperty isDeleted;

    public void setId(String id) {

        this.id = new SimpleStringProperty(id);
    }
    public String getId(){

        return id.get();
    }
    public void setGrade(String grade){

        this.grade = new SimpleStringProperty(grade);
    }
    public String getGrade(){

        return grade.get();
    }
    public void setEmail(String email){

        this.email = new SimpleStringProperty(email);
    }
    public String getEmail(){

        return email.get();
    }
    public void setFirst(String first){

        this.firstName = new SimpleStringProperty(first);
    }
    public String getFirst(){

        return firstName.get();
    }
    public void setLast(String last){

        this.lastName = new SimpleStringProperty(last);
    }
    public String getLast(){

        return lastName.get();
    }
    public void setIsDeleted(Boolean isDeleted){

        this.isDeleted = new SimpleBooleanProperty(isDeleted);
    }
    public Boolean getIsDeleted(){

        return isDeleted.get();
    }


    public Student(String id, String grade, String email, String first, String last, Boolean isDeleted){

        this.id = new SimpleStringProperty(id);
        this.grade = new SimpleStringProperty(grade);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(first);
        this.lastName = new SimpleStringProperty(last);
        this.isDeleted = new SimpleBooleanProperty(isDeleted);
    }

    public static String GenerateID(String first, String last) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Random rand = new Random();

        while (true) {

            int n = rand.nextInt(8);
            n = n + 1;
            int n2 = rand.nextInt(8);
            n2 = n2 + 1;
            int n3 = rand.nextInt(8);
            n3 = n3 + 1;

            String ID = first + "." + last + n + n2 + n3;

            try {

                connection = DataConnect.getConnection();
                assert connection != null;
                statement = connection.createStatement();

                try {

                    String sql = "Select * FROM Persons WHERE id='" + ID + "';";

                    rs = statement.executeQuery(sql);

                } catch (SQLException e) {

                    return ID;
                }
            } catch (SQLException e) {

                e.printStackTrace();
            } finally {

                DataUtil.close(rs);
                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        }
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
}
