package StudentInterface.SubWindows.StudentContact;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

public class StudentContact implements Initializable {

    @FXML private Button back;
    @FXML private ChoiceBox<String> recipients;
    @FXML private TextField subject;
    @FXML private TextArea body;

    private Connection connection;
    private Statement statement;


    public void initialize(URL url, ResourceBundle rb){

        setBox();
    }

    private void setBox() {

        String sql = "SELECT id FROM Persons;";
        ResultSet rs = null;
        recipients.getItems().removeAll();
        recipients.getItems().add("Select Recipient");


        try{

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()){

                recipients.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        }

        recipients.setValue("Select Recipient");
    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/StudentUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    protected void send(ActionEvent event) throws IOException {

        String id = recipients.getValue();
        String _subject = subject.toString();
        String _body = body.toString();

        LocalDate date =  LocalDate.now();

        String sql = "INSERT INTO Inbox VALUES ('" + id + "', '" + LoginController.id + "', '" + _subject + "', '" + _body + "', '" + date + "');";

        try {

            connection = DataConnect.getConnection();
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

    @FXML protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }
    @FXML protected void refresh(MouseEvent event) { back.setStyle("-fx-text-fill: white"); }
}

