package AdvisorInterface;

import Login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

import Database.DataConnect;

public class AdvisorController implements Initializable {

    @FXML private Label TopLabel;
    @FXML private Button studentHours;

    private final String id = LoginController.id;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.TopLabel.setText("Welcome " + getName(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String getName(String id) throws SQLException {

        String SQL = "SELECT * FROM Persons WHERE id ='" + id + "';";

        Connection connection = DataConnect.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(SQL);
        return rs.getString("first");

    }

    @FXML
    protected void studentHours(ActionEvent event) throws Exception {

        Stage stage = (Stage) studentHours.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/StudentHours/StudentHours.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void studentProgress(ActionEvent event) throws IOException {

        Stage stage = (Stage) studentHours.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/ChapterProgress/ChapterProgress.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void manageUsers(ActionEvent event) throws IOException {

        Stage stage = (Stage) studentHours.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/ManageUsers/ManageUsers.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    protected void contactUsers(ActionEvent event) throws IOException {

        Stage stage = (Stage) studentHours.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/ContactUsers/ContactUsers.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}