package AccountTypes.Admin.AdminGUI;

import AccountTypes.Admin.AdminDataManager;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.DataViewer;
import Data.Item.Item;
import Data.Item.ItemType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.dialog.ExceptionDialog;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * Sad Boi hours
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUIController implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView banner;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField nameField, balanceField, usernameField,
            passwordField, priceField, imageURLField,  itemNameField;
    @FXML
    private ChoiceBox<EmployeeType> employeeTypes;
    @FXML
    private ChoiceBox<ItemType> itemTypes;
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> employeeID, username, password, accountType;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperID, name, balance;
    @FXML
    private TableView<Item> itemTableView;
    @FXML
    private TableColumn<Item, String> itemID, itemName, price, imageURL, category;
    private AdminDataManager adminDataManager = new AdminDataManager();
    private DataViewer[] dataViewers = new DataViewer[3];
    boolean isDeletingData = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        banner.fitWidthProperty().bind(mainPane.widthProperty());
        setCellValueFactories();
        setDataViewers();
        tryToPopulateAll();
        setChoiceBoxes();
    }


    private void setCellValueFactories() {
        setEmployeeColumns();
        setCamperColumns();
        setItemColumns();
    }

    private void setDataViewers(){
        dataViewers = new DataViewer[]{
                new DataViewer(camperTableView, "camper"),
                new DataViewer(itemTableView, "item"),
                new DataViewer(employeeTableView, "employee")
        };
    }

    private void tryToPopulateAll(){
        try {
            for (DataViewer dataViewer: dataViewers)
                adminDataManager.retrieveDatabaseData(dataViewer);
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void setChoiceBoxes() {
        for (EmployeeType accountType : EmployeeType.values())
            employeeTypes.getItems().add(accountType);
        for (ItemType itemType : ItemType.values())
            itemTypes.getItems().add(itemType);
    }

    @FXML
    private void tableViewClickListener(MouseEvent e) {
        String tableView = ((TableView) e.getSource()).getId();
        switch (tableView) {
            case "camperTableView":
                setCamperFields();
                break;
            case "itemTableView":
                setItemFields();
                break;
            case "employeeTableView":
                setEmployeeFields();
                break;
        }
    }

    private void setEmployeeFields() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
        usernameField.setText(employee.getUsername());
        passwordField.setText("");
        employeeTypes.setValue(employee.getEmployeeType());
    }

    private void setItemFields(){
        Item item = itemTableView.getSelectionModel().getSelectedItem();
        itemNameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        imageURLField.setText(item.getImageURL());
        itemTypes.setValue(item.getItemType());
    }

    private void setCamperFields() {
        Camper camper = camperTableView.getSelectionModel().getSelectedItem();
        nameField.setText(camper.getName());
        balanceField.setText(String.valueOf(camper.getBalance()));
    }

    @FXML
    private void clearSelectionsOnClick() {
        if (!isTableNotSelected(employeeTableView) || !isTableNotSelected(camperTableView) || !isTableNotSelected(itemTableView)) {
            for (DataViewer dataViewer: dataViewers) {
                TableView tableView = (TableView) dataViewer.getNode();
                tableView.getSelectionModel().clearSelection();
            }
            clearFields();
        }
    }

    @FXML
    private void keyListener(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.BACK_SPACE) {
            isDeletingData = keyEvent.getCode() != KeyCode.ENTER;
            tryToUpdate();
        }
    }

    @FXML
    private void buttonListener(ActionEvent event) {
        String buttonText = ((Button) event.getSource()).getText();
        isDeletingData = !buttonText.equals("Update");
        tryToUpdate();
    }

    private void tryToUpdate(){
        try {
            update();
        } catch (Exception exception) {
            new ExceptionDialog(exception).showAndWait();
        }
    }

    private void update() throws SQLException {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        switch (tab.getText()) {
            case "Campers":
                updateCamper();
                nameField.requestFocus();
                adminDataManager.retrieveDatabaseData(dataViewers[0]);
                break;
            case "Items":
                updateItem();
                itemNameField.requestFocus();
                adminDataManager.retrieveDatabaseData(dataViewers[1]);
                break;
            case "Employees":
                updateEmployee();
                usernameField.requestFocus();
                adminDataManager.retrieveDatabaseData(dataViewers[2]);
                break;
        }
        clearFields();
    }

    private void updateCamper() throws SQLException {
        if (isTableNotSelected(camperTableView) && !isDeletingData) {
            adminDataManager.addToCamperTableQuery(new Camper(new Random().nextInt(99999), nameField.getText(),
                    Integer.parseInt(balanceField.getText())));
        } else {
            Camper camper = camperTableView.getSelectionModel().getSelectedItem();
            if (isDeletingData)
                adminDataManager.tryToDeleteRow("camper", camper.getId());
             else {
                camper.setName(nameField.getText());
                camper.setBalance(Integer.parseInt(balanceField.getText()));
            }

        }
    }

    private void updateItem() throws SQLException {
        if (isTableNotSelected(itemTableView)) {
            adminDataManager.addToItemTableQuery(new Item(new Random().nextInt(999999), itemNameField.getText(),
                    Integer.parseInt(priceField.getText()), imageURLField.getText(), itemTypes.getValue()));
        } else {
            Item item = itemTableView.getSelectionModel().getSelectedItem();
            if (isDeletingData)
                adminDataManager.tryToDeleteRow("item", item.getId());
            else {
                item.setName(itemNameField.getText());
                item.setPrice(Integer.parseInt(priceField.getText()));
                item.setImageURL(imageURLField.getText());
                item.setItemType(itemTypes.getValue());
            }
        }
    }


    private void updateEmployee() throws SQLException {
        if (isTableNotSelected(employeeTableView)) {
            adminDataManager.addToEmployeeTableQuery(new Employee(new Random().nextInt(999999),
                    usernameField.getText(), passwordField.getText(), employeeTypes.getValue()));
        } else {
            Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
            employee.requestPermissionToModifyEmployees();
            if (isDeletingData)
                adminDataManager.tryToDeleteRow("employee", employee.getId());
            else{
                employee.setUsername(usernameField.getText());
                employee.setPassword(passwordField.getText());
                employee.setEmployeeType(employeeTypes.getValue());
            }

        }
    }

    private boolean isTableNotSelected(TableView tableView) {
        return tableView.getSelectionModel().getSelectedItem() == null;
    }

    private void clearFields() {
        clearCamperFields();
        clearItemFields();
        clearEmployeeFields();
    }

    private void clearCamperFields() {
        nameField.setText("");
        balanceField.setText("");
    }

    private void clearItemFields(){
        itemNameField.setText("");
        priceField.setText("");
        imageURLField.setText("");
        itemTypes.setValue(null);
    }

    private void clearEmployeeFields() {
        usernameField.setText("");
        passwordField.setText("");
        employeeTypes.setValue(null);
    }

    private void setEmployeeColumns() {
        employeeID.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountType.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
    }

    private void setCamperColumns() {
        camperID.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    private void setItemColumns(){
        itemID.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        imageURL.setCellValueFactory(new PropertyValueFactory<>("imageURL"));
        category.setCellValueFactory(new PropertyValueFactory<>("itemType"));
    }
}