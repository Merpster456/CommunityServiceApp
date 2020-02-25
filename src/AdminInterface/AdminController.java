package AdminInterface;

import Database.DataUtil;

import Objects.Person;
import Objects.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Database.DataConnect;
import javafx.stage.StageStyle;

public class AdminController implements Initializable {

    @FXML private ChoiceBox choiceBox;
    @FXML private ChoiceBox changeBox;
    @FXML private ChoiceBox recoverBox;
    @FXML private ChoiceBox permanentBox;
    @FXML private TableView<Person> personTable;
    @FXML private TableView<Person> delAccTable;
    @FXML private TableView<Person> delHoursTable;
    @FXML private TableColumn<Person, String> idCol;
    @FXML private TableColumn<Person, String> firstCol;
    @FXML private TableColumn<Person, String> lastCol;
    @FXML private TableColumn<Person, String> gradYearCol;
    @FXML private TableColumn<Person, String> emailCol;
    @FXML private TableColumn<Person, String> passCol;
    @FXML private TableColumn<Person, String> hoursCol;
    @FXML private TableColumn<Person, String> isAdvisorCol;
    @FXML private TableColumn<Person, String> accID;
    @FXML private TableColumn<Person, String> accGradYear;
    @FXML private TableColumn<Person, String> accEmail;
    @FXML private TableColumn<Person, String> accFirst;
    @FXML private TableColumn<Person, String> accLast;
    @FXML private TableColumn<Person, String> accPass;
    @FXML private TableColumn<Person, String> accHours;
    @FXML private TableColumn<Person, String> accIsAdvisor;
    @FXML private TextField newFirst;
    @FXML private TextField newLast;
    @FXML private TextField newGradYear;
    @FXML private TextField newEmail;
    @FXML private TextField changeFirst;
    @FXML private TextField changeLast;
    @FXML private TextField changeGradYear;
    @FXML private TextField changeEmail;
    @FXML private Label firstErr;
    @FXML private Label lastErr;
    @FXML private Label gradErr;
    @FXML private Label emailErr;
    @FXML private Label err;
    @FXML private Label delErr;
    @FXML private Label firstCErr;
    @FXML private Label lastCErr;
    @FXML private Label gradCErr;
    @FXML private Label emailCErr;
    @FXML private Label changeErr;
    @FXML private Label idCErr;
    @FXML private Label recoverErr;
    @FXML private Label permErr;

    private Connection connection;
    private Statement statement;
    private boolean isAdvisor;

    public AdminController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        setTable();
        setDelTable();

        setBox();
        setDelBox();

    }

    private void setDelTable(){

        String SQL = "SELECT * FROM Deleted";
        ResultSet rs = null;
        List<Person> list = new ArrayList<Person>();
        Person person = null;
        ObservableList<Person> persons = FXCollections.observableArrayList();

        try {

            Connection connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                persons.add(new Person(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }

            this.accID.setCellValueFactory(new PropertyValueFactory<Person, String>("Id"));
            this.accFirst.setCellValueFactory(new PropertyValueFactory<Person, String>("First"));
            this.accLast.setCellValueFactory(new PropertyValueFactory<Person, String>("Last"));
            this.accGradYear.setCellValueFactory(new PropertyValueFactory<Person, String>("GradYear"));
            this.accEmail.setCellValueFactory(new PropertyValueFactory<Person, String>("Email"));
            this.accPass.setCellValueFactory(new PropertyValueFactory<Person, String>("Pass"));
            this.accHours.setCellValueFactory(new PropertyValueFactory<Person, String>("Hours"));
            this.accIsAdvisor.setCellValueFactory(new PropertyValueFactory<Person, String>( "IsAdmin"));

            accID.setCellFactory(TextFieldTableCell.forTableColumn());
            accFirst.setCellFactory(TextFieldTableCell.forTableColumn());
            accLast.setCellFactory(TextFieldTableCell.forTableColumn());
            accGradYear.setCellFactory(TextFieldTableCell.forTableColumn());
            accEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            accPass.setCellFactory(TextFieldTableCell.forTableColumn());
            accHours.setCellFactory(TextFieldTableCell.forTableColumn());
            accIsAdvisor.setCellFactory(TextFieldTableCell.forTableColumn());

            delAccTable.setItems(null);
            delAccTable.setItems(persons);

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            setDelBox();

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }

    }

    private void setTable(){

        String SQL = "SELECT Persons.id, Persons.gradYear, Persons.email, Persons.first, " +
                "Persons.last, Persons.password, Persons.isAdmin, SUM(Hours.hours) " +
                "FROM Persons LEFT JOIN Hours ON Persons.id=Hours.id GROUP BY Persons.id;";
        ResultSet rs = null;
        List<Person> list = new ArrayList<Person>();
        Person person = null;
        ObservableList<Person> persons = FXCollections.observableArrayList();

        try {

            Connection connection = DataConnect.getConnection();
            assert connection != null;
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL);

            while (rs.next()) {

                persons.add(new Person(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }

            this.idCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Id"));
            this.firstCol.setCellValueFactory(new PropertyValueFactory<Person, String>("First"));
            this.lastCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Last"));
            this.gradYearCol.setCellValueFactory(new PropertyValueFactory<Person, String>("GradYear"));
            this.emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Email"));
            this.passCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Pass"));
            this.hoursCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Hours"));
            this.isAdvisorCol.setCellValueFactory(new PropertyValueFactory<Person, String>( "IsAdmin"));

            idCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            gradYearCol.setCellFactory(TextFieldTableCell.forTableColumn());
            emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
            passCol.setCellFactory(TextFieldTableCell.forTableColumn());
            hoursCol.setCellFactory(TextFieldTableCell.forTableColumn());
            isAdvisorCol.setCellFactory(TextFieldTableCell.forTableColumn());

            personTable.setItems(null);
            personTable.setItems(persons);

            setBox();

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    @FXML
    protected void submit(ActionEvent event) throws IOException {

        ResultSet rs = null;
        boolean control = true;

        String first = (String) this.newFirst.getText();
        String last = (String) this.newLast.getText();
        String grad = (String) this.newGradYear.getText();
        String email = (String) this.newEmail.getText();

        if (!isAdvisor) {

            if (grad.length() < 1 ) {

                gradErr.setText("Graduation Year is Required!");
                control = false;
            }
            try {

                int numCheck = Integer.parseInt(grad);
            } catch (NumberFormatException e) {


                gradErr.setText("Need a Numerical Value!");
                control = false;
            }

        }
        if (first.length() < 1) {

            firstErr.setText("First Name is Required!");
            control = false;
        }
        if (last.length() < 1) {

            lastErr.setText("Last Name is Required!");
            control = false;
        }

        if (email.length() < 1) {

            emailErr.setText("Email is Required!");
            control = false;
        }
        if (control) {

            try {

                String id = Student.GenerateID(first, last);
                String pass = Student.GeneratePass();

                String sql = "INSERT INTO Persons VALUES (?,?,?,?,?,?,?);";
                try {

                    connection = DataConnect.getConnection();
                    PreparedStatement insert = connection.prepareStatement(sql);
                    insert.setString(1, id);
                    if (!isAdvisor) {
                        insert.setString(2,grad);
                    } else {
                        insert.setString(2,"");
                    }
                    insert.setString(3, email);
                    insert.setString(4, first);
                    insert.setString(5, last);
                    insert.setString(6, pass);
                    insert.setBoolean(7, isAdvisor);
                    insert.executeUpdate();

                    setTable();
                    showCreds(id, pass);

                } catch (SQLException e) {

                    System.err.println("Error: " + e);
                    System.err.println(e.getSQLState());
                }
            } catch (Exception e){

                System.err.println("Error: " + e);
                err.setText("Error!");
            } finally {

                DataUtil.close(rs);
                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        } else err.setText("Change Needed Fields");
    }

    @FXML
    protected void setStudent(ActionEvent event) {

        isAdvisor = false;
    }

    @FXML
    protected void setAdvisor(ActionEvent event) {

        isAdvisor = true;
    }

    @FXML
    protected void cancel(ActionEvent event) throws IOException{

        newFirst.setText("");
        newLast.setText("");
        newGradYear.setText("");
        newEmail.setText("");
    }
    private void showCreds(String id, String pass) {

        Stage newStage = new Stage();
        StackPane pane = new StackPane();

        GridPane gridPane = new GridPane();
        pane.getChildren().add(gridPane);

        Label idLabel = new Label();
        Label passLabel = new Label();
        Button quit = new Button("Quit");

        quit.setOnAction(event -> newStage.close());

        idLabel.setText("Users ID: " + id);
        passLabel.setText("Users Password: " + pass);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(50,50,50,50));
        gridPane.setVgap(10.0);
        gridPane.add(idLabel, 0, 0);
        gridPane.add(passLabel, 0, 1);
        gridPane.add(quit, 0, 3);

        Scene scene = new Scene(pane, 500, 300);

        newStage.initStyle(StageStyle.UTILITY);
        newStage.setScene(scene);
        newStage.show();
    }

    private void setDelBox() {

        String sql = "SELECT id FROM Deleted;";
        ResultSet rs = null;

        try {

            recoverBox.getItems().clear();
            permanentBox.getItems().clear();

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {

                recoverBox.getItems().add(rs.getString(1));
                permanentBox.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private void setBox() {

        String sql = "SELECT id FROM Persons;";
        ResultSet rs = null;

        try{

            choiceBox.getItems().clear();
            changeBox.getItems().clear();

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while(rs.next()) {

                choiceBox.getItems().add(rs.getString(1));
                changeBox.getItems().add(rs.getString(1));
            }
        } catch (SQLException e){

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private String getAdmin(String id) {

        String sql = "SELECT isAdmin FROM Persons WHERE id='" + id + "';";

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            int isAdmin = statement.executeQuery(sql).getInt(1);

            if (isAdmin == 1) return "true";
            else if (isAdmin == 0) return "false";
            else return "error";

        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.out.println("Error: " + e);
            return "error";
        } finally {

            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    private int getHours(String id) {

        int hours;
        String sql = "SELECT hours FROM Hours WHERE id='" + id + "';";

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();

            try {
                hours = statement.executeQuery(sql).getInt(1);
                return hours;
            } catch (SQLException ignore) { return 0; }
        } catch (SQLException e) {

            System.err.println(e.getStackTrace()[0].getLineNumber());
            System.err.println("Error: " + e);
            return 0;
        } finally {

            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }

    @FXML
    protected void Delete(ActionEvent event) throws IOException {

        String id = (String) choiceBox.getValue();

        String sql;
        String isAdmin;
        int hours;
        int grad;
        String email;
        String first;
        String last;
        String pass;

        ResultSet rs = null;

        try {

            isAdmin = getAdmin(id);
            if (isAdmin.equals("error")) delErr.setText("Error Deleting User!");

            else {

                if (isAdmin.equals("true")) {

                    grad = 0;
                    sql = "SELECT email, first, last, password FROM Persons WHERE id='" + id + "';";

                    connection = DataConnect.getConnection();
                    statement = connection.createStatement();
                    rs = statement.executeQuery(sql);

                    email = rs.getString(1);
                    first = rs.getString(2);
                    last = rs.getString(3);
                    pass = rs.getString(4);

                    DataUtil.close(rs);

                    sql = "INSERT INTO Deleted VALUES ('" + id + "', 0, '" + email + "', '"
                            + first + "', '" + last + "', '" + pass + "', true, 0);";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignored) {
                    }

                    sql = "SELECT id FROM Deleted WHERE id='" + id + "';";
                    try {
                        statement.executeQuery(sql);

                        sql = "DELETE FROM Persons WHERE id='" + id + "';";
                        try {
                            statement.executeQuery(sql);
                        } catch (SQLException ignored) {
                        }

                    } catch (SQLException ignore) {
                        delErr.setText("Error Deleting User!");
                    }
                } else if (isAdmin.equals("false")) {

                    hours = getHours(id);

                    sql = "SELECT grad, email, first, last, password FROM Persons WHERE id='" + id + "';";

                    connection = DataConnect.getConnection();
                    statement = connection.createStatement();
                    rs = statement.executeQuery(sql);

                    grad = rs.getInt(1);
                    email = rs.getString(2);
                    first = rs.getString(3);
                    last = rs.getString(4);
                    pass = rs.getString(5);

                    DataUtil.close(rs);

                    sql = "INSERT INTO Deleted VALUES ('" + id + "', '" + grad + "', '" + email + "', '" +
                            first + "', '" + last + "', '" + pass + "', false, " + hours + ");";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignored) {}

                    // Checks that user was moved to the deleted database before actually deleting data
                    sql = "SELECT * FROM Deleted WHERE id='" + id + "';";

                    try {
                        statement.executeQuery(sql);
                        sql = "DELETE FROM Persons WHERE id='" + id + "';";

                        try {
                            statement.executeQuery(sql);
                        } catch (SQLException ignore) {}

                        if (hours > 0) {
                            sql = "DELETE FROM Hours WHERE id='" + id + "';";

                            try {
                                statement.executeQuery(sql);
                            } catch(SQLException ignore){}
                        }
                    } catch (SQLException ignore) {

                        delErr.setText("Error Deleting User!");
                    }
                }
            }
        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        } finally {

            setTable();
            setDelTable();

            DataUtil.close(rs);
            DataUtil.close(statement);
            DataUtil.close(connection);
        }
    }
    @FXML
    protected void Change(ActionEvent event) {

        gradCErr.setText("");
        idCErr.setText("");
        changeErr.setText("");

        String id = (String) changeBox.getValue();
        String first = changeFirst.getText();
        String last = changeLast.getText();
        String grad = changeGradYear.getText();
        String email = changeEmail.getText();
        String sql;

        if (id != null) {
            try {

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                if (first.length() > 0) {

                    sql = "UPDATE Persons SET first='" + first + "' WHERE id='" + id + "';";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignore) {
                    }
                }
                if (last.length() > 0) {

                    sql = "UPDATE Persons SET last='" + last + "' WHERE id='" + id + "';";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignore) {
                    }
                }
                if (grad.length() > 0) {

                    try {

                        int numCheck = Integer.parseInt(grad);

                        sql = "UPDATE Persons SET gradYear='" + grad + "' WHERE id='" + id + "';";

                        try {
                            statement.executeQuery(sql);
                        } catch (SQLException ignore) {}

                    } catch (NumberFormatException e) {

                        gradCErr.setText("Need Numerical Value!");

                        changeErr.setText("Change Needed Fields!");
                    }
                }
                if (email.length() > 0) {

                    sql = "UPDATE Persons SET email='" + email + "' WHERE id='" + id + "';";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignore) {}
                }
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {

                DataUtil.close(statement);
                DataUtil.close(connection);

                setTable();
                setBox();
            }
        } else {

            idCErr.setText("Need to Select User");

            changeErr.setText("Change Needed Fields!");
        }
    }
    /*
    @FXML
    protected void Autofill(ActionEvent event) {

        String id = changeBox.getValue().toString();

        String sql = "SELECT * FROM Persons WHERE id='" + id + "';";
        ResultSet rs = null;

        try {

            connection = DataConnect.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            changeGrade.setText(rs.getString(2));
            changeEmail.setText(rs.getString(3));
            changeFirst.setText(rs.getString(4));
            changeLast.setText(rs.getString(5));

        } catch (SQLException e) {

            System.out.println("Error: " + e);
            System.err.println(e.getStackTrace()[0].getLineNumber());
        }
    }
     */
    @FXML
    protected void changeCancel(ActionEvent event) {

        changeBox.getItems().clear();
        setBox();

        changeFirst.setText("");
        changeLast.setText("");
        changeGradYear.setText("");
        changeEmail.setText("");
    }

    @FXML
    protected void recover(ActionEvent event) {

        recoverErr.setText("");

        String id = (String) recoverBox.getValue();
        ResultSet rs = null;
        String sql;

        if (id != null) {

            try {

                sql = "SELECT * FROM Deleted";

                connection = DataConnect.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(sql);

                int gradYear = rs.getInt(2);
                String email = rs.getString(3);
                String first = rs.getString(3);
                String last = rs.getString(4);
                String pass = rs.getString(5);
                boolean isAdmin = rs.getBoolean(6);
                int hours = rs.getInt(7);

                DataUtil.close(rs);

                sql = "INSERT INTO Persons VALUES ('" + id + "', " + gradYear + ", '" + email +
                        "', '" + first + "', '" + last + "', '" + pass + "', " + isAdmin + ");";

                try {
                    statement.executeQuery(sql);
                } catch (SQLException ignore) {}

                if (!isAdmin || hours > 0) {

                    sql = "INSERT INTO Hours VALUES('" + id + "', " + hours + ", '1999-9-9');";

                    try {
                        statement.executeQuery(sql);
                    } catch (SQLException ignore) {
                    }
                }

                sql = "DELETE FROM Deleted WHERE id='" + id + "';";

                try {
                    statement.executeQuery(sql);
                } catch (SQLException ignore) {}
            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {

                DataUtil.close(statement);
                DataUtil.close(connection);

                setTable();
                setDelTable();
            }
        } else {

            recoverErr.setText("Select Account!");
        }
    }

    @FXML
    protected void permDelete(ActionEvent event) {

        permErr.setText("");

        String id = (String) permanentBox.getValue();
        String sql;

        if (id != null) {

            try {

                sql = "DELETE FROM Deleted WHERE id='" + id + "';";

                connection = DataConnect.getConnection();
                statement = connection.createStatement();

                try { statement.executeQuery(sql); }
                catch (SQLException ignore) {}

            } catch (SQLException e) {

                System.err.println(e.getStackTrace()[0].getLineNumber());
                System.out.println("Error: " + e);
            } finally {

                setTable();
                setDelTable();

                DataUtil.close(statement);
                DataUtil.close(connection);
            }
        } else {

            permErr.setText("Select Account!");
        }
    }
}
