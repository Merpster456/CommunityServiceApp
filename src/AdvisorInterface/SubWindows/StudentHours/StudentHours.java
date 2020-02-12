package AdvisorInterface.SubWindows.StudentHours;

import Database.DataConnect;
import Database.DataUtil;
import Objects.Student;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.HBox;
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
import java.util.*;
import java.util.function.Predicate;

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
    @FXML private ComboBox addCombo;
    @FXML private ComboBox delCombo;
    @FXML private ComboBox dateCombo;
    @FXML private ComboBox idCombo;
    @FXML private ComboBox timeCombo;
    @FXML private Label addErr;
    @FXML private Label delErr;
    @FXML private HBox primaryBox;

    private Connection connection;
    private Statement statement;

    private String id = "";
    private String interval = "";

    public void initialize(URL url, ResourceBundle rs) {

        setTable();
        setBox();
        setDelBoxes();

        timeCombo.getItems().add("Time Interval");
        timeCombo.getItems().add("Weekly");
        timeCombo.getItems().add("Monthly");
        timeCombo.getItems().add("Yearly");
    }

    @FXML
    private void setTable() {

        String SQL = "SELECT * FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY julianday(date);";
        ResultSet rs = null;
        ObservableList<Student> students = FXCollections.observableArrayList();

        if (interval.length() > 0) {


            switch (interval) {

                case "Weekly":

                    SQL = "SELECT *, strftime('%W', date) FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY julianday(date);";

                    dateCol.setText("Year & Week #");
                    dateCol.setPrefWidth(114);
                    studentTable.setPrefWidth(910);
                    primaryBox.setPrefWidth(1000);

                    try {
                        connection = DataConnect.getConnection();
                        statement = connection.createStatement();
                        rs = statement.executeQuery(SQL);


                        while (rs.next()) {
                            if (!students.isEmpty()) {
                                for (int i = 0; i < students.size(); i++) {
                                    if ((rs.getString(1).equals(students.get(i).getId())) && (rs.getInt(11) == students.get(i).getTimeInterval())
                                            && (rs.getString(3).substring(0,4).equals(students.get(i).getEmailOrDate().substring(0,4)))) {
                                        students.get(i).setIntHours(rs.getInt(2) + students.get(i).getIntHours());
                                        break;
                                    } else {
                                        if (i + 1 == students.size()) {
                                            students.add(new Student(rs.getString(1),
                                                    rs.getString(5),
                                                    rs.getString(3),
                                                    rs.getString(7),
                                                    rs.getString(8),
                                                    rs.getInt(2),
                                                    rs.getInt(11)));
                                            break;
                                        }
                                    }
                                }
                            } else {
                                students.add(new Student(rs.getString(1),
                                        rs.getString(5),
                                        rs.getString(3),
                                        rs.getString(7),
                                        rs.getString(8),
                                        rs.getInt(2),
                                        rs.getInt(11)));
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e);
                    }

                    break;
                case "Monthly":

                    SQL = "SELECT *, strftime('%m', date) FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY julianday(date);";

                    dateCol.setText("Year & Month #");
                    dateCol.setPrefWidth(114);
                    studentTable.setPrefWidth(910);
                    primaryBox.setPrefWidth(1000);

                    try {
                        connection = DataConnect.getConnection();
                        statement = connection.createStatement();
                        rs = statement.executeQuery(SQL);


                        while (rs.next()) {
                            if (!students.isEmpty()) {
                                for (int i = 0; i < students.size(); i++) {
                                    if ((rs.getString(1).equals(students.get(i).getId())) && (rs.getInt(11) == students.get(i).getTimeInterval())
                                            && (rs.getString(3).substring(0,4).equals(students.get(i).getEmailOrDate().substring(0,4)))) {
                                        students.get(i).setIntHours(rs.getInt(2) + students.get(i).getIntHours());
                                        break;
                                    } else {
                                        if (i + 1 == students.size()) {
                                            students.add(new Student(rs.getString(1),
                                                    rs.getString(5),
                                                    rs.getString(3),
                                                    rs.getString(7),
                                                    rs.getString(8),
                                                    rs.getInt(2),
                                                    rs.getInt(11)));
                                            break;
                                        }
                                    }
                                }
                            } else {
                                students.add(new Student(rs.getString(1),
                                        rs.getString(5),
                                        rs.getString(3),
                                        rs.getString(7),
                                        rs.getString(8),
                                        rs.getInt(2),
                                        rs.getInt(11)));
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e);
                    }

                    break;

                case "Yearly":
                    SQL = "SELECT *, strftime('%Y', date) FROM Hours LEFT JOIN Persons ON Hours.id=Persons.id ORDER BY julianday(date);";

                    dateCol.setText("Year");
                    dateCol.setPrefWidth(114);
                    studentTable.setPrefWidth(910);
                    primaryBox.setPrefWidth(1000);

                    try {
                        connection = DataConnect.getConnection();
                        statement = connection.createStatement();
                        rs = statement.executeQuery(SQL);


                        while (rs.next()) {
                            if (!students.isEmpty()) {
                                for (int i = 0; i < students.size(); i++) {
                                    if ((rs.getString(1).equals(students.get(i).getId())) && (rs.getInt(11) == students.get(i).getTimeInterval())
                                            && (rs.getString(3).substring(0,4).equals(students.get(i).getEmailOrDate().substring(0,4)))) {
                                        students.get(i).setIntHours(rs.getInt(2) + students.get(i).getIntHours());
                                        break;
                                    } else {
                                        if (i + 1 == students.size()) {
                                            students.add(new Student(rs.getString(1),
                                                    rs.getString(5),
                                                    rs.getString(3),
                                                    rs.getString(7),
                                                    rs.getString(8),
                                                    rs.getInt(2),
                                                    rs.getInt(11)));
                                            break;
                                        }
                                    }
                                }
                            } else {
                                students.add(new Student(rs.getString(1),
                                        rs.getString(5),
                                        rs.getString(3),
                                        rs.getString(7),
                                        rs.getString(8),
                                        rs.getInt(2),
                                        rs.getInt(11)));
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e);
                    }

                    break;
            }
            if (id.length() > 0) {
                Predicate<Student> studentPredicate = s -> !s.getId().equals(id);
                students.removeIf(studentPredicate);
            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("first"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("last"));
            this.gradCol.setCellValueFactory(new PropertyValueFactory<Student, String>("gradYear"));
            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("hoursString"));
            if (interval.equals("Yearly")) { this.dateCol.setCellValueFactory(new PropertyValueFactory<Student, String>("year")); }
            else { this.dateCol.setCellValueFactory(new PropertyValueFactory<Student, String>("interval")); }
            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradCol.setCellFactory(TextFieldTableCell.forTableColumn());
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

            studentTable.setItems(null);
            studentTable.setItems(students);

            this.dateCol.setVisible(true);


        } else {
            try {

                connection = DataConnect.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(SQL);

                dateCol.setText("Date");
                dateCol.setPrefWidth(84);
                studentTable.setPrefWidth(900);
                primaryBox.setPrefWidth(950);

                while (rs.next()) {

                    if (id.length() > 0) {
                        if (!rs.getString(1).equals(id)) continue;
                    }

                    students.add(new Student(rs.getString(1),
                            rs.getString(5),
                            rs.getString(3),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(2)));

                    this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
                    this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("first"));
                    this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("last"));
                    this.gradCol.setCellValueFactory(new PropertyValueFactory<Student, String>("gradYear"));
                    this.hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("hours"));
                    this.dateCol.setCellValueFactory(new PropertyValueFactory<Student, String>("emailOrDate"));

                    idCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    gradCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
                    dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

                    studentTable.setItems(null);
                    studentTable.setItems(students);

                    this.dateCol.setVisible(true);
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

            this.idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Student, String>("first"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Student, String>("last"));
            this.gradCol.setCellValueFactory(new PropertyValueFactory<Student, String>("gradYear"));
            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Student, String>("hours"));
            this.dateCol.setCellValueFactory(null);

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradCol.setCellFactory(TextFieldTableCell.forTableColumn());
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

            studentTable.setItems(null);
            studentTable.setItems(students);

            dateCol.setPrefWidth(84);
            this.dateCol.setVisible(false);
            studentTable.setPrefWidth(900);
            primaryBox.setPrefWidth(950);

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

            addCombo.getItems().clear();
            idCombo.getItems().clear();
            idCombo.getItems().add("Select ID");
            addCombo.getItems().add("Select ID");


            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while(rs.next()) {

                addCombo.getItems().add(rs.getString(1));
                idCombo.getItems().add(rs.getString(1));
            }

            timeCombo.setValue("Time Interval");
            idCombo.setValue("Select ID");
            addCombo.setValue("Select ID");
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

            delCombo.getItems().clear();
            dateCombo.getItems().clear();
            delCombo.getItems().add("Select ID");
            dateCombo.getItems().add("Date of Event");


            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                delCombo.getItems().add(rs.getString(1));
            }

        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);

            delCombo.setValue("Select ID");
            dateCombo.setValue("Date of Event");
        }
    }
    @FXML
    protected void specify(ActionEvent event) {

        interval = (String) timeCombo.getValue();
        id = (String) idCombo.getValue();

        if (interval.equals("Time Interval")) {
            interval = "";
        }
        if (id.equals("Select ID")) {
            id = "";
        }
        setTable();
    }

    @FXML
    protected void setDateChoice(ActionEvent event) {

        String id = (String) delCombo.getValue();

        try {
            if (!id.equals("")) {

                String sql = "SELECT date FROM Hours WHERE id='" + id + "' GROUP BY date;";
                ResultSet rs = null;

                try {

                    dateCombo.getItems().clear();
                    dateCombo.getItems().add("Date of Event");
                    dateCombo.setValue("Date of Event");

                    connection = DataConnect.getConnection();
                    statement = connection.createStatement();
                    rs = statement.executeQuery(sql);

                    while (rs.next()) {

                        dateCombo.getItems().add(rs.getString(1));
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
        } catch (NullPointerException e) {
            System.out.println("Error: " + e);
        }

    }

    @FXML
    protected void cancel(ActionEvent event) {

        id = "";
        interval = "";
        setTable();
        setBox();
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

    @FXML protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }

    @FXML
    protected void refresh(MouseEvent event) {
        back.setStyle("-fx-text-fill: white");
    }

    @FXML
    protected void addSubmit(ActionEvent event) {

        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        if (addCombo.getValue().equals("Select ID")) {
            addErr.setText("Please Select ID");
        } else if (hoursField.getText().equals("") || datePicker.getValue().toString().equals("")) {
            addErr.setText("Please Insert Number of Hours and Date ");
        } else {

            String id = (String) addCombo.getValue();
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
                    addErr.setText("");
                }

                setTable();
                setDelBoxes();
                addCombo.setValue("Select ID");
            } catch (NumberFormatException e) {
                addErr.setText("Insert a Numerical Value");
            }
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
        delErr.setText("");
    }

    @FXML
    protected void Delete(ActionEvent event) {

        String id = (String) delCombo.getValue();
        String date = (String) dateCombo.getValue();

        String sql = "DELETE FROM Hours WHERE id=? and date=?;";

        if (id.equals("Select ID")) {
            delErr.setText("Please Select ID");
        } else if (date.equals("Date of Event")) {
            delErr.setText("Please Select the Date of Event");
        } else {
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
}
