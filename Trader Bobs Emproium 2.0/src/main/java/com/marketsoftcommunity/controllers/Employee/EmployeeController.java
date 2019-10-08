package main.java.com.marketsoftcommunity.controllers.Employee;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.consumers.GeneralDTO;
import main.java.com.marketsoftcommunity.consumers.ItemDTO;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.InitGUI;
import main.java.com.marketsoftcommunity.model.*;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.model.logging.AccountActivity;
import main.java.com.marketsoftcommunity.model.logging.ActivityType;
import main.java.com.marketsoftcommunity.model.logging.PurchasesActivity;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.Util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019Aid
 * Copyright (c)
 * All rights reserved.
 */


public class EmployeeController implements InitGUI {

    private final GeneralDTO<Customer> customerDTO = new GeneralDTO<>("customer", Customer.class);
    private final ItemDTO itemDTO = new ItemDTO();
    private final GeneralDTO<Account> accountDTO = new GeneralDTO<>("account", Account.class);
    private final GeneralDTO<AccountActivity> accountActivityLogger = new GeneralDTO<>("accountactivity", AccountActivity.class);
    private final GeneralDTO<PurchasesActivity> purchasesActivityLogger = new GeneralDTO<>("purchasesactivity", PurchasesActivity.class);
    private EmployeePanel[] panels;
    @FXML
    private ImageView logoutImage;
    @FXML
    private ImageView fullScreenImage;
    @FXML
    private AnchorPane itemsAnchorPane;
    @FXML
    private ImageView itemsCancel;
    @FXML
    private TableView<Item> itemsTableView;
    @FXML
    private TableColumn<Item, String> itemsIdColumn, itemsNameColumn, itemsPriceColumn, itemsQuantityColumn,
            imageURLColumn, itemTypeColumn;
    @FXML
    private TextField itemNameField, itemPriceField, itemQuantityField, itemImageUrlField, itemCategoryField;
    @FXML
    private ChoiceBox<ItemCategory> itemTypeChoiceBox;
    @FXML
    private Button itemAdd, itemUpdate, itemDelete;
    @FXML
    private AnchorPane customersAnchorPane;
    @FXML
    private ImageView customersCancel;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerIdColumn, customerNameColumn, balanceColumn, dailyLimitColumn;
    @FXML
    private TextField customerNameField, customerBalanceField, customerDailyLimitField;
    @FXML
    private Button customerAdd, customerUpdate, customerDelete;
    @FXML
    private AnchorPane accountsAnchorPane;
    @FXML
    private ImageView accountsCancel;
    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, String> accountIdColumn, usernameColumn, passwordColumn, accountRoleColumn;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private ChoiceBox<AccountRole> accountRoleChoiceBox;
    @FXML
    private Button accountAdd, accountUpdate, accountDelete;
    @FXML
    private TabPane logsTabPane;
    @FXML
    private AnchorPane accountActivityAnchorPane;
    @FXML
    private ImageView activityCancel;
    @FXML
    private TableView<AccountActivity> accountActivityLoggerTableView;
    @FXML
    private TableColumn<AccountActivity, String> activityIdColumn, activityUsernameColumn, activityActivityTypeColumn,
            activityAffectedIdColumn, activityAffectedNameColumn, activityDateTimeColumn;
    @FXML
    private TextField activityUsernameField, affectedIdField, affectedNameField, activityDateTimeField;
    @FXML
    private ChoiceBox<ActivityType> activityTypeChoiceBox;
    @FXML
    private Button activityAdd, activityUpdate, activityDelete;
    @FXML
    private AnchorPane purchasesAnchorPane;
    @FXML
    private ImageView purchasesCancel;
    @FXML
    private TableView<PurchasesActivity> purchasesActivityLoggerTableView;
    @FXML
    private TableColumn<PurchasesActivity, String> purchasesLoggerIdColumn, purchasesLoggerCustomerNameColumn,
            purchasesLoggerItemIdColumn, purchasesLoggerItemNameColumn, purchasesLoggerItemTypeColumn, purchasesLoggerDateColumn;
    @FXML
    private TextField purchasesCustomerNameField, purchasesItemIdField, purchasesItemNameField, purchasesDateField;
    @FXML
    private ChoiceBox<ItemCategory> purchasesItemTypeChoiceBox;
    @FXML
    private Button purchasesAdd, purchasesUpdate, purchasesDelete;
    @FXML
    private AnchorPane helpAnchorPane;
    @FXML
    private WebView helpWebView;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactory();
        panels = new EmployeePanel[]{new EmployeePanel(customersAnchorPane, "Customers", "customer"),
                new EmployeePanel(accountsAnchorPane, "Accounts", "account"), new EmployeePanel(logsTabPane, "Logs"),
                new EmployeePanel(itemsAnchorPane, "Items", "item"), new EmployeePanel(helpAnchorPane, "Help")};
        loadPanelsChoiceBox();
        helpWebView.getEngine().load("https://www.communitymarketsoft.com");
        loadDataManagerEventHandlers();
        disableAccountActivityPanelForNonAdmins();
        setTableSelectionMethods();
    }

    private void setTableSelectionMethods() {
        customerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        purchasesActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadDataManagerEventHandlers() {
        createItemsPanelEventHandler();
        createCustomerPanelEventHandler();
        createPurchasesActivityPanelEventHandler();
        createAccountPanelEventHandler();
        createAccountActivitiesPanelEventHandler();
    }

    private void loadPanelsChoiceBox() {
        try {
            populateAccountRoleChoiceBox();
            populateAccountActivityTypeChoiceBox();
            populateItemTypeChoiceBox();
        } catch (Exception e){}
    }



    private void disableAccountActivityPanelForNonAdmins(){
            if (!AuthUtil.hasPermission("accountactivity:display"))
                logsTabPane.getTabs().get(0).setDisable(true);
            if  (!AuthUtil.hasPermission("purchasesactivity:display"))
                logsTabPane.getTabs().get(0).setDisable(true);

    }

    /*
    //////////////
    SIDE/TOP BAR
    /////////////
     */

    @FXML
    private void unsubscribe(){
        Util.displayAlert("Are you sure you want to unsubscribe?\nEven if you unsubscribe, your information " +
                "will still be available if you choose to subscribe again with same email. Application will be available until subscription " +
                "period end.", Alert.AlertType.CONFIRMATION);
        try {
            new ApiConsumer().post("subscription/cancel");
        } catch (IOException | ApiException e) {
            e.printStackTrace();
            Util.displayAlert("Cancellation failed! You must be an admin to unsubscribe", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openPanel(MouseEvent mouseEvent) {
        for (EmployeePanel guiPanel : panels) {
            if (guiPanel.getName().equals(((Text) mouseEvent.getSource()).getText())) {
                if (AuthUtil.hasPermission(guiPanel.getModelName() + ":display") || !guiPanel.getRequiresPermissions())
                    guiPanel.getNode().setVisible(true);
                else
                    Util.displayAlert("You are not permitted to open this panel!", Alert.AlertType.WARNING);
            } else
                guiPanel.getNode().setVisible(false);
        }

        }

    @FXML
    private void logout() {
        AuthUtil.LOGOUT();
        Stage stage = (Stage) logoutImage.getScene().getWindow();
        stage.close();
        new GUI("main/resources/view/LoginGUI.fxml").display();

    }

    @FXML
    private void setFullScreen() {
        AuthUtil.LOGOUT();
        Stage stage = (Stage) fullScreenImage.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }


    /*
    //////////////
    ITEMS
    //////////////
    */

    private void createItemsPanelEventHandler() {
        EventHandler<Event> itemEventHandler = new EmployeePanelHandler<Item>(itemsTableView, itemDTO) {

            //Dont allow add if itemcategory is empty
            @Override
            void clearFields() {
                itemNameField.requestFocus();
                itemNameField.clear();
                itemQuantityField.clear();
                itemPriceField.clear();
                itemTypeChoiceBox.setValue(null);
                itemImageUrlField.clear();

            }

            @Override
            Item panelModel() {
                return new Item(itemNameField.getText(), Integer.parseInt(itemQuantityField.getText()),
                        new BigDecimal(itemPriceField.getText()), itemImageUrlField.getText(), itemTypeChoiceBox.getValue().getName());
            }

            @Override
            void updateSelectedModel(Item item) {
                if (!itemNameField.getText().isEmpty())
                    item.setName(itemNameField.getText());
                if (!itemQuantityField.getText().isEmpty())
                    item.setQuantity(Integer.parseInt(itemQuantityField.getText()));
                if (!itemPriceField.getText().isEmpty())
                    item.setPrice(new BigDecimal(itemPriceField.getText()));
                if (!itemImageUrlField.getText().isEmpty())
                    item.setImageURL(itemImageUrlField.getText());
                if (itemTypeChoiceBox.getValue() != null)
                    item.setItemType(itemTypeChoiceBox.getValue().getName());
            }

            @Override
            void populateFields() {
                Item item = itemsTableView.getSelectionModel().getSelectedItem();
                itemNameField.setText(item.getName());
                itemQuantityField.setText(String.valueOf(item.getQuantity()));
                itemPriceField.setText(String.valueOf(item.getPrice()));
                itemImageUrlField.setText(item.getImageURL());
            }
        }.getEventHandler();
        itemAdd.addEventHandler(ActionEvent.ACTION, itemEventHandler);
        itemUpdate.addEventHandler(ActionEvent.ACTION, itemEventHandler);
        itemDelete.addEventHandler(ActionEvent.ACTION, itemEventHandler);
        itemsAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, itemEventHandler);
        itemsCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, itemEventHandler);
        itemsTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, itemEventHandler);
    }

    @FXML
    private void addItemCategory() throws Exception {
        ItemCategory itemCategory = new ItemCategory(itemCategoryField.getText());
        itemDTO.addCategory(itemCategory);
        itemTypeChoiceBox.getItems().add(itemCategory);
        itemCategoryField.clear();
    }

    @FXML
    private void removeItemCategory() throws Exception {
        itemDTO.deleteCategory(itemTypeChoiceBox.getValue().getId());
        itemTypeChoiceBox.getItems().remove(itemTypeChoiceBox.getValue());
        itemTypeChoiceBox.setValue(null);

    }

    private void populateItemTypeChoiceBox() {
        ItemCategory itemCategory = new ItemCategory("Misc");
        addCategoryToChoiceBox(itemCategory);
        setCategoryChoiceBoxsToo(itemCategory);
        try {
            for (ItemCategory itemcategory: itemDTO.getAllCategories())
                addCategoryToChoiceBox(itemcategory);
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    private void setCategoryChoiceBoxsToo(ItemCategory itemCategory){
        itemTypeChoiceBox.setValue(itemCategory);
        purchasesItemTypeChoiceBox.setValue(itemCategory);
    }

    private void addCategoryToChoiceBox(ItemCategory itemcategory){
        itemTypeChoiceBox.getItems().add(itemcategory);
        purchasesItemTypeChoiceBox.getItems().add(itemcategory);
    }

    /*
    //////////////
    CUSTOMERS
    //////////////
     */

    private void createCustomerPanelEventHandler() {
        EventHandler<Event> customerEventHandler = new EmployeePanelHandler<Customer>(customerTableView, customerDTO) {

            void clearFields() {
                customerNameField.clear();
                customerBalanceField.clear();
                customerDailyLimitField.clear();
                customerNameField.requestFocus();

            }

            @Override
            Customer panelModel() {
               return new Customer(customerNameField.getText(), new BigDecimal(customerBalanceField.getText()),
                       Integer.parseInt(customerDailyLimitField.getText()));
            }

            @Override
            void updateSelectedModel(Customer customer) {
                if (!customerNameField.getText().isEmpty())
                    customer.setName(customerNameField.getText());
                if (!customerBalanceField.getText().isEmpty())
                    customer.setBalance(new BigDecimal(customerBalanceField.getText()));
                if (!customerDailyLimitField.getText().isEmpty())
                    customer.setDailyLimit(Integer.parseInt(customerDailyLimitField.getText()));
            }

            @Override
            void populateFields() {
                Customer customer = customerTableView.getSelectionModel().getSelectedItem();
                customerNameField.setText(customer.getName());
                customerBalanceField.setText(String.valueOf(customer.getBalance()));
                customerBalanceField.setText(String.valueOf(customer.getBalance()));
                customerDailyLimitField.setText(String.valueOf(customer.getDailyLimit()));
            }
        }.getEventHandler();
        customerUpdate.addEventHandler(ActionEvent.ACTION, customerEventHandler);
        customerDelete.addEventHandler(ActionEvent.ACTION, customerEventHandler);
        customerAdd.addEventHandler(ActionEvent.ACTION, customerEventHandler);
        customersCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, customerEventHandler);
        customerTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, customerEventHandler);
        customersAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, customerEventHandler);
    }

    /*
    //////////////
    ACCOUNTS
    //////////////
     */

    private void createAccountPanelEventHandler() {
        EventHandler<Event> accountEventHandler = new EmployeePanelHandler<Account>(accountTableView, accountDTO) {

            @Override
            void clearFields() {
                usernameField.requestFocus();
                usernameField.clear();
                passwordField.clear();
                accountRoleChoiceBox.setValue(null);

            }

            @Override
            Account panelModel() {
                return new Account(usernameField.getText(), passwordField.getText(), accountRoleChoiceBox.getValue());
            }

            @Override
            void updateSelectedModel(Account account) {
                if (!usernameField.getText().isEmpty())
                    account.setName(usernameField.getText());
                if (!passwordField.getText().isEmpty())
                    account.setPassword(passwordField.getText());
                if (accountRoleChoiceBox.getValue() != null)
                    account.setAccountRoles(accountRoleChoiceBox.getValue());
            }

            @Override
            void populateFields() {
                Account account = accountTableView.getSelectionModel().getSelectedItem();
                usernameField.setText(account.getName());
                accountRoleChoiceBox.setValue(account.getAccountRole());
            }
        }.getEventHandler();
        accountsCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, accountEventHandler);
        accountAdd.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountUpdate.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountDelete.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, accountEventHandler);
        accountsAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, accountEventHandler);
    }




    private void populateAccountRoleChoiceBox() {
        for (AccountRole accountType : AccountRole.values())
            accountRoleChoiceBox.getItems().add(accountType);
    }

   /*
    //////////////
    ACCOUNT ACTIVITY
    //////////////
    */

    private void createAccountActivitiesPanelEventHandler() {
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler<AccountActivity>(accountActivityLoggerTableView,
                accountActivityLogger) {

            @Override
            AccountActivity panelModel() {
                return new AccountActivity(activityUsernameField.getText(), activityTypeChoiceBox.getValue(),
                        Integer.parseInt(affectedIdField.getText()), affectedNameField.getText(), activityDateTimeField.getText());
            }

            @Override
            void updateSelectedModel(AccountActivity accountActivity) {
                if (!activityUsernameField.getText().isEmpty())
                    accountActivity.setName(activityUsernameField.getText());
                if (activityTypeChoiceBox.getValue() != null)
                    accountActivity.setActivityType(activityTypeChoiceBox.getValue());
                if (!affectedIdField.getText().isEmpty())
                    accountActivity.setAffectedId(Integer.parseInt(affectedIdField.getText()));
                if (!affectedNameField.getText().isEmpty())
                    accountActivity.setAffectedName(affectedNameField.getText());
                if (!activityDateTimeField.getText().isEmpty())
                    accountActivity.setDate(activityDateTimeField.getText());
            }

            @Override
            void clearFields() {
                activityUsernameField.requestFocus();
                activityUsernameField.clear();
                affectedIdField.clear();
                affectedNameField.clear();
                activityDateTimeField.clear();

            }

            @Override
            void populateFields() {
                AccountActivity accountActivity = accountActivityLoggerTableView.getSelectionModel().getSelectedItem();
                activityUsernameField.setText(accountActivity.getName());
                affectedIdField.setText(String.valueOf(accountActivity.getAffectedId()));
                affectedNameField.setText(accountActivity.getAffectedName());
                activityDateTimeField.setText(accountActivity.getDate());
                activityTypeChoiceBox.setValue(accountActivity.getActivityType());

            }
        }.getEventHandler();

        activityCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
        activityUpdate.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        activityDelete.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        activityAdd.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        accountActivityAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, panelEventHandler);
        accountActivityLoggerTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
    }

    private void populateAccountActivityTypeChoiceBox() {
        for (ActivityType activityType : ActivityType.values())
            activityTypeChoiceBox.getItems().add(activityType);
        activityTypeChoiceBox.setValue(ActivityType.ADD);
    }


    /*
    //////////////
    PURCHASES ACTIVITY
    //////////////
     */

    private void createPurchasesActivityPanelEventHandler() {
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler<PurchasesActivity>(purchasesActivityLoggerTableView,
                purchasesActivityLogger) {

            @Override
            PurchasesActivity panelModel() {
                return new PurchasesActivity(purchasesCustomerNameField.getText(), Integer.parseInt(purchasesItemIdField.getText()),
                        purchasesItemNameField.getText(), purchasesItemTypeChoiceBox.getValue().getName(), purchasesDateField.getText());
            }

            @Override
            void updateSelectedModel(PurchasesActivity purchasesActivity) {
                if (!purchasesCustomerNameField.getText().isEmpty())
                    purchasesActivity.setName(purchasesCustomerNameField.getText());
                if (!purchasesItemIdField.getText().isEmpty())
                    purchasesActivity.setItemId(Integer.parseInt(purchasesItemIdField.getText()));
                if (!purchasesItemNameField.getText().isEmpty())
                    purchasesActivity.setItemName(purchasesItemNameField.getText());
                if (purchasesItemTypeChoiceBox.getValue() != null)
                    purchasesActivity.setItemType(purchasesItemTypeChoiceBox.getValue().getName());
                if (!purchasesDateField.getText().isEmpty())
                    purchasesActivity.setDate(purchasesDateField.getText());
            }

            @Override
            void clearFields() {
                purchasesCustomerNameField.clear();
                purchasesItemIdField.clear();
                purchasesItemNameField.clear();
                purchasesDateField.clear();
                purchasesItemTypeChoiceBox.setValue(null);
                purchasesCustomerNameField.requestFocus();

            }

            @Override
            void populateFields() {
                PurchasesActivity purchasesActivity = purchasesActivityLoggerTableView.getSelectionModel().getSelectedItem();
                purchasesCustomerNameField.setText(purchasesActivity.getName());
                purchasesItemIdField.setText(String.valueOf(purchasesActivity.getItemId()));
                purchasesItemNameField.setText(purchasesActivity.getItemName());
                purchasesDateField.setText(purchasesActivity.getDate());
                purchasesCustomerNameField.requestFocus();
            }

        }.getEventHandler();
        purchasesCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
        purchasesAdd.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        purchasesUpdate.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        purchasesDelete.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        purchasesAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, panelEventHandler);
        purchasesActivityLoggerTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
    }


    private void setCellValueFactory(){
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balanceString"));
        dailyLimitColumn.setCellValueFactory(new PropertyValueFactory<>("dailyLimit"));
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountRoleColumn.setCellValueFactory(new PropertyValueFactory<>("accountRole"));
        activityIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        activityUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        activityAffectedIdColumn.setCellValueFactory(new PropertyValueFactory<>("affectedId"));
        activityAffectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("affectedName"));
        activityActivityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        activityDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        itemsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceString"));
        itemsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        purchasesLoggerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasesLoggerCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        purchasesLoggerItemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        purchasesLoggerItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        purchasesLoggerItemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        purchasesLoggerDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @Override
    public void exit() {
        AuthUtil.LOGOUT();
    }
}