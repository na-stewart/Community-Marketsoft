package main.java.com.traderbobsemporium.controllers.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import main.java.com.traderbobsemporium.dao.*;
import main.java.com.traderbobsemporium.dao.Loggers.AccountActivityLogger;
import main.java.com.traderbobsemporium.dao.Loggers.AnnouncementLogger;
import main.java.com.traderbobsemporium.dao.Loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.gui.InitGUI;
import main.java.com.traderbobsemporium.model.*;
import main.java.com.traderbobsemporium.model.logging.AccountActivity;
import main.java.com.traderbobsemporium.model.logging.ActivityType;
import main.java.com.traderbobsemporium.model.logging.Announcement;
import main.java.com.traderbobsemporium.model.logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */


public class EmployeeController implements InitGUI {
    private Subject subject = SecurityUtils.getSubject();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final AccountActivityLogger accountActivityLogger = new AccountActivityLogger();
    private final PurchasesActivityLogger purchasesActivityLogger = new PurchasesActivityLogger();
    private final AnnouncementLogger announcementLogger = new AnnouncementLogger();

    private EmployeePanel[] panels;
    @FXML
    private HBox dashboardResizerContainer;
    @FXML
    private TextField panelXField, panelYField;
    @FXML
    private ScrollPane dashboardScrollPane;
    @FXML
    private TilePane dashboardTilePane;
    @FXML
    private PieChart accountActivityFrequencyChart;
    @FXML
    private ChoiceBox<String> itemsBoughtFrequencyChoiceBox;
    @FXML
    private PieChart itemsBoughtFrequencyChart;
    @FXML
    private TextArea announcementsArea;
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
    private ChoiceBox<String> itemTypeChoiceBox;
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
    private TextField usernameField, passwordField, permissionsField;
    @FXML
    private TableView<AccountPermission> accountPermissionTableView;
    @FXML
    private TableColumn<AccountPermission, String> permissionsPermissionColumn, permissionsUsernameColumn;
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
    private ChoiceBox<String> purchasesItemTypeChoiceBox;
    @FXML
    private Button purchasesAdd, purchasesUpdate, purchasesDelete;
    @FXML
    private AnchorPane announcementsAnchorPane;
    @FXML
    private ImageView announcementsCancel;
    @FXML
    private TableView<Announcement> announcementTableView;
    @FXML
    private TableColumn<Announcement, String> announcementIdColumn, announcementAuthorColumn, titleColumn, dialogColumn,
            announcementDateTimeColumn;
    @FXML
    private TextField titleField, authorField, announcementDateTimeField;
    @FXML
    private TextArea dialogField;
    @FXML
    private Button announcementsAdd, announcementsUpdate, announcementsDelete;
    @FXML
    private AnchorPane helpAnchorPane;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountActivityLogger.start();
        panels = new EmployeePanel[]{new EmployeePanel(dashboardScrollPane, "Dashboard", false),
                new EmployeePanel(dashboardResizerContainer, "Dashboard", false), new EmployeePanel(customersAnchorPane, "Customers"),
                new EmployeePanel(accountsAnchorPane, "Accounts"), new EmployeePanel(logsTabPane, "Logs"),
                new EmployeePanel(itemsAnchorPane, "Items"), new EmployeePanel(helpAnchorPane, "Help", false)};
        setCellValueFactory();
        loadPanelsChoiceBox();
        setupDashboardResponsiveness();
        loadDashboard();
        loadDataManagerEventHandlers();
        setTableSelectionMethods();
    }

    private void setTableSelectionMethods() {
        customerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        announcementTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        purchasesActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountPermissionTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private void loadDataManagerEventHandlers() {
        createItemsPanelEventHandler();
        createCustomerPanelEventHandler();
        createPurchasesActivityPanelEventHandler();
        createAccountPanelEventHandler();
        createAnnouncementPanelEventHandler();
        createAccountActivitiesPanelEventHandler();
    }

    private void loadPanelsChoiceBox() {
        populateAccountRoleChoiceBox();
        populateAccountActivityTypeChoiceBox();
        populateItemTypeChoiceBox();
    }

    /*
    //////////////
    SIDE/TOP BAR
    /////////////
     */

    @FXML
    private void openPanel(MouseEvent mouseEvent) {
        for (EmployeePanel guiPanel : panels) {
            if (guiPanel.getName().equals(((Text) mouseEvent.getSource()).getText())) {
                if (subject.isPermitted(guiPanel.getName() + ":display") || !guiPanel.getRequiresPermissions())
                    guiPanel.getNode().setVisible(true);
            } else {
                guiPanel.getNode().setVisible(false);
            }
        }
    }

    @FXML
    private void logout() {
        accountActivityLogger.stop();
        AuthUtil.LOGOUT();
    }

    @FXML
    private void setFullScreen() {
        GUI thisGUI = GUIManager.getInstance().getByName("EmplGUI");
        thisGUI.getStage().setFullScreen(!thisGUI.getStage().isFullScreen());
    }

    /*
    //////////////
    DASHBOARD
    /////////////
     */

    private void setupDashboardResponsiveness() {
        itemsBoughtFrequencyChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) ->
                populatePurchasedItemsFrequencyChart());
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());
        dashboardTilePane.prefHeightProperty().bind(dashboardScrollPane.heightProperty());
    }

    @FXML
    private void loadDashboard() {
        try {
            populateAccountActivityFrequency();
            populatePurchasedItemsFrequencyChart();
            populateAnnouncementsTextArea();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    @FXML
    private void setDashboardTileSizes(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dashboardTilePane.setPrefTileWidth(Double.parseDouble(panelXField.getText()));
            dashboardTilePane.setPrefTileHeight(Double.parseDouble(panelYField.getText()));
        }
    }

    private void populateAnnouncementsTextArea() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Announcement announcement : announcementLogger.getAll()) {
            stringBuilder.append(announcement.getTitle()).append("\n");
            stringBuilder.append("Author: ").append(announcement.getName()).append("\n");
            stringBuilder.append(announcement.getDateTime()).append("\n");
            stringBuilder.append("-----------------------------------\n");
            stringBuilder.append(announcement.getDialog()).append("\n");
            stringBuilder.append("\n\n\n");
        }
        announcementsArea.setText(stringBuilder.toString());
    }


    private void populateAccountActivityFrequency() throws SQLException {
        int frequency = 0;
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String[] activityTypes = Arrays.stream(ActivityType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        for (String activityType : activityTypes) {
            for (AccountActivity accountActivity : accountActivityLogger.getAll()) {
                if (accountActivity.getActivityType().name().equals(activityType))
                    frequency++;
            }
            data.add(new PieChart.Data(activityType, frequency));
            frequency = 0;
        }
        removeUnusedData(data);
        accountActivityFrequencyChart.setData(data);
    }

    private void populatePurchasedItemsFrequencyChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            for (String name : listOfItemNames().stream().distinct().collect(Collectors.toList()))
                data.add(new PieChart.Data(name, Collections.frequency(listOfItemNames(), name)));
            itemsBoughtFrequencyChart.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }

    }

    private List<String> listOfItemNames() {
        List<String> list = new ArrayList<>();
        for (PurchasesActivity purchasesActivity : purchasesActivityLogger.getAll("WHERE itemtype = ?",
                itemsBoughtFrequencyChoiceBox.getValue())) {
                list.add(purchasesActivity.getItemName());
        }
        return list;
    }

    private void removeUnusedData(ObservableList<PieChart.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPieValue() == 0)
                data.remove(i--);
        }
    }

    /*
    //////////////
    ITEMS
    //////////////
    */

    private void createItemsPanelEventHandler() {
        EventHandler<Event> itemEventHandler = new EmployeePanelHandler<Item>(itemsTableView, itemDAO, "items",
                accountActivityLogger) {

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
                        new BigDecimal(itemPriceField.getText()), itemImageUrlField.getText(), itemTypeChoiceBox.getValue());
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
                    item.setItemType(itemTypeChoiceBox.getValue());
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
    private void addItemCategory(){
        itemDAO.addItemCategory(itemCategoryField.getText());
        itemTypeChoiceBox.getItems().add(itemCategoryField.getText());
        itemCategoryField.clear();
    }

    @FXML
    private void removeItemCategory(){
        itemDAO.deleteItemCategory(itemTypeChoiceBox.getValue());
        itemTypeChoiceBox.getItems().remove(itemTypeChoiceBox.getValue());
        itemTypeChoiceBox.setValue(null);

    }

    private void populateItemTypeChoiceBox() {
        try {
            for (String itemType : itemDAO.getItemCategories()) {
                itemTypeChoiceBox.getItems().add(itemType);
                purchasesItemTypeChoiceBox.getItems().add(itemType);
                itemsBoughtFrequencyChoiceBox.getItems().add(itemType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
    //////////////
    CUSTOMERS
    //////////////
     */

    private void createCustomerPanelEventHandler() {
        EventHandler<Event> customerEventHandler = new EmployeePanelHandler<Customer>(customerTableView, customerDAO, "customers",
                accountActivityLogger) {

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
        EventHandler<Event> accountEventHandler = new EmployeePanelHandler<Account>(accountTableView, accountDAO, "accounts",
                accountActivityLogger) {

            @Override
            void clearFields() {
                usernameField.requestFocus();
                usernameField.clear();
                passwordField.clear();
                permissionsField.clear();
                accountRoleChoiceBox.setValue(null);
                accountPermissionTableView.getItems().clear();
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
                accountPermissionTableView.getItems().setAll(accountDAO.getAccountPermissions(usernameField.getText()));
                accountRoleChoiceBox.setValue(account.getAccountRole());
            }

            @Override
            public void postEvent(){
                if (getEventType() == EventType.DELETE)
                    accountDAO.deletePermissions(usernameField.getText());
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
        accountRoleChoiceBox.setValue(AccountRole.UNCONFIRMED);
    }

      /*
    //////////////
    ACCOUNT PERMISSIONS
    //////////////
    */

      @FXML
    private void addPermission() {
        subject.checkPermission("accounts:add");
        AccountPermission accountPermission = new AccountPermission(usernameField.getText(), permissionsField.getText());
        try {
            accountDAO.addPermissions(accountPermission);
            accountActivityLogger.add(new AccountActivity(ActivityType.ADD, accountPermission));
            permissionsField.clear();
        } catch (SQLException e){
            LoggingUtil.logExceptionToFile(e);
            e.printStackTrace();
        }
        accountPermissionTableView.getItems().setAll(accountDAO.getAccountPermissions(usernameField.getText()));

    }

    @FXML
    private void deletePermission() {
        for (AccountPermission accountPermission : accountPermissionTableView.getSelectionModel().getSelectedItems()) {
            accountDAO.deletePermissions(accountPermission.getId());
            accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, accountPermission));
        }
        accountPermissionTableView.getItems().setAll(accountDAO.getAccountPermissions(usernameField.getText()));
    }
      /*
    //////////////
    ACCOUNT ACTIVITY
    //////////////
    */

    private void createAccountActivitiesPanelEventHandler() {
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler<AccountActivity>(accountActivityLoggerTableView,
                accountActivityLogger, "accountactivity") {

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
                purchasesActivityLogger, "purchasesactivity") {

            @Override
            PurchasesActivity panelModel() {
                return new PurchasesActivity(purchasesCustomerNameField.getText(), Integer.parseInt(purchasesItemIdField.getText()),
                        purchasesItemNameField.getText(), purchasesItemTypeChoiceBox.getValue(), purchasesDateField.getText());
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
                    purchasesActivity.setItemType(purchasesItemTypeChoiceBox.getValue());
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
                purchasesItemTypeChoiceBox.setValue(purchasesActivity.getItemType());
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

     /*
    //////////////
    ANNOUNCEMENT
    //////////////
     */

    private void createAnnouncementPanelEventHandler(){
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler<Announcement>(announcementTableView,
                announcementLogger, "announcement") {

            @Override
            Announcement panelModel() {
                return new Announcement(titleField.getText(), dialogField.getText());
            }

            @Override
            void updateSelectedModel(Announcement announcement) {
                if (!authorField.getText().isEmpty())
                    announcement.setName(authorField.getText());
                if (!titleField.getText().isEmpty())
                    announcement.setTitle(titleField.getText());
                if (!dialogField.getText().isEmpty())
                    announcement.setDialog(dialogField.getText());
                if (!announcementDateTimeField.getText().isEmpty())
                    announcement.setDateTime(announcementDateTimeField.getText());
            }


            @Override
            void clearFields() {
                titleField.requestFocus();
                titleField.clear();
                authorField.clear();
                dialogField.clear();
                announcementDateTimeField.clear();
            }

            @Override
            void populateFields() {
                Announcement announcement = announcementTableView.getSelectionModel().getSelectedItem();
                titleField.setText(announcement.getTitle());
                authorField.setText(announcement.getName());
                dialogField.setText(announcement.getDialog());
                announcementDateTimeField.setText(announcement.getDateTime());
            }
        }.getEventHandler();
        announcementsCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
        announcementsUpdate.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        announcementsDelete.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        announcementsAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, panelEventHandler);
        announcementTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, panelEventHandler);
        announcementsAdd.addEventHandler(ActionEvent.ACTION, panelEventHandler);

    }

    private void setCellValueFactory(){
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balanceString"));
        dailyLimitColumn.setCellValueFactory(new PropertyValueFactory<>("dailyLimit"));
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        permissionsPermissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        permissionsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountRoleColumn.setCellValueFactory(new PropertyValueFactory<>("accountRole"));
        announcementIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        announcementAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dialogColumn.setCellValueFactory(new PropertyValueFactory<>("dialog"));
        announcementDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
        accountActivityLogger.stop();
        DatabaseUtil.DATA_SOURCE.close();
    }
}