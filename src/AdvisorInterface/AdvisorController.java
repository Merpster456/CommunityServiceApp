package AdvisorInterface;

import Login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

import Database.DataConnect;

import static Login.LoginController.*;

public class AdvisorController implements Initializable {

    @FXML private Label topLabel;
    @FXML private Button studentHours;
    @FXML private Button chapterProgress;
    @FXML private Button manageUsers;
    //@FXML private Button contactUsers;
    @FXML private ImageView imageView;

    private final String id = LoginController.id;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            String label = "Welcome " + getName(id) + "!";
            this.topLabel.setText(label);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh(null);

        File file = new File("src/FBLA.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    protected void setStudentHours(MouseEvent event) {

        studentHours.setStyle(primaryLight);
        studentHours.setTextFill(Color.BLACK);

    }
    @FXML
    protected void setChapterProgress(MouseEvent event) {

        chapterProgress.setStyle(primaryLight);
        chapterProgress.setTextFill(Color.BLACK);

    }
    @FXML
    protected void setManageUsers(MouseEvent event) {

        manageUsers.setStyle(primaryLight);
        manageUsers.setTextFill(Color.BLACK);
    }
    /*@FXML
    protected void setContactUsers(MouseEvent event) {

        contactUsers.setStyle(primaryLight);
        contactUsers.setTextFill(Color.BLACK);

    }*/
    @FXML
    protected void refresh(MouseEvent event) {
        studentHours.setStyle(primary);
        chapterProgress.setStyle(primary);
        manageUsers.setStyle(primary);
        //contactUsers.setStyle(primary);

        studentHours.setTextFill(Color.WHITE);
        chapterProgress.setTextFill(Color.WHITE);
        manageUsers.setTextFill(Color.WHITE);
        //contactUsers.setTextFill(Color.WHITE);
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
    /*
    @FXML
    protected void contactUsers(ActionEvent event) throws IOException {

        Stage stage = (Stage) studentHours.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/SubWindows/ContactUsers/ContactUsers.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
     */
}