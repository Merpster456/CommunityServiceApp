package StudentInterface.SubWindows.PersonalProgress;

import Database.DataConnect;
import Database.DataUtil;
import Login.LoginController;
import Objects.Event;
import Objects.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.sqlite.SQLiteErrorCode;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Login.LoginController.id;

public class PersonalProgress implements Initializable {

    @FXML private Button back;
    @FXML private TableView<Event> personalTable;
    @FXML private TableColumn<Event, String> hoursCol;
    @FXML private TableColumn<Event, String> dateCol;
    @FXML private ProgressBar achievementProgress;
    @FXML private ProgressBar serviceProgress;
    @FXML private ProgressBar communityProgress;
    @FXML private Label achievementHours;
    @FXML private Label achievementLabel;
    @FXML private Label serviceHours;
    @FXML private Label serviceLabel;
    @FXML private Label communityHours;
    @FXML private Label communityLabel;
    @FXML private Label hoursErr;
    @FXML private Label dateErr;
    @FXML private Label mainErr;
    @FXML private Label hours;
    @FXML private TextField hoursField;
    @FXML private DatePicker dateField;
    @FXML private TextArea descField;

    Connection connection;
    Statement statement;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setProgress();
        setTable();
        totalHours();
    }

    private void totalHours() {

        String SQL = "SELECT sum(hours) FROM Hours WHERE id='" + id + "';";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            hours.setText(rs.getString(1));
        } catch (SQLException e){

            System.out.println("Error: " + e);
            System.err.println(e.getSQLState());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setTable() {

        String SQL = "SELECT hours, date FROM Hours WHERE id='" + id + "' ORDER BY julianday(date);";
        ResultSet rs = null;
        ObservableList<Event> events = FXCollections.observableArrayList();

        try{

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {
                events.add(new Event(rs.getString(1), rs.getString(2)));
            }

            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Event, String>("hours"));
            this.dateCol.setCellValueFactory(new PropertyValueFactory<Event, String>("date"));
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dateCol.setCellFactory(TextFieldTableCell.forTableColumn());

            personalTable.setItems(null);
            personalTable.setItems(events);

            this.personalTable.setVisible(true);

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getSQLState());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setProgress() {

        int achievement = 500;
        int service = 200;
        int community = 50;

        achievementProgress.setProgress(0);
        serviceProgress.setProgress(0);
        communityProgress.setProgress(0);

        String sql = "SELECT SUM(hours) FROM Hours WHERE id='" + id + "' GROUP BY id;";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            try {
                double hours = rs.getInt(1);


                double achievementPercent = hours / ((double) achievement);
                double servicePercent = hours / ((double) service);
                double communityPercent = hours / ((double) community);

                if (communityPercent >= 1) {

                    communityProgress.setProgress(1);
                    communityLabel.setText("AWARD ACCOMPLISHED!");
                    communityLabel.setStyle("-fx-text-fill: limegreen;");
                } else {

                    communityProgress.setProgress(communityPercent);
                    double result = community - hours;
                    if (result % 2 == 0 || result % 2 == 5) {
                        int result1 = (int) result;
                        communityHours.setText(String.valueOf(result1));
                    } else communityHours.setText(String.valueOf(result));
                }

                if (servicePercent >= 1) {

                    serviceProgress.setProgress(1);
                    serviceLabel.setText("AWARD ACCOMPLISHED!");
                    serviceLabel.setStyle("-fx-text-fill: limegreen;");
                } else {

                    serviceProgress.setProgress(servicePercent);
                    double result = service - hours;
                    if (result % 2 == 0 || result % 2 == 5) {
                        int result1 = (int) result;
                        serviceHours.setText(String.valueOf(result1));
                    } else serviceHours.setText(String.valueOf(result));
                }
                if (achievementPercent >= 1) {

                    achievementProgress.setProgress(1);
                    achievementLabel.setText("AWARD ACCOMPLISHED!");
                    achievementLabel.setStyle("-fx-text-fill: limegreen;");
                } else {

                    achievementProgress.setProgress(achievementPercent);
                    double result = achievement - hours;
                    if (result % 2 == 0 || result % 2 == 5) {
                        int result1 = (int) result;
                        achievementHours.setText(String.valueOf(result1));
                    } else achievementHours.setText(String.valueOf(result));
                }
            } catch (SQLException ignored) {

                achievementLabel.setText("No Hours Completed!");
                achievementLabel.setStyle("-fx-text-fill: red;");
                serviceLabel.setText("No Hours Completed!");
                serviceLabel.setStyle("-fx-text-fill: red;");
                communityLabel.setText("No Hours Completed!");
                communityLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {

            System.err.println(e.getSQLState());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/StudentUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void cancel(ActionEvent event) {

        hoursField.setText("");
        dateField.setValue(null);
        descField.setText("");

        hoursErr.setText("");
        dateErr.setText("");
        mainErr.setText("");
    }

    @FXML
    protected void submit(ActionEvent event) {

        boolean control = true;

        if (hoursField.getText().length() == 0) {

            hoursErr.setText("Please Specify Number of Hours!");
            control = false;
        }
        try {

            dateField.getValue().toString();
        } catch (NullPointerException e) {

            dateErr.setText("Please Specify Date!");
            control = false;
        }

        if (control) {
            try {
                String h = hoursField.getText();
                int hours = Integer.parseInt(h);

                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate localDate = dateField.getValue();
                String date = localDate.format(dateFormatter);

                String desc = descField.getText();

                String sql = "INSERT INTO Pending VALUES (?,?,?,?);";

                try {

                    connection = DataConnect.getConnection();
                    PreparedStatement submit = connection.prepareStatement(sql);
                    submit.setString(1, id);
                    submit.setInt(2, hours);
                    submit.setString(3, date);
                    submit.setString(4, desc);
                    submit.executeUpdate();

                    hoursField.setText("");
                    dateField.setValue(null);
                    descField.setText("");

                    hoursErr.setText("");
                    dateErr.setText("");
                    mainErr.setText("");
                } catch (SQLException e) {

                    System.err.println(e.getStackTrace()[0].getLineNumber());
                    System.out.println("Error: " + e);
                } finally {

                    DataUtil.close(connection);

                }

            } catch (NumberFormatException e) {

                hoursErr.setText("Please Insert a Numerical Value!");
            }
        } else {

            mainErr.setText("Please Change needed fields!");
        }
    }

    @FXML
    protected void print(ActionEvent event) {

        Stage stage = (Stage) back.getScene().getWindow();

        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        printerJob.getJobSettings().setPageLayout(pageLayout);

        final double scaleX = pageLayout.getPrintableWidth() / personalTable.getBoundsInParent().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / personalTable.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        personalTable.getTransforms().add(scale);

        if(printerJob.showPrintDialog(stage.getOwner()) && printerJob.printPage(personalTable)){
            personalTable.getTransforms().remove(scale);
            printerJob.endJob();
        }
        else {
            personalTable.getTransforms().remove(scale);
        }
    }
    @FXML protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }

    @FXML
    protected void refresh(MouseEvent event) {
        back.setStyle("-fx-text-fill: white");
    }
}
