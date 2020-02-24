package StudentInterface;

import Database.DataConnect;
import Login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Login.LoginController.*;

public class StudentController implements Initializable {

    @FXML private Label topLabel;
    @FXML private Button personalProgress;
    @FXML private Button peersProgress;
    //@FXML private Button contactUsers;
    @FXML private Button aboutCSA;
    @FXML private ImageView imageView;

    private final String id = LoginController.id;

    public void initialize(URL url, ResourceBundle rb){
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

    private String getName(String id) throws SQLException {

        String SQL = "SELECT * FROM Persons WHERE id ='" + id + "';";

        Connection connection = DataConnect.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(SQL);
        return rs.getString("first");

    }

    @FXML
    protected void setPersonalProgress(MouseEvent event) {

        personalProgress.setStyle(primaryLight);
        personalProgress.setTextFill(Color.BLACK);
    }
    @FXML
    protected void setPeersProgress(MouseEvent event) {

        peersProgress.setStyle(primaryLight);
        peersProgress.setTextFill(Color.BLACK);
    }
    /*
    @FXML
    protected void setContactUsers(MouseEvent event) {

        contactUsers.setStyle(primaryLight);
        contactUsers.setTextFill(Color.BLACK);
    }
     */
    @FXML
    protected void setAboutCSA(MouseEvent event) {

        aboutCSA.setStyle(primaryLight);
        aboutCSA.setTextFill(Color.BLACK);
    }
    @FXML
    protected void refresh(MouseEvent event) {

        personalProgress.setStyle(primary);
        peersProgress.setStyle(primary);
        //contactUsers.setStyle(primary);
        aboutCSA.setStyle(primary);

        personalProgress.setTextFill(Color.WHITE);
        peersProgress.setTextFill(Color.WHITE);
        //contactUsers.setTextFill(Color.WHITE);
        aboutCSA.setTextFill(Color.WHITE);

    }

    @FXML
    protected void PersonalProgress(ActionEvent event) throws IOException {

        Stage stage = (Stage) personalProgress.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/SubWindows/PersonalProgress/PersonalProgress.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void PeersProgress(ActionEvent event) throws IOException {

        Stage stage = (Stage) personalProgress.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/SubWindows/PeersProgress/PeersProgress.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    /*
    @FXML
    protected void ContactUsers(ActionEvent event) throws IOException {

        Stage stage = (Stage) personalProgress.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/SubWindows/StudentContact/StudentContact.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
     */

    @FXML
    protected void AboutCSA(ActionEvent event) throws IOException {

        Stage stage = (Stage) personalProgress.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/SubWindows/AboutCSA/AboutCSA.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
