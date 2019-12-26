package AdvisorInterface.SubWindows.StudentHours;

import Database.DataConnect;
import Database.DataUtil;
import Objects.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentHours implements Initializable {

    @FXML private Button back;
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
    @FXML private Label hoursErr;

    private Connection connection;
    private Statement statement;

    private String id = "";

    public void initialize(URL url, ResourceBundle rs){

        setTable();
        setBox();
        setDelBoxes();

    }

    @FXML
    private void setTable() {

        String SQL = "SELECT * FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY Hours.id;";
        ResultSet rs = null;
        List<Student> list = new ArrayList<Student>();
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

        Stage stage = (Stage) back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void print(ActionEvent event) {

        Stage stage = (Stage) back.getScene().getWindow();

        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        printerJob.getJobSettings().setPageLayout(pageLayout);

        final double scaleX = pageLayout.getPrintableWidth() / studentTable.getBoundsInParent().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / studentTable.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        studentTable.getTransforms().add(scale);

        if(printerJob.showPrintDialog(stage.getOwner()) && printerJob.printPage(studentTable)){
            studentTable.getTransforms().remove(scale);
            printerJob.endJob();
        }
        else {
            studentTable.getTransforms().remove(scale);
        }
    }

    @FXML
    protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }
    @FXML
    protected void refresh(MouseEvent event) {
        back.setStyle("-fx-text-fill: white");
    }

    @FXML
    protected void addSubmit(ActionEvent event) {

        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        String id = (String) addChoice.getValue();
        String h = hoursField.getText();

        LocalDate localDate = datePicker.getValue();
        String date = localDate.format(dateFormatter);

        try {
            int hours = Integer.parseInt(h);
            String sql = "INSERT INTO Hours VALUES (?,?,?);";

            try {

                connection = DataConnect.getConnection();
                PreparedStatement submit = connection.prepareStatement(sql);
                submit.setString(1, id);
                submit.setInt(2, hours);
                submit.setString(3, date);
                submit.executeUpdate();

            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {
                DataUtil.close(connection);
            }

            setTable();
            setDelBoxes();
        } catch (NumberFormatException e) {
            hoursErr.setText("Insert a Numerical Value");
        }
    }

    @FXML
    protected void acceptWindow(ActionEvent event) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/StudentHours/AcceptEvents/AcceptEvents.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML protected void delCancel(ActionEvent event) {

        setDelBoxes();
    }

    @FXML
    protected void Delete(ActionEvent event) {

        String id = (String) delChoice.getValue();
        String date = (String) dateChoice.getValue();

        String sql = "DELETE FROM Hours WHERE id=? and date=?;";

        try {

            connection = DataConnect.getConnection();
            PreparedStatement deleteEvent = connection.prepareStatement(sql);
            deleteEvent.setString(1, id);
            deleteEvent.setString(2, date);
            deleteEvent.executeUpdate();
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {
            DataUtil.close(connection);
        }

        setTable();
        setDelBoxes();
    }
}


