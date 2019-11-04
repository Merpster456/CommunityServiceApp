package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.CRIMSON;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import Database.dataConnect;


public class LoginController {


    @FXML private Button button;
    @FXML private TextField textField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;


    /** This is executed when the button is pressed
        It gets value of User and Pass fields then
        checks if it matches login credentials */
    @FXML
    protected void reactToClick(ActionEvent event) throws Exception {


        String id = (String) this.textField.getText();
        String password = (String) this.passwordField.getText();
        findUser( id, password);
    }

    private void findUser(String id, String password) {

        id = id;
        password = password;
        String sql = "SELECT * FROM Persons WHERE id = '" + id + "' and password = '" + password + "';";

        try {

            Connection connection = dataConnect.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            String admin = rs.getString(7);
            String deleted = rs.getString(8);
            System.out.println(admin);
            System.out.println(deleted);

            try {

                if (deleted.equals("true")) errorMessage.setText("User Deleted...");
                else if (admin.equals("1")) adminLogin();
                else studentLogin();
            } catch (IOException e) {

                System.err.println("Error: " + e);
                errorMessage.setText("Error!");

            }
        }
        catch (SQLException e) {

            System.err.println("Wrong User or Pass");
            errorMessage.setText("Wrong User or Pass");

        }
     }


    private void adminLogin() throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
        Stage nextStage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/AdminInterface/AdminUI.fxml"));
        Scene scene = new Scene(root);
        nextStage.setResizable(false );
        nextStage.setTitle("Community Service");
        nextStage.setScene(scene);
        nextStage.show();
    }

    private void studentLogin() throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
        Stage nextStage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/StudentUI.fxml"));
        Scene scene = new Scene(root);
        nextStage.setResizable(false );
        nextStage.setTitle("Community Service");
        nextStage.setScene(scene);
        nextStage.show();
    }
}
