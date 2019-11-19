package AdvisorInterface.SubWindows.ManageUsers;

import Student;
import Database.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
import javafx.stage.StageStyle;

public class ManageUsers implements Initializable {

    @FXML private Button Back;
    @FXML private ChoiceBox choiceBox;
    @FXML private ChoiceBox changeBox;
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
    @FXML private TextField changeFirst;
    @FXML private TextField changeLast;
    @FXML private TextField changeGrade;
    @FXML private TextField changeEmail;
    @FXML private Label firstErr;
    @FXML private Label lastErr;
    @FXML private Label gradeErr;
    @FXML private Label emailErr;
    @FXML private Label err;
    @FXML private Label delErr;
    @FXML private Label firstCErr;
    @FXML private Label lastCErr;
    @FXML private Label gradeCErr;
    @FXML private Label emailCErr;
    @FXML private Label changeErr;

    private Connection connection;
    private Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable();

        setBox();

    }

    private void setTable(){

        String SQL = "SELECT * FROM Persons WHERE isAdmin = false;";
        ResultSet rs = null;
        List<Student> list = new ArrayList<Student>();
        Student student = null;
        ObservableList<Student> students = FXCollections.observableArrayList();

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
                        rs.getString(5)));
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

            setBox();
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
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);


    }

    @FXML
    protected void submit(ActionEvent event) throws IOException {

        ResultSet rs = null;
        boolean control = true;

        String first = (String) this.newFirst.getText();
        String last = (String) this.newLast.getText();
        String grade = (String) this.newGrade.getText();
        String email = (String) this.newEmail.getText();

        try {

            int numCheck = Integer.parseInt(grade);
        } catch (NumberFormatException e) {

            gradeErr.setText("Need a Numerical Value!");
            control = false;

            err.setText("Change Needed Fields!");
        }

        if (first.length() < 1) {

            firstErr.setText("First Name is Required!");
            control = false;

            err.setText("Need to Fill Required Field!");
        }
        if (last.length() < 1) {

            lastErr.setText("Last Name is Required!");
            control = false;

            err.setText("Need to Fill Required Field!");
        }
        if (grade.length() < 1) {

            gradeErr.setText("Grade is Required!");
            control = false;

            err.setText("Need to Fill Required Field!");
        }
        if (email.length() < 1) {

            emailErr.setText("Email is Required!");
            control = false;

            err.setText("Need to Fill Required Field!");
        }
        if (control) {

            try {

                String id = Student.GenerateID(first, last);
                String pass = Student.GeneratePass();

                String sql = "INSERT INTO Persons VALUES ('" + id + "', " + grade + ", '" +
                        email + "', '" + first + "', '" + last + "', '" + pass + "', false);";
                try {

                    connection = DataConnect.getConnection();
                    assert connection != null;
                    statement = connection.createStatement();

                    statement.executeQuery(sql);

                } catch (SQLException e) {

                    setTable();
                    showCreds(id, pass);
                }
            } catch (Exception e){

                System.err.println("Error: " + e);

                err.setText("Error!");
            } finally {

                DataUtil.close(rs);
                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        }
    }
    @FXML
    protected void cancel(ActionEvent event) throws IOException{

        newFirst.setText("");
        newLast.setText("");
        newGrade.setText("");
        newEmail.setText("");
    }
    private void showCreds(String id, String pass) {

        Stage newStage = new Stage();
        StackPane pane = new StackPane();

        GridPane gridPane = new GridPane();
        pane.getChildren().add(gridPane);

        Label idLabel = new Label();
        Label passLabel = new Label();
        Button quit = new Button("Quit");

        quit.setOnAction(event -> newStage.close());

        idLabel.setText("Users ID: " + id);
        passLabel.setText("Users Password: " + pass);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(50,50,50,50));
        gridPane.setVgap(10.0);
        gridPane.add(idLabel, 0, 0);
        gridPane.add(passLabel, 0, 1);
        gridPane.add(quit, 0, 3);

        Scene scene = new Scene(pane, 500, 300);

        newStage.initStyle(StageStyle.UTILITY);
        newStage.setScene(scene);
        newStage.show();
    }

    private void setBox(){

        String sql = "SELECT id FROM Persons WHERE isAdmin=false;";
        ResultSet rs = null;

        try{

            choiceBox.getItems().clear();
            changeBox.getItems().clear();

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while(rs.next()) {

                choiceBox.getItems().add(rs.getString(1));
                changeBox.getItems().add(rs.getString(1));
            }
        } catch (SQLException e){

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    /**
     * Method to delete users from Persons table
     * @param event
     * @throws IOException
     */
    @FXML
    protected void Delete(ActionEvent event) throws IOException{

        String id = (String) choiceBox.getValue();
        String sql = "SELECT * FROM Persons WHERE id='" + id + "';";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            int grade = rs.getInt(2);
            String email = rs.getString(3);
            String first = rs.getString(4);
            String last = rs.getString(5);
            String pass = rs.getString(6);

            DataUtil.close(rs);

            sql = "INSERT INTO Deleted VALUES ('" + id + "', '" + grade + "', '" + email + "', '" +
                    first + "', '" + last + "', '" + pass + "', false);";

            try{
                statement.executeQuery(sql);
            } catch (SQLException e) {}

            sql = "SELECT * FROM Deleted WHERE id='" + id + "';";

            rs = statement.executeQuery(sql);

            if (rs.getString(1).equals(id)) {

                DataUtil.close(rs);

                sql = "DELETE FROM Persons WHERE id='" + id + "';";

                try {
                    statement.executeQuery(sql);
                } catch (SQLException e){}

                setTable();
            } else {

                delErr.setText("Error Deleting User!");
            }

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    /**
     * Method to change user data!
     * @param event
     */
    @FXML
    protected void Change(ActionEvent event) {

        String id = (String) changeBox.getValue();
        String grade = changeGrade.getText();
        String email = changeGrade.getText();
        String first = changeFirst.getText();
        String last = changeLast.getText();

        if (grade.length() > 0) {

            try {

                int numCheck = Integer.parseInt(grade);

                String sql = "UPDATE Persons SET grade = " + grade + " WHERE id = '" + id + "';";

                try {

                    connection = DataConnect.getConnection();
                    statement = connection.createStatement();

                    /**
                     * There will always be an exception thrown
                     * here saying "No result set" however this
                     * is completely fine and expected therefore
                     * we ignore the exception
                     */
                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignore) {}
                } catch (SQLException e) {

                    System.err.println(e.getStackTrace()[0].getLineNumber());
                    System.out.println("Error: " + e);
                }
            } catch (NumberFormatException e) {

                gradeCErr.setText("Need a Numerical Value!");
                changeErr.setText("Change Needed Fields!");
            }
        } if (email.length() > 0) {

            String sql = "UPDATE Persons SET email = '" + email + "' WHERE id = '" + id + "';";

            try {

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                try {
                    statement.executeQuery(sql);
                } catch (SQLException ignore) {}
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            }
        } if (first.length() > 0) {

            String sql = "UPDATE Persons SET first = '" + first + "' WHERE id = '" + id + "';";

            try {

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                try {
                    statement.executeQuery(sql);
                } catch (SQLException ignore) {}
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {

                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        } if (last.length() > 0) {

            String sql = "UPDATE Persons SET last = '" + last + "' WHERE id = '" + id + "';";

            try {

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                try {
                    statement.executeQuery(sql);
                } catch (SQLException ignore) {
                }
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            }
        }
        setTable();
    }

    /**
     * Quality of life auto-fill function
     * @param event
     */
    /*
    @FXML
    protected void Autofill(ActionEvent event) {

        if (changeBox.getValue() != null) {

            String id = changeBox.getValue().toString();

            String sql = "SELECT * FROM Persons WHERE id='" + id + "';";
            ResultSet rs = null;

            try {

                connection = DataConnect.getConnection();
                assert connection != null;
                statement = connection.createStatement();
                rs = statement.executeQuery(sql);

                changeGrade.setText(rs.getString(2));
                changeEmail.setText(rs.getString(3));
                changeFirst.setText(rs.getString(4));
                changeLast.setText(rs.getString(5));

            } catch (SQLException e) {

                System.out.println("Error: " + e);
                System.err.println(e.getStackTrace()[0].getLineNumber());
            }
        }
    }
     */
    @FXML
    protected void changeCancel(ActionEvent event) {

        changeBox.getItems().clear();
        setBox();

        changeFirst.setText("");
        changeLast.setText("");
        changeGrade.setText("");
        changeEmail.setText("");
    }
}
