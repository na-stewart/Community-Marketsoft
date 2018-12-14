package AccountTypes.Admin.AdminGUI;

import Data.Customers.EmployeeType;
import AccountTypes.Admin.AdminDataManager;
import Data.Item.Item;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Item.ItemType;
import Manager.DataViewer;
import Security.PassHash;
import Tables.TableType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private PassHash passHash = new PassHash();
    private DataViewer[] dataViewers = new DataViewer[3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        banner.fitWidthProperty().bind(mainPane.widthProperty());
        setCellValueFactories();
        tryToPopulateAll();
        setChoiceBoxes();
    }

    private void setTableViewers(){
        dataViewers[0] = new DataViewer(TableType.CAMPER, camperTableView);
        dataViewers[1] = new DataViewer(TableType.ITEM, itemTableView);
        dataViewers[2] = new DataViewer(TableType.EMPLOYEE, employeeTableView);
    }

    private void tryToPopulateAll() {
        setTableViewers();
        try {
            for (DataViewer dataViewer: dataViewers)
                adminDataManager.retrieveDatabaseData(dataViewer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearSelectionsOnClick() {
        if (!isTableRowNotSelected(employeeTableView) || !isTableRowNotSelected(camperTableView) || !isTableRowNotSelected(itemTableView)) {
            employeeTableView.getSelectionModel().clearSelection();
            itemTableView.getSelectionModel().clearSelection();
            camperTableView.getSelectionModel().clearSelection();
            clearFields();
        }
    }

    @FXML
    private void tableViewClickListener(MouseEvent e) throws SQLException {
        String tableView = ((TableView) e.getSource()).getId();
        switch (tableView) {
            case "camperTableView":
                setCamperFields();
                if (e.getClickCount() == 2) {
                    int cmpID = camperTableView.getSelectionModel().getSelectedItem().getId();
                    deleteRow(dataViewers[0], cmpID);
                }
                break;
            case "itemTableView":
                setItemFields();
                if (e.getClickCount() == 2) {
                    int itemId = itemTableView.getSelectionModel().getSelectedItem().getId();
                    deleteRow(dataViewers[1], itemId);
                }
                break;
            case "employeeTableView":
                setEmployeeFields();
                if (e.getClickCount() == 2) {
                    int empID = employeeTableView.getSelectionModel().getSelectedItem().getId();
                    deleteRow(dataViewers[2], empID);
                }
                break;
        }
    }

    private void setEmployeeFields() {
        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
        usernameField.setText(employee.getUsername());
        passwordField.setText("");
        employeeTypes.setValue(employee.getAccountType());
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

    private void deleteRow(DataViewer dataViewer, int id) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete this row?");
        ButtonType buttonTypeOne = new ButtonType("Delete");
        alert.getButtonTypes().setAll(buttonTypeOne);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            String query = "DELETE FROM " + dataViewer.getTable().name().toLowerCase() + " WHERE id = '" + id + "'";
            adminDataManager.updateDatabase(query);
            adminDataManager.retrieveDatabaseData(dataViewer);
            clearFields();
        }
    }

    @FXML
    private void keyListener(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            update();
    }

    @FXML
    private void update() throws SQLException {
        int tabPaneIndex = tabPane.getSelectionModel().getSelectedIndex();
        switch (tabPaneIndex) {
            case 0:
                updateCamper();
                adminDataManager.retrieveDatabaseData(dataViewers[0]);
                break;
            case 1:
                updateItem();
                adminDataManager.retrieveDatabaseData(dataViewers[1]);
                break;
            case 2:
                updateEmployee();
                adminDataManager.retrieveDatabaseData(dataViewers[2]);
                break;

        }
        clearFields();
    }

    private void updateCamper() throws SQLException {
        String[] fields = new String[]{
                nameField.getText(),
                balanceField.getText() };
        if (isTableRowNotSelected(camperTableView))
            adminDataManager.updateDatabase(adminDataManager.addToCamperTableQuery(fields));
        else {
            int id = camperTableView.getSelectionModel().getSelectedItem().getId();
            adminDataManager.updateDatabase(adminDataManager.editCamperTableQuery(id, fields));
        }
        nameField.requestFocus();
    }

    private void updateItem() throws SQLException {
        int itemType = ItemType.itemTypeToInt(itemTypes.getValue());
        String[] fields = new String[]{
                itemNameField.getText(),
                priceField.getText(),
                imageURLField.getText(),
                String.valueOf(itemType)
        };
        if (isTableRowNotSelected(itemTableView))
            adminDataManager.updateDatabase(adminDataManager.addToItemTableQuery(fields));
        else {
            int id = itemTableView.getSelectionModel().getSelectedItem().getId();
            adminDataManager.updateDatabase(adminDataManager.editItemTableQuery(id, fields));
        }
        itemNameField.requestFocus();
    }

    private void updateEmployee() throws SQLException {
        int employeeType = EmployeeType.employeeTypeToInt(employeeTypes.getValue());
        String fields[] = new String[]{
                usernameField.getText(),
                passHash.tryToGetSaltedHash(passwordField.getText()),
                String.valueOf(employeeType)
        };
        if (isTableRowNotSelected(employeeTableView))
            adminDataManager.updateDatabase(adminDataManager.addToEmployeeTableQuery(fields));
        else {
            int id = employeeTableView.getSelectionModel().getSelectedItem().getId();
            adminDataManager.updateDatabase(adminDataManager.editEmployeeTableQuery(id, fields));
        }
        usernameField.requestFocus();
    }

    private boolean isTableRowNotSelected(TableView tableView) {
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

    private void setChoiceBoxes() {
        for (EmployeeType accountType : EmployeeType.values())
            employeeTypes.getItems().add(accountType);
        for (ItemType itemType : ItemType.values())
            itemTypes.getItems().add(itemType);
    }

    private void setCellValueFactories() {
        setEmployeeColumns();
        setCamperColumns();
        setItemColumns();
    }

    private void setEmployeeColumns() {
        employeeID.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
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