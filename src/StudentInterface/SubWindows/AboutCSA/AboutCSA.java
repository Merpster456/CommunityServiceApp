package StudentInterface.SubWindows.AboutCSA;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutCSA implements Initializable {

    @FXML private Button back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }


    @FXML
    protected void back(ActionEvent event) throws IOException {

        Stage stage = (Stage) back.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/StudentInterface/StudentUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML protected void backChange(MouseEvent event) { back.setStyle("-fx-text-fill: black"); }
    @FXML protected void refresh(MouseEvent event) {
        back.setStyle("-fx-text-fill: white");
    }
}

