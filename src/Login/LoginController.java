package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
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

    @FXML private GridPane grid;
    @FXML private Button button;
    @FXML private TextField textField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;
    @FXML private Label topLabel;
    @FXML private Label idLabel;
    @FXML private Label passLabel;

    private Connection connection;
    private Statement statement;

    public static String id;
    public static String primaryDark = "-fx-background-color: #3d5878";
    public static String primary = "-fx-background-color: #5377a1";
    public static String primaryLight = "-fx-background-color: #789cc7";

    public void initialize(URL url, ResourceBundle rb) {

        try {
            connection = DataConnect.getConnection();

            if (connection == null) {
                errorMessage.setText("Not Connected!");
                errorMessage.setTextFill(CRIMSON);
                errorMessage.setStyle("-fx-font-weight: bold;");
            } else {
                errorMessage.setText("Database Connected!");
                errorMessage.setTextFill(Color.WHITE);
                errorMessage.setStyle("-fx-font-weight: bold;");
            }
        } catch (SQLException e) {
            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {
            DataUtil.close(connection);
        }

        grid.setStyle("-fx-background-color: #3d5878");
        topLabel.setTextFill(Color.WHITE);
    }

    /**
     * Gets value of the login fields,
     * then calls the login function
     *
     * @param event
     * @throws Exception
     */
    @FXML
    protected void reactToClick(ActionEvent event) throws Exception {


        String id = (String) this.textField.getText();
        String password = (String) this.passwordField.getText();
        findUser(id, password);
    }

    /**
     * Checks for user with given
     * credentials then logs into
     * respective interfaces
     *
     * @param id
     * @param password
     * @throws IOException
     */
    private void findUser(String id, String password) throws IOException {

        ResultSet rs = null;

        String sql = "SELECT * FROM Persons WHERE id = '" + id + "' and password = '" + password + "';";
        LoginController.id = id;
        boolean control = false;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            String admin = rs.getString(7);

            try {

                if (admin.equals("1")) advisorLogin();
                else if (admin.equals("0")) studentLogin();
            } catch (IOException e) {

                System.err.println("Error: " + e);
                errorMessage.setText("Error!");
                errorMessage.setTextFill(CRIMSON);
            }
        } catch (SQLException ignored) {

            try {

                sql = "SELECT * FROM Admin WHERE user='" + id + "' and pass='" + password + "';";

                DataUtil.close(rs);
                rs = statement.executeQuery(sql);


                if (rs.getString(1).equals(id)) {

                    control = true;
                }


            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: "+ e);

                errorMessage.setText("Wrong User or Pass");
                errorMessage.setTextFill(CRIMSON);
            }

        } finally {

            if (rs != null) DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
            if (control) adminLogin();
        }
    }

    /**
     * Logs into advisor interface
     *
     * @throws IOException
     */
    private void advisorLogin() throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
        Stage nextStage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        nextStage.setResizable(false);
        nextStage.setTitle("Community Service");
        nextStage.setScene(scene);
        nextStage.show();
    }

    /**
     * Logs into student interface
     *
     * @throws IOException
     */
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

    /**
     * Logs into admin interface
     *
     * @throws IOException
     */

    private void adminLogin() throws IOException {

        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
        Stage nextStage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/AdminInterface/AdminUI.fxml"));
        Scene scene = new Scene(root);
        nextStage.setResizable(false);
        nextStage.setTitle("Admin");
        nextStage.setScene(scene);
        nextStage.show();
    }
}