package AdvisorInterface.SubWindows.ChapterProgress;

import Database.DataConnect;
import Database.DataUtil;
import Objects.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChapterProgress implements Initializable {

    @FXML private Button back;
    @FXML private Label communityNum;
    @FXML private Label serviceNum;
    @FXML private Label achievementNum;
    @FXML private Label totalHours;
    @FXML private Label totalParticipants;
    @FXML private Label firstStudent;
    @FXML private Label secondStudent;
    @FXML private Label thirdStudent;
    @FXML private HBox mainBox;

    private Connection connection;
    private Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setNums();
        setTop3();
        setTotals();
    }

    private void setNums() {

        String sql = "SELECT id, SUM(hours) FROM Hours GROUP BY id;";
        ResultSet rs = null;

        int community = 0;
        int service = 0;
        int achievement = 0;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while(rs.next()) {

                Student student = new Student(rs.getString(1), rs.getString(2));

                if (student.isCommunity()) community += 1;
                if (student.isService()) service += 1;
                if (student.isAchievement()) achievement += 1;
            }

            String Achievement = String.valueOf(achievement);
            achievementNum.setText(Achievement);

            String Service = String.valueOf(service);
            serviceNum.setText(Service);

            String Community = String.valueOf(community);
            communityNum.setText(Community);

        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setTotals() {

        String sql = "SELECT SUM(hours) FROM Hours;";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            totalHours.setText(rs.getString(1));
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

        sql = "SELECT id FROM Hours GROUP BY id;";
        rs = null;
        int participants = 0;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                participants +=1;
            }

            totalParticipants.setText(String.valueOf(participants));
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setTop3() {

        String sql = "SELECT Persons.first, SUM(hours) FROM Hours LEFT JOIN Persons on Hours.id=Persons.id GROUP BY Persons.first ORDER BY hours DESC";
        ResultSet rs = null;
        ArrayList<Student> students = new ArrayList<>();

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                Student student = new Student(rs.getString(1), rs.getString(2));
                students.add(student);
                if (students.size() == 3) { break; }
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

        try {
            firstStudent.setText(students.get(0).getId() + " with " + students.get(0).getHours() + " hours");
            secondStudent.setText (students.get(1).getId() + " with " + students.get(1).getHours() + " hours");
            thirdStudent.setText(students.get(2).getId() + " with " + students.get(2).getHours() + " hours");
        } catch (IndexOutOfBoundsException ignore) {}
    }

    @FXML
    protected void print(ActionEvent event) {

        Stage stage = (Stage) back.getScene().getWindow();

        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
        printerJob.getJobSettings().setPageLayout(pageLayout);

        mainBox.setLayoutX(0);
        mainBox.setLayoutY(0);

        final double scaleX = pageLayout.getPrintableWidth() / mainBox.getBoundsInParent().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / mainBox.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        mainBox.getTransforms().add(scale);

        if(printerJob.showPrintDialog(stage.getOwner()) && printerJob.printPage(mainBox)){
            mainBox.getTransforms().remove(scale);
            mainBox.setLayoutX(183);
            mainBox.setLayoutY(184);
            printerJob.endJob();

        }
        else {
            mainBox.getTransforms().remove(scale);
            mainBox.setLayoutX(183);
            mainBox.setLayoutY(184);
        }
    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }
    @FXML protected void refresh(MouseEvent event) { back.setStyle("-fx-text-fill: white"); }
}
