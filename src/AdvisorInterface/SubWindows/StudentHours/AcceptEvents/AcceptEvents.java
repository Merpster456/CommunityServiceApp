package AdvisorInterface.SubWindows.StudentHours.AcceptEvents;

import AdvisorInterface.SubWindows.StudentHours.StudentHours;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import Database.DataConnect;
import Database.DataUtil;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class AcceptEvents implements Initializable {

    @FXML private Label firstLabel;
    @FXML private Label lastLabel;
    @FXML private Label dateLabel;
    @FXML private Label hoursLabel;
    @FXML private Label description;
    @FXML private Label errorLabel;
    @FXML private ListView<String> listView;

    private Connection connection;
    private Statement statement;

    public void initialize (URL url, ResourceBundle rb){

        firstLabel.setText("First Name: ");
        lastLabel.setText("Last Name: ");
        dateLabel.setText("Date: ");
        hoursLabel.setText("Hours: ");
        setList();
    }

    private void setList() {

        String sql = "SELECT id, date FROM Pending GROUP BY date;";
        ResultSet rs = null;
        List<String> events= new ArrayList<String>();

        try {
            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            try {
                rs = statement.executeQuery(sql);
                while (rs.next()) {
                    events.add(rs.getString(1) + ", " + rs.getString(2));
                }
                listView.getItems().addAll(events);
            } catch (SQLException e) {
                System.out.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            }
        } catch (SQLException e) {
            System.out.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        }
    }

    @FXML
    private void selected(MouseEvent event) {

        try {
            String[] selectedItems = listView.getSelectionModel().getSelectedItem().split(", ");
            String id = selectedItems[0];
            String date = selectedItems[1];

            String sql = "SELECT Persons.first, Persons.last, Pending.hours, Pending.desc FROM Pending LEFT JOIN Persons WHERE Pending.id='" + id + "' and Pending.date='" + date + "' and Persons.id='" + id + "';";
            ResultSet rs = null;

            try{

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                try {
                    rs = statement.executeQuery(sql);

                    rs.next();

                    String first = rs.getString(1);
                    String last = rs.getString(2);
                    int hours = rs.getInt(3);
                    String desc = rs.getString(4);

                    while (rs.next()) {

                        hours += rs.getInt(3);
                        desc += "\n\n" + rs.getString(4);
                    }

                    firstLabel.setText("First Name: " + first);
                    lastLabel.setText("Last Name: " + last);
                    dateLabel.setText("Date: " + date);
                    hoursLabel.setText("Hours:" + hours);
                    description.setText(desc);


                } catch (NullPointerException e) {
                    errorLabel.setText(("Error! Empty Result Set"));
                }
            } catch (SQLException e) {

                System.out.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);

                errorLabel.setText("SQL Error!");
            }
        } catch (NullPointerException e) {}

    }
    @FXML
    protected void reject(ActionEvent event) {

        String[] selectedItems = listView.getSelectionModel().getSelectedItem().split(", ");
        String id = selectedItems[0];
        String date = selectedItems[1];

        String sql = "DELETE FROM Pending WHERE id = ? and date = ?;";

        try{

            connection = DataConnect.getConnection();
            PreparedStatement delete = connection.prepareStatement(sql);
            delete.setString(1, id);
            delete.setString(2, date);
            delete.executeUpdate();
        } catch (SQLException e) {

            System.out.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(connection);
            listView.getItems().clear();
            setList();
        }
    }
    @FXML
    protected void accept(ActionEvent event) {

        String[] selectedItems = listView.getSelectionModel().getSelectedItem().split(", ");
        String id = selectedItems[0];
        String date = selectedItems[1];

        String sql = "SELECT hours FROM Pending WHERE id='" + id + "' and date='" + date + "';";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            int hours = rs.getInt(1);

            DataUtil.close(rs);
            DataUtil.close(statement);

            try {

                sql = "INSERT INTO Hours VALUES (?,?,?);";

                PreparedStatement insert = connection.prepareStatement(sql);
                insert.setString(1, id);
                insert.setInt(2, hours);
                insert.setString(3, date);
                insert.executeUpdate();

                DataUtil.close(insert);
                try{

                    sql = "DELETE FROM Pending WHERE id=? and date=?;";

                    PreparedStatement delete = connection.prepareStatement(sql);
                    delete.setString(1, id);
                    delete.setString(2, date);
                    delete.executeUpdate();

                    DataUtil.close(delete);
                } catch (SQLException e) {

                    System.out.println(e.getStackTrace()[0].getLineNumber());
                    System.out.println("Error: " + e);
                } finally {

                    DataUtil.close(connection);
                    listView.getItems().clear();
                    setList();
                }
            } catch (SQLException e) {

                System.out.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            }
        } catch (SQLException e) {

            System.out.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        }
    }
}
