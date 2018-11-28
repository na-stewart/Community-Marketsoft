package AccountTypes.Admin.AdminGUI;

import AccountTypes.AccountType;
import AccountTypes.Admin.AdminPanel;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Manager.DatabaseViewer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUIController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField nameField, balanceField;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private ChoiceBox accountTypes, consumableType;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> employeeID, username, password, accountType;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperID, name, balance;
    private AdminPanel adminPanel = new AdminPanel();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    setCellValueFactories();
    tryToPopulateAll();
}

    private void tryToPopulateAll(){
        try {
            populateAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tableViewListener(MouseEvent e){
        String tableView = ((TableView) e.getSource()).getId();
        if (e.getClickCount() == 2) {
            switch (tableView) {
                case "employeeTableView":
                    setEmployeeFields(employeeTableView.getSelectionModel().getSelectedItem());
                    break;
                case "camperTableView":
                    setCamperFields(camperTableView.getSelectionModel().getSelectedItem());
                    break;

            }
        }

    }

    private void setEmployeeFields(Employee employee){
        usernameField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
    }

    private void setCamperFields(Camper camper){
        nameField.setText(camper.getName());
        balanceField.setText(String.valueOf(camper.getBalance()));
    }

    private void populateAll() throws SQLException {
        adminPanel.retrieveDatabaseData("SELECT * FROM camper", new DatabaseViewer(camperTableView, "camper"));
        adminPanel.retrieveDatabaseData("SELECT * FROM employee", new DatabaseViewer(employeeTableView, "employee"));
    }

    private void setCellValueFactories() {
        setEmployeeColumns();
        setCamperColumns();
    }

    private void setEmployeeColumns(){
        employeeID.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
    }

    private void setCamperColumns(){
        camperID.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }
}
