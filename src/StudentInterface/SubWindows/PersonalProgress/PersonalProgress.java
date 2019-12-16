package StudentInterface.SubWindows.PersonalProgress;

import Database.DataConnect;
import Database.DataUtil;
import Login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PersonalProgress implements Initializable {

    @FXML private Button back;
    @FXML private TableView personalTable;
    @FXML private TableColumn hoursCol;
    @FXML private TableColumn dateCol;
    @FXML private ProgressBar achievementProgress;
    @FXML private ProgressBar serviceProgress;
    @FXML private ProgressBar communityProgress;
    @FXML private Label achievementHours;
    @FXML private Label achievementLabel;
    @FXML private Label serviceHours;
    @FXML private Label serviceLabel;
    @FXML private Label communityHours;
    @FXML private Label communityLabel;

    Connection connection;
    Statement statement;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setProgress();

    }

    private void setProgress() {

        int achievement = 500;
        int service = 200;
        int community = 50;

        achievementProgress.setProgress(0);
        serviceProgress.setProgress(0);
        communityProgress.setProgress(0);

        String sql = "SELECT SUM(hours) FROM Hours WHERE id='" + LoginController.id + "' GROUP BY id;";
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
                    communityLabel.setTextFill(Color.GREEN);
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
                    serviceLabel.setTextFill(Color.GREEN);
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
                    achievementLabel.setTextFill(Color.GREEN);
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
                achievementLabel.setTextFill(Color.RED);
                serviceLabel.setText("No Hours Completed!");
                serviceLabel.setTextFill(Color.RED);
                communityLabel.setText("No Hours Completed!");
                communityLabel.setTextFill(Color.RED);
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
        }
