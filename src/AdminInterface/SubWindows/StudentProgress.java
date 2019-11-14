package AdminInterface.SubWindows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentProgress implements Initializable {

    @FXML private Button Back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) Back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/AdminInterface/AdminUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
