package App.Admin.AdminGUI;

import App.Admin.AdminDataManager;
import Data.Users.Camper;
import Data.Users.Employee;
import Data.Users.EmployeeType;
import Data.DataViewer;
import Data.Item.Item;
import Data.Item.ItemType;
import GUILoader.GUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author Aidan Stewart
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
            passwordField, priceField, quantityField, imageURLField,  itemNameField;
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
    private TableColumn<Item, String> itemID, itemName, price, quantity, imageURL, category;
    private AdminDataManager adminDataManager = new AdminDataManager();
    private DataViewer[] dataViewers = new DataViewer[3];
    private boolean isDeletingData = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        banner.fitWidthProperty().bind(mainPane.widthProperty());
        setCellValueFactories();
        setDataViewers();
        autoRefreshTable();
        setChoiceBoxes();
    }


    private void setCellValueFactories() {
        setEmployeeColumns();
        setCamperColumns();
        setItemColumns();
    }

    private void setDataViewers(){
        dataViewers = new DataViewer[]{
                new DataViewer(camperTableView, "SELECT * FROM camper"),
                new DataViewer(itemTableView, "SELECT * FROM item"),
                new DataViewer(employeeTableView, "SELECT * FROM employee")
        };
    }

    private void autoRefreshTable(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tryToPopulateAll();
            }
        };

        new Timer().schedule(task, 0, 3000);
    }

    private void tryToPopulateAll(){
        try {
            if (isNothingSelected()) {
                for (DataViewer dataViewer : dataViewers)
                    adminDataManager.retrieveDatabaseData(dataViewer);
            }
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

    private void setCamperFields() {
        if (oneItemSelected(camperTableView)) {
            Camper camper = camperTableView.getSelectionModel().getSelectedItem();
            nameField.setText(camper.getName());
            balanceField.setText(String.valueOf(camper.getBalance()));
        } else
            clearCamperFields();
    }

    private void setItemFields() {
        if (oneItemSelected(itemTableView)) {
            Item item = itemTableView.getSelectionModel().getSelectedItem();
            itemNameField.setText(item.getName());
            priceField.setText(String.valueOf(item.getPrice()));
            quantityField.setText(String.valueOf(item.getQuantity()));
            imageURLField.setText(item.getImageURL());
            itemTypes.setValue(item.getItemType());
        } else
            clearItemFields();
    }


    private void setEmployeeFields() {
        if (oneItemSelected(employeeTableView)) {
            Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
            usernameField.setText(employee.getUsername());
            passwordField.setText("");
            employeeTypes.setValue(employee.getEmployeeType());
        } else
            clearEmployeeFields();
    }

    private boolean oneItemSelected(TableView tableView){
        return tableView.getSelectionModel().getSelectedItems().size() == 1;
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
        quantityField.setText("");
        itemTypes.setValue(null);
    }

    private void clearEmployeeFields() {
        usernameField.setText("");
        passwordField.setText("");
        employeeTypes.setValue(null);
    }



    @FXML
    private void clearSelectionsOnClick() {
        if (!isNothingSelected()) {
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
        if (buttonText.equals("Logout"))
            new GUI("App/Login/LoginGUI/LoginGUI.fxml");
        else {
            isDeletingData = !buttonText.equals("Update");
            tryToUpdate();
        }
    }

    @FXML
    private void imageLink(){
        try {
            java.awt.Desktop.getDesktop().browse(new URI(imageURLField.getText()));
        } catch (IOException | URISyntaxException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void tryToUpdate(){
        try {
            update();
        } catch (Exception e) {
            new ExceptionDialog(e).showAndWait();
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

    //Intergers cant be checked as empty in setters so its checked in update method instead.

    private void updateCamper() throws SQLException {
        if (isNothingSelected() && !isDeletingData) {
            adminDataManager.addToCamperTableQuery(new Camper(new Random().nextInt(99999), nameField.getText(),
                    Integer.parseInt(balanceField.getText())));
        } else {
            ObservableList<Camper> selectedCampers = camperTableView.getSelectionModel().getSelectedItems();
            for (Camper camper : selectedCampers) {
                if (isDeletingData)
                    adminDataManager.tryToDeleteRow("camper", camper.getId());
                else {
                    camper.setName(nameField.getText());
                    if (!balanceField.getText().isEmpty())
                        camper.setBalance(Integer.parseInt(balanceField.getText()));
                }
            }
        }
    }

    private void updateItem() throws SQLException {
        if (isNothingSelected()) {
            adminDataManager.addToItemTableQuery(new Item(new Random().nextInt(999999), itemNameField.getText(),
                    Integer.parseInt(priceField.getText()), Integer.parseInt(quantityField.getText()),
                    imageURLField.getText(), itemTypes.getValue()));
        } else {
            ObservableList<Item> selectedItems = itemTableView.getSelectionModel().getSelectedItems();
            for (Item item : selectedItems) {
                if (isDeletingData)
                    adminDataManager.tryToDeleteRow("item", item.getId());
                else {
                    item.setName(itemNameField.getText());
                    item.setImageURL(imageURLField.getText());
                    item.setItemType(itemTypes.getValue());
                    if (!priceField.getText().isEmpty())
                        item.setPrice(Integer.parseInt(priceField.getText()));
                    if (!quantityField.getText().isEmpty())
                        item.setQuantity(Integer.parseInt(quantityField.getText()));
                }
            }
        }
    }

    private void updateEmployee() throws SQLException {
        if (isNothingSelected()) {
            adminDataManager.addToEmployeeTableQuery(new Employee(new Random().nextInt(999999),
                    usernameField.getText(), passwordField.getText(), employeeTypes.getValue()));
        } else {
            ObservableList<Employee> selectedItems = employeeTableView.getSelectionModel().getSelectedItems();
            for (Employee employee : selectedItems) {
                employee.requestPermissionToModifyEmployees();
                if (isDeletingData)
                    adminDataManager.tryToDeleteRow("employee", employee.getId());
                else {
                    employee.setUsername(usernameField.getText());
                    employee.setEmployeeType(employeeTypes.getValue());
                    employee.setPassword(passwordField.getText());
                }
            }
        }
    }

    private boolean isNothingSelected() {
        return camperTableView.getSelectionModel().getSelectedItem() == null &&
                itemTableView.getSelectionModel().getSelectedItem() == null &&
                employeeTableView.getSelectionModel().getSelectedItem() == null;
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
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        imageURL.setCellValueFactory(new PropertyValueFactory<>("imageURL"));
        category.setCellValueFactory(new PropertyValueFactory<>("itemType"));
    }
}