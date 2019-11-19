package AdminInterface;

import Student;
import Database.DataUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
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

public class AdminController implements Initializable {

    @FXML private ChoiceBox choiceBox;
    @FXML private ChoiceBox changeBox;
    @FXML private TableView<Person> personTable;
    @FXML private TableView<Person> delTable;
    @FXML private TableColumn<Person, String> idCol;
    @FXML private TableColumn<Person, String> firstCol;
    @FXML private TableColumn<Person, String> lastCol;
    @FXML private TableColumn<Person, String> gradeCol;
    @FXML private TableColumn<Person, String> emailCol;
    @FXML private TableColumn<Person, String> isAdvisorCol;
    @FXML private TableColumn<Person, String> delID;
    @FXML private TableColumn<Person, String> delGrade;
    @FXML private TableColumn<Person, String> delEmail;
    @FXML private TableColumn<Person, String> delFirst;
    @FXML private TableColumn<Person, String> delLast;
    @FXML private TableColumn<Person, String> delIsAdvisor;
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

    public AdminController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable();
        setDelTable();

        setBox();

    }

    private void setDelTable(){

        String SQL = "SELECT * FROM Deleted";
        ResultSet rs = null;
        List<Person> list = new ArrayList<Person>();
        Person person = null;
        ObservableList<Person> persons = FXCollections.observableArrayList();

        try {

            Connection connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                persons.add(new Person(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(7)));
            }

            this.delID.setCellValueFactory(new PropertyValueFactory<Person, String>("Id"));
            this.delFirst.setCellValueFactory(new PropertyValueFactory<Person, String>("First"));
            this.delLast.setCellValueFactory(new PropertyValueFactory<Person, String>("Last"));
            this.delGrade.setCellValueFactory(new PropertyValueFactory<Person, String>("Grade"));
            this.delEmail.setCellValueFactory(new PropertyValueFactory<Person, String>("Email"));
            this.delIsAdvisor.setCellValueFactory(new PropertyValueFactory<Person, String>( "IsAdmin"));

            delID.setCellFactory(TextFieldTableCell.forTableColumn());
            delFirst.setCellFactory(TextFieldTableCell.forTableColumn());
            delLast.setCellFactory(TextFieldTableCell.forTableColumn());
            delGrade.setCellFactory(TextFieldTableCell.forTableColumn());
            delEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            delIsAdvisor.setCellFactory(TextFieldTableCell.forTableColumn());

            delTable.setItems(null);
            delTable.setItems(persons);

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

    }

    private void setTable(){

        String SQL = "SELECT * FROM Persons";
        ResultSet rs = null;
        List<Person> list = new ArrayList<Person>();
        Person person = null;
        ObservableList<Person> persons = FXCollections.observableArrayList();

        try {

            Connection connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                persons.add(new Person(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(7)));
            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Person, String>("First"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Last"));
            this.gradeCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Grade"));
            this.emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Email"));
            this.isAdvisorCol.setCellValueFactory(new PropertyValueFactory<Person, String>( "IsAdmin"));

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());
            emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
            isAdvisorCol.setCellFactory(TextFieldTableCell.forTableColumn());

            personTable.setItems(null);
            personTable.setItems(persons);

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

        String sql = "SELECT id FROM Persons;";
        ResultSet rs = null;

        try{

            choiceBox.getItems().clear();
            changeBox.getItems().clear();

            connection = DataConnect.getConnection();
            assert connection != null;
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
    @FXML
    protected void Change(ActionEvent event) {


    }
    @FXML
    protected void Autofill(ActionEvent event) {

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
