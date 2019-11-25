package StudentInterface.SubWindows.PeersProgress;

import Database.DataConnect;
import Database.DataUtil;
import Objects.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class PeersProgress implements Initializable {

    @FXML private Button back;
    @FXML private Label communityNum;
    @FXML private Label serviceNum;
    @FXML private Label achievementNum;
    @FXML private Label totalHours;
    @FXML private Label totalParticipants;
    @FXML private Label firstStudent;
    @FXML private Label secondStudent;
    @FXML private Label thirdStudent;

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

        String sql = "SELECT id, SUM(hours) FROM Hours GROUP BY id;";
        ResultSet rs = null;

        int first = 0;
        int second = 0;
        int third = 0;

        String firstID = "";
        String secondID = "";
        String thirdID = "";

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                Student student = new Student(rs.getString(1), rs.getInt(2));

                if (student.getIntHours() > first) {

                    third = second;
                    thirdID = secondID;

                    second = first;
                    secondID = firstID;

                    first = student.getIntHours();
                    firstID = student.getId();

                } else if (student.getIntHours() > second) {

                    third = second;
                    thirdID = secondID;

                    second = student.getIntHours();
                    secondID = student.getId();

                } else if (student.getIntHours() > third) {

                    third = student.getIntHours();
                    thirdID = student.getId();
                }
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

        sql = "SELECT id, first, last FROM Persons;";

        String firstName = "";
        String secondName = "";
        String thirdName = "";

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                if (rs.getString(1).equals(firstID)) {

                    firstName = rs.getString(2) + " " + rs.getString(3);
                } else if (rs.getString(1).equals(secondID)) {

                    secondName = rs.getString(2) + " " + rs.getString(3);
                } else if (rs.getString(1).equals(thirdID)) {

                    thirdName = rs.getString(2) + " " + rs.getString(3);
                }
            }

            firstStudent.setText(firstName + " with " + first + " hours");
            secondStudent.setText(secondName + " with " + second + " hours");
            thirdStudent.setText(thirdName + " with " + third + " hours");

        } catch (SQLException e){

            System.err.println(e.getStackTrace()[0].getLineNumber());
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
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
