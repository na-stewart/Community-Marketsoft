package AccountTypes.Admin.AdminGUI;

import AccountTypes.AccountTypes;
import AccountTypes.Admin.AdminPanel;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.ID;
import Manager.DatabaseViewer;
import PassProtection.PassHash;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.event.KeyListener;
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
    private ChoiceBox<AccountTypes> accountTypes;
    @FXML
    private ChoiceBox itemTypes;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> employeeID, username, password, accountType;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperID, name, balance;
    private AdminPanel adminPanel = new AdminPanel();
    private PassHash passHash = new PassHash();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();
        tryToPopulateAll();
        setChoiceBoxes();
    }

    private void tryToPopulateAll(){
        try {
            adminPanel.retrieveDatabaseData("SELECT * FROM camper", new DatabaseViewer(camperTableView, "camper"));
            adminPanel.retrieveDatabaseData("SELECT * FROM employee", new DatabaseViewer(employeeTableView, "employee"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tableViewClickListener(MouseEvent e) {
        String tableView = ((TableView) e.getSource()).getId();
        switch (tableView) {
            case "employeeTableView":
                setEmployeeFields();
                break;
            case "camperTableView":
                setCamperFields();
                break;
        }
    }

    private void setEmployeeFields(){
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
        usernameField.setText(employee.getUsername());
        passwordField.setText("");
        accountTypes.setValue(employee.getAccountType());
    }

    private void setCamperFields(){
        Camper camper = camperTableView.getSelectionModel().getSelectedItem();
        nameField.setText(camper.getName());
        balanceField.setText(String.valueOf(camper.getBalance()));
    }


    @FXML
    private void buttonListener(ActionEvent e) throws SQLException {
        String buttonText = ((Button) e.getSource()).getText();
        if (buttonText.equals("Update"))
            determineHowToUpdate();
    }

    private void determineHowToUpdate() throws SQLException {
        if (canAddToTable())
            addToTable();
        else
            editTable();
    }

    private boolean canAddToTable(){
        return employeeTableView.getSelectionModel().getSelectedItem() == null &&
                camperTableView.getSelectionModel().getSelectedItem() == null;
    }

    private void addToTable() throws SQLException {
        int tabPaneIndex = tabPane.getSelectionModel().getSelectedIndex();
        switch (tabPaneIndex){
            case 0:
                addToCamperTable();
                break;
            case 2:
                addToEmployeeTable();
                break;
        }
    }

    private void addToCamperTable() throws SQLException {
        String query = "INSERT INTO camper VALUES('" + new ID().getId() + "','" +
                nameField.getText() + "','" +
                balanceField.getText() + "')";
        adminPanel.updateDatabase(query);
        adminPanel.retrieveDatabaseData("SELECT * FROM camper", new DatabaseViewer(camperTableView, "camper"));
    }

    private void addToEmployeeTable() throws SQLException {
        int accountType = AccountTypes.accountTypePermToInt(accountTypes.getSelectionModel().getSelectedItem());
        String query = "INSERT INTO employee VALUES('" + new ID().getId() + "','" +
                usernameField.getText() + "','" +
                passHash.tryToGetSaltedHash(passwordField.getText())+ "','" +
                accountType + "')";
        adminPanel.updateDatabase(query);
        adminPanel.retrieveDatabaseData("SELECT * FROM employee", new DatabaseViewer(employeeTableView, "employee"));
    }

    private void editTable() throws SQLException {
        int tabPaneIndex = tabPane.getSelectionModel().getSelectedIndex();
        switch (tabPaneIndex){
            case 0:
                editCamperRow();
                break;
            case 2:
                editEmployeeRow();
                break;
        }
    }

    private void editEmployeeRow() throws SQLException {
        int accountType = AccountTypes.accountTypePermToInt(accountTypes.getSelectionModel().getSelectedItem());
        int id = employeeTableView.getSelectionModel().getSelectedItem().getId();
        String query = "UPDATE employee SET " +
                "username = '" + usernameField.getText() + "',"+
                "password = '" + passHash.tryToGetSaltedHash(passwordField.getText()) + "'," +
                "accounttype = '" + accountType + "' " +
                "WHERE idaccounts = "+ id +";";
        adminPanel.updateDatabase(query);
        adminPanel.retrieveDatabaseData("SELECT * FROM employee", new DatabaseViewer(employeeTableView, "employee"));
    }

    private void editCamperRow() throws SQLException {
        int id = camperTableView.getSelectionModel().getSelectedItem().getId();
        String query = "UPDATE camper SET " +
                "name = '" + nameField.getText() + "',"+
                "balance = '" + balanceField.getText() + "' " +
                "WHERE idcamper = "+ id +";";
        adminPanel.updateDatabase(query);
        adminPanel.retrieveDatabaseData("SELECT * FROM employee", new DatabaseViewer(employeeTableView, "employee"));
    }

    private void setChoiceBoxes(){
        for (AccountTypes accountType: AccountTypes.values())
            accountTypes.getItems().add(accountType);
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
