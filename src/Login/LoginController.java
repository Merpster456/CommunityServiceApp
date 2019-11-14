package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.CRIMSON;
import static javafx.scene.paint.Color.GREEN;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

import Database.DataConnect;
import Database.DataUtil;


public class LoginController implements Initializable {

    @FXML private Button button;
    @FXML private TextField textField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;
    private Connection connection;
    private Statement statement;

    public static String id;

    public void initialize(URL url, ResourceBundle rb) {

        try {
            connection = DataConnect.getConnection();

            if (connection == null) {
                errorMessage.setText("Not Connected!");
                errorMessage.setTextFill(CRIMSON);
            } else {
                errorMessage.setText("Database Connected!");
                errorMessage.setTextFill(GREEN);
            }
        } catch (SQLException e) {
            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {
            DataUtil.close(connection);
        }
    }

    /**
     * This is executed when the button is pressed
     * It gets value of User and Pass fields then
     * checks if it matches login credentials
     */
    @FXML
    protected void reactToClick(ActionEvent event) throws Exception {


        String id = (String) this.textField.getText();
        String password = (String) this.passwordField.getText();
        findUser(id, password);
    }

    private void findUser(String id, String password) {

        ResultSet rs = null;

        String sql = "SELECT * FROM Persons WHERE id = '" + id + "' and password = '" + password + "';";
        LoginController.id = id;

        try {

            connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            String admin = rs.getString(7);
            String deleted = rs.getString(8);
            System.out.println(admin);
            System.out.println(deleted);

            try {

                if (deleted.equals("true")) {
                    errorMessage.setText("User Deleted...");
                    errorMessage.setTextFill(CRIMSON);
                } else if (admin.equals("1")) adminLogin();
                else studentLogin();
            } catch (IOException e) {

                System.err.println("Error: " + e);
                errorMessage.setText("Error!");
                errorMessage.setTextFill(CRIMSON);
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: "+ e);

            errorMessage.setText("Wrong User or Pass");
            errorMessage.setTextFill(CRIMSON);
        } finally {
            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }


    private void adminLogin() throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
        Stage nextStage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/AdminInterface/AdminUI.fxml"));
        Scene scene = new Scene(root);
        nextStage.setResizable(false);
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
        nextStage.setResizable(false);
        nextStage.setTitle("Community Service");
        nextStage.setScene(scene);
        nextStage.show();
    }
}