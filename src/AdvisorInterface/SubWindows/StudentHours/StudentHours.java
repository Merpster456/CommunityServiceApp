package AdvisorInterface.SubWindows.StudentHours;

import Database.DataConnect;
import Database.DataUtil;
import Objects.Event;
import Objects.Student;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentHours implements Initializable {

    @FXML private Button Back;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> firstCol;
    @FXML private TableColumn<Student, String> lastCol;
    @FXML private TableColumn<Student, String> gradCol;
    @FXML private TableColumn<Student, String> hoursCol;
    @FXML private TableColumn<Student, String> dateCol;
    @FXML private TextField hoursField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox addChoice;
    @FXML private ChoiceBox specifyChoice;
    @FXML private ChoiceBox delChoice;
    @FXML private ChoiceBox dateChoice;
    @FXML private ObservableList<String> observableList;
    @FXML private ListView<String> acceptList;

    private Connection connection;
    private Statement statement;

    private String id = "";

    public void initialize(URL url, ResourceBundle rs){

        setTable();
        setBox();
        setDelBoxes();
        setLists();

    }

    private void setLists() {



    }

    @FXML
    private void setTable() {

        String SQL = "SELECT * FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY Hours.id;";
        ResultSet rs = null;
        List<Student> list = new ArrayList<Student>();
        Student student = null;
        ObservableList<Student> students = FXCollections.observableArrayList();

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                if (id.length() > 0) {
                    if (rs.getString(1).equals(id));
                    else continue;
                }
                students.add(new Student(rs.getString(1),
                        rs.getString(5),
                        rs.getString(3),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(2)));
            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("First"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Last"));
            this.gradCol.setCellValueFactory(new PropertyValueFactory<Student, String>("GradYear"));
            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Hours"));
            this.dateCol.setCellValueFactory(new PropertyValueFactory<Student, String>("EmailOrDate"));

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradCol.setCellFactory(TextFieldTableCell.forTableColumn());
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

            studentTable.setItems(null);
            studentTable.setItems(students);

            this.dateCol.setVisible(true);

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
    protected void setPTable() {

        String SQL = "SELECT Hours.id, SUM(Hours.hours), Persons.gradYear, Persons.first, Persons.last, Persons.email FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id GROUP BY Hours.id;";
        ResultSet rs = null;
        List<Student> list = new ArrayList<Student>();
        Student student = null;
        ObservableList<Student> students = FXCollections.observableArrayList();

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                if (id.length() > 0) {
                    if (rs.getString(1).equals(id));
                    else continue;
                }

                students.add(new Student(rs.getString(1),
                        rs.getString(3),
                        rs.getString(6),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(2)));
            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("First"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Last"));
            this.gradCol.setCellValueFactory(new PropertyValueFactory<Student, String>("GradYear"));
            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Hours"));
            this.dateCol.setCellValueFactory(null);

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradCol.setCellFactory(TextFieldTableCell.forTableColumn());
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

            studentTable.setItems(null);
            studentTable.setItems(students);

            this.dateCol.setVisible(false);

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setBox(){

        String sql = "SELECT id FROM Persons WHERE isAdmin=false;";
        ResultSet rs = null;

        try{

            addChoice.getItems().clear();
            specifyChoice.getItems().clear();


            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while(rs.next()) {

                addChoice.getItems().add(rs.getString(1));
                specifyChoice.getItems().add(rs.getString(1));
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

    private void setDelBoxes() {

        String sql = "SELECT id FROM Hours WHERE hours IS NOT NULL GROUP BY id;";
        ResultSet rs = null;

        try {

            delChoice.getItems().clear();
            dateChoice.getItems().clear();

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                delChoice.getItems().add(rs.getString(1));
            }

        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    @FXML
    protected void setDateChoice(ActionEvent event) {

        String id = (String) delChoice.getValue();

        if (id != "") {

            String sql = "SELECT date FROM Hours WHERE id='" + id + "';";
            ResultSet rs = null;

            try {

                dateChoice.getItems().clear();

                connection = DataConnect.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(sql);

                while (rs.next()) {

                    dateChoice.getItems().add(rs.getString(1));
                }
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {

                DataUtil.close(rs);
                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        }
    }

    @FXML
    protected void Cancel(ActionEvent event) {

        id = "";
        setTable();
    }

    @FXML
    protected void Specify(ActionEvent event) {

        id = (String) specifyChoice.getValue();
        setTable();
    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void backChange(MouseEvent event) { Back.setStyle("-fx-text-fill: black"); }
    @FXML
    protected void refresh(MouseEvent event) {
        Back.setStyle("-fx-text-fill: white");
    }

    @FXML
    protected void addSubmit(ActionEvent event) {

        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        String id = (String) addChoice.getValue();
        String hours = hoursField.getText();
        LocalDate localDate = datePicker.getValue();
        String date = localDate.format(dateFormatter);

        String sql = "INSERT INTO Hours VALUES ('" + id + "', " + hours + ", '" + date + "');";

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
        } finally {

            DataUtil.close(statement);
            DataUtil.close(connection);
        }

        setTable();
        setDelBoxes();
    }

    @FXML protected void delCancel(ActionEvent event) {

        setDelBoxes();
    }

    @FXML
    protected void Delete(ActionEvent event) {

        String id = (String) delChoice.getValue();
        String date = (String) dateChoice.getValue();

        String sql = "DELETE FROM Hours WHERE id='" + id + "' and date='" + date + "';";

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();

            try{
                statement.executeQuery(sql);
            } catch (SQLException ignored){}

        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(statement);
            DataUtil.close(connection);
        }

        setTable();
        setDelBoxes();
    }
}


