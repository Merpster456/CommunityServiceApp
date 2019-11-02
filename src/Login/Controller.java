package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.CRIMSON;

import java.sql.*;


public class Controller {

    @FXML
    private Button button;
    @FXML
    private TextField textField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;

    /** This is executed when the button is pressed
        It gets value of User and Pass fields then
        checks if it matches login credentials */
    @FXML
    public void reactToClick(ActionEvent event) throws Exception {

    Stage stage = (Stage) this.button.getScene().getWindow();

    String user = (String) this.textField.getText();
    String password = (String) this.passwordField.getText();

    try {
        if (user.equals("admin") && password.equals("password")) {

            stage.close();
            Stage nextStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane)loader.load(getClass().getResource("/AdminInterface/new.fxml"));
            Scene scene = new Scene(root, 1400,900);
            nextStage.setResizable(false );
            nextStage.setTitle("Community Service");
            nextStage.setScene(scene);
            nextStage.show();



        }
        else {

            //Wrong User or Pass
            this.errorMessage.setTextFill(CRIMSON);
            this.errorMessage.setText("Wrong Username or Password");
        }
    }
    catch (Exception e){

            System.out.println("Error "+ e);
            stage.close();
        }

    }

}
