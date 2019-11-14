package AdminInterface.SubWindows;

import AdminInterface.Student;
import AdminInterface.Student.*;
import Database.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Database.DataConnect;

public class ManageUsers implements Initializable {

    @FXML private Button Back;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> firstCol;
    @FXML private TableColumn<Student, String> lastCol;
    @FXML private TableColumn<Student, String> gradeCol;
    @FXML private TableColumn<Student, String> emailCol;
    @FXML private TextField newFirst;
    @FXML private TextField newLast;
    @FXML private TextField newGrade;
    @FXML private TextField newEmail;
    @FXML private Label firstErr;
    @FXML private Label lastErr;
    @FXML private Label gradeErr;
    @FXML private Label emailErr;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    private Connection connection;
    private Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable();

    }

    private void setTable(){

        String SQL = "SELECT * FROM Persons WHERE isAdmin = false;";
        ResultSet rs = null;
        List<Student> list = new ArrayList<Student>();
        Student student = null;




        try {

            Connection connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                students.add(new Student(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getBoolean(8)));

            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("First"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Last"));
            this.gradeCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Grade"));
            this.emailCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Email"));

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());
            emailCol.setCellFactory(TextFieldTableCell.forTableColumn());

            studentTable.setItems(null);
            studentTable.setItems(students);

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdminInterface/AdminUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);


    }

    @FXML
    protected void submit(ActionEvent event) throws IOException {

        Boolean control = true;

        String first = (String) this.newFirst.getText();
        String last = (String) this.newLast.getText();
        String grade = (String) this.newGrade.getText();
        String email = (String) this.newEmail.getText();

        if (first == null) {

            firstErr.setText("First Name is Required!");
            control = false;
        }
        if (last == null) {

            lastErr.setText("Last Name is Required!");
            control = false;
        }
        if (grade == null) {

            gradeErr.setText("Grade is Required!");
            control = false;
        }
        if (email == null) {

            emailErr.setText("Email is Required!");
            control = false;
        }
        if (control == true) {

            try {

                ResultSet rs = null;
                String id = Student.GenerateID(first, last);
                String pass = Student.GeneratePass();

                String sql = "INSERT INTO Persons (id, grade, email, first, last, password, isAdmin, isDeleted) " +
                        "VALUES ('" + id + "', " + grade + ", '" + email + "', '" + first + "', '" + last + "', '" +
                        pass + "', false, false);";

                try {

                    connection = DataConnect.getConnection();
                    assert connection != null;
                    statement = connection.createStatement();

                    rs = statement.executeQuery(sql);


                } catch (SQLException e) {



                }
            } catch (Exception e){
                System.err.println("Error: " + e);
            }
        }
    }
}
