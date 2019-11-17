package AdvisorInterface.SubWindows.ContactUsers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import Database.DataConnect;
import Database.DataUtil;
import Login.LoginController;

public class ContactUsers implements Initializable {

    @FXML private Button Back;
    @FXML private ChoiceBox recipients;
    @FXML private TextField subjectF;
    @FXML private TextArea bodyF;

    private Connection connection;
    private Statement statement;


    public void initialize(URL url, ResourceBundle rb){

        String sql = "SELECT id FROM Persons;";
        ResultSet rs = null;

        try{

            connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()){

                recipients.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        }
    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdvisorInterface/AdvisorUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void send(ActionEvent event) throws IOException {

        String id = (String) recipients.getValue();
        String subject = subjectF.toString();
        String body = bodyF.toString();

        LocalDate date =  LocalDate.now();

        String sql = "INSERT INTO Inbox VALUES ('" + id + "', '" + LoginController.id + "', '" + subject + "', '" + body + "', '" + date + "');";

        try {

            connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            try {
                statement.executeQuery(sql);
            } catch (SQLException ignore) {
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(statement);
            DataUtil.close(connection);
        }

    }
}

