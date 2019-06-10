package main.java.com.traderbobsemporium.controllers.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import main.java.com.traderbobsemporium.dao.AccountPermissionDAO;
import main.java.com.traderbobsemporium.dao.Loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.*;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Announcement;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.subject.Subject;
import org.controlsfx.control.table.TableFilter;

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

//https://camo.githubusercontent.com/8708a8dcb49d365b1786a5093d8f3fd37aeb18a2/68747470733a2f2f7770696d672e77616c6c7374636e2e636f6d2f61353839346331622d663661662d343536652d383264662d3131353164613038333962662e706e67 <- design link\


public class EmployeeController implements Initializable {
    private Subject subject = SecurityUtils.getSubject();
    private AccountPermissionDAO accountPermissionDAO = new AccountPermissionDAO();
    private CamperDAO camperDAO = new CamperDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private AccountActivityLogger accountActivityLogger = new AccountActivityLogger();
    private PurchasesActivityLogger purchasesActivityLogger = new PurchasesActivityLogger();
    private AnnouncementDeclarer announcementLogger = new AnnouncementDeclarer();

    //TODO: cleanup var names. aka: Dashboard(Tableview)//Logger(TableView)
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
    private TableView<AccountActivity> accountActivityTableView;
    @FXML
    private TableColumn<AccountActivity, String> usernameActivityColumn, ipColumn, macColumn, activityTypeColumn,
            affectedName, affectedID, dateTimeColumnAccount, successfulColumn;
    @FXML
    private TableView<PurchasesActivity> purchasesActivityTableView;
    @FXML
    private TableColumn<PurchasesActivity, String> purchasesIdColumn, purchasesCamperNameColumn, purchasesCamperBalanceColumn,
            purchasesItemIdColumn, purchasesItemNameColumn;
    @FXML
    private PieChart accountActivityFrequencyChart;
    @FXML
    private ChoiceBox<ItemType> itemsBoughtFrequencyChoiceBox;
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
    private TextField itemNameField, itemPriceField, itemQuantityField, itemImageUrlField;
    @FXML
    private ChoiceBox<ItemType> itemTypeChoiceBox;
    @FXML
    private Button itemAdd, itemUpdate, itemDelete;
    @FXML
    private AnchorPane campersAnchorPane;
    @FXML
    private ImageView campersCancel;
    @FXML
    private TableView<Camper> camperTableView;
    @FXML
    private TableColumn<Camper, String> camperIdColumn, camperNameColumn, balanceColumn;
    @FXML
    private TextField camperNameField, camperBalanceField;
    @FXML
    private Button camperAdd, camperUpdate, camperDelete;
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
    private TableColumn<AccountPermission, String> permissionsPermissionColumn;
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
    private TableColumn<AccountActivity, String> activityIdColumn, activityUsernameColumn, activityIpColumn, activityMacColumn,
            activityActivityTypeColumn, activityAffectedIdColumn, activityAffectedNameColumn, activityDateTimeColumn, activitySuccessfulColumn;
    @FXML
    private TextField activityUsernameField, ipField, macField, affectedIdField, affectedNameField, activityDateTimeField,
            activitySuccessfulField;
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
    private TableColumn<PurchasesActivity, String> purchasesLoggerIdColumn, purchasesLoggerCamperNameColumn,
            purchasesLoggerCamperBalanceColumn, purchasesLoggerItemIdColumn, purchasesLoggerItemNameColumn;
    @FXML
    private TextField purchasesCamperNameField, purchasesCamperBalanceField, purchasesItemIdField, purchasesItemNameField;
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
        panels = new EmployeePanel[]{new EmployeePanel(dashboardScrollPane, "Dashboard"),
                new EmployeePanel(dashboardResizerContainer, "Dashboard"), new EmployeePanel(campersAnchorPane, "Campers"),
                new EmployeePanel(accountsAnchorPane, "Accounts"), new EmployeePanel(logsTabPane, "Logs"),
                new EmployeePanel(itemsAnchorPane, "Items"), new EmployeePanel(helpAnchorPane, "Help",false)};
        setCellValueFactory();
        initDashboard();
        loadDataManagerEventHandlers();
        loadPanelsChoiceBox();
        setTableSelectionMethods();
    }

    private void setTableSelectionMethods(){
        camperTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        announcementTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        purchasesActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountActivityTableView.setSelectionModel(null);
        purchasesActivityTableView.setSelectionModel(null);
    }



    private void loadDataManagerEventHandlers() {
        populateAccountRoleChoiceBox();
        createItemsPanelEventHandler();
        createCamperPanelEventHandler();
        createPurchasesActivityPanelEventHandler();
        createAccountPanelEventHandler();
        createAnnouncementPanelEventHandler();
        createAccountActivitiesPanelEventHandler();
        reloadAllPanels();

    }

    private void loadPanelsChoiceBox(){
        populateItemsBoughtFrequencyChoiceBox();
        populateAccountActivityTypeChoiceBox();
        populateItemTypeChoiceBox();
    }

    /*
    //////////////
    SIDE/TOP BAR
    /////////////
     */


     @FXML
    private void reloadAllPanels() {
        populateTableViewWithObservableList(accountDAO, accountTableView);
        populateTableViewWithObservableList(camperDAO, camperTableView);
        populateTableViewWithObservableList(announcementLogger, announcementTableView);
        populateTableViewWithObservableList(accountActivityLogger, accountActivityLoggerTableView);
        populateTableViewWithObservableList(itemDAO, itemsTableView);
        populateTableViewWithObservableList(purchasesActivityLogger, purchasesActivityLoggerTableView);
        loadDashboard();
    }

    @FXML
    private void openPanel(MouseEvent mouseEvent) {
        for (EmployeePanel guiPanel : panels) {
            if (guiPanel.getName().equals(((Text) mouseEvent.getSource()).getText())) {
                if (subject.isPermitted(guiPanel.getName() + ":Display") || !guiPanel.getRequiresPermissions())
                    guiPanel.getNode().setVisible(true);
            } else {
                guiPanel.getNode().setVisible(false);
            }
        }
    }

    @FXML
    private void logout(){
        AuthUtil.LOGOUT();
    }

    @FXML
    private void setFullScreen(){
        GUI thisGUI = GUIManager.getInstance().getByName("EmplGUI");
        thisGUI.getStage().setFullScreen(!thisGUI.getStage().isFullScreen());
    }

    /*
    //////////////
    DASHBOARD
    /////////////
     */


    private void initDashboard(){
        itemsBoughtFrequencyChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) ->
                populatePurchasedItemsFrequencyChart());
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());
        dashboardTilePane.prefHeightProperty().bind(dashboardScrollPane.heightProperty());
    }

    private void loadDashboard() {
        try {
            populateTableViewWithObservableList(accountActivityLogger, accountActivityTableView);
            populateTableViewWithObservableList(purchasesActivityLogger, purchasesActivityTableView);
            populateAccountActivityFrequency();
            populatePurchasedItemsFrequencyChart();
            populateAnnouncementsTextArea();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void setDashboardTileSizes(KeyEvent keyEvent){
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
            for (AccountActivity accountActivity: accountActivityLogger.getAll()) {
                if (accountActivity.getActivityType().name().equals(activityType))
                    frequency++;
            }
            data.add(new PieChart.Data(activityType, frequency));
            frequency = 0;
        }
        removeUnusedData(data);
        accountActivityFrequencyChart.setData(data);
    }

    private void populatePurchasedItemsFrequencyChart(){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            for (String name : listOfItemNames().stream().distinct().collect(Collectors.toList()))
               data.add(new PieChart.Data(name, Collections.frequency(listOfItemNames(), name)));
            itemsBoughtFrequencyChart.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> listOfItemNames() throws SQLException {
        List<String> list = new ArrayList<>();
        for (PurchasesActivity purchasesActivity : purchasesActivityLogger.getAll()) {
            if (itemDAO.get(purchasesActivity.getItemId()).getItemType() == itemsBoughtFrequencyChoiceBox.getValue()) {
                list.add(purchasesActivity.getItemName());
            }
        }

        return list;
    }

    private void removeUnusedData(ObservableList<PieChart.Data> data){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPieValue() == 0)
                data.remove(i--);
        }
    }

    private void populateItemsBoughtFrequencyChoiceBox(){
        for (ItemType activityType : ItemType.values())
           itemsBoughtFrequencyChoiceBox.getItems().add(activityType);
        itemsBoughtFrequencyChoiceBox.setValue(ItemType.CHIPS);
    }

    /*
    //////////////
    ITEMS
    //////////////
    */

    private void createItemsPanelEventHandler(){
        EventHandler<Event> itemEventHandler = new EmployeePanelHandler(itemsTableView) {
            @Override
            void add() {
                subject.checkPermission("items:add");
                Item item = new Item(itemNameField.getText(), Integer.parseInt(itemQuantityField.getText()),
                        new BigDecimal(itemPriceField.getText()), itemImageUrlField.getText(), itemTypeChoiceBox.getValue());
                itemDAO.add(item);
                accountActivityLogger.add(new AccountActivity(ActivityType.ADD, item));
            }

            @Override
            void update() {
                subject.checkPermission("items:update");
                for (Item item : itemsTableView.getSelectionModel().getSelectedItems()) {
                    itemDAO.updateAll(item, new String[]{itemNameField.getText(), itemQuantityField.getText(),
                    itemPriceField.getText(), itemImageUrlField.getText(), itemTypeChoiceBox.getValue().name()});
                    accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, item));
                }
            }

            @Override
            void delete() {
                subject.checkPermission("items:delete");
                for (Item item : itemsTableView.getSelectionModel().getSelectedItems()) {
                    itemDAO.delete(item.getId());
                    accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, item));
                }
            }

            @Override
            void afterEvent() {
                populateTableViewWithObservableList(itemDAO, itemsTableView);
            }

            @Override
            void clearFields() {
                itemNameField.requestFocus();
                itemNameField.clear();
                itemQuantityField.clear();
                itemPriceField.clear();
                itemImageUrlField.clear();
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

    private void populateItemTypeChoiceBox(){
        for (ItemType itemType : ItemType.values())
            itemTypeChoiceBox.getItems().add(itemType);
        itemTypeChoiceBox.setValue(ItemType.CANDY);
    }


    /*
    //////////////
    CAMPERS
    //////////////
     */

    private void createCamperPanelEventHandler(){
        EventHandler<Event> camperEventHandler = new EmployeePanelHandler(camperTableView) {
            @Override
            void add() {
                subject.checkPermission("campers:add");
                Camper camper = new Camper(camperNameField.getText(), new BigDecimal(camperBalanceField.getText()));
                camperDAO.add(camper);
                accountActivityLogger.add(new AccountActivity(ActivityType.ADD, camper));
            }

            @Override
            void update() {
                subject.checkPermission("campers:update");
                for (Camper camper : camperTableView.getSelectionModel().getSelectedItems()) {
                    camperDAO.updateAll(camper, new String[]{camperNameField.getText(), camperBalanceField.getText()});
                    accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, camper));
                }


            }

            @Override
            void delete() {
                subject.checkPermission("campers:delete");
                for (Camper camper : camperTableView.getSelectionModel().getSelectedItems()) {
                    camperDAO.delete(camper.getId());
                    accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, camper));
                }
            }

            @Override
            void clearFields() {
                camperNameField.clear();
                camperBalanceField.clear();
            }

            @Override
            void populateFields() {
                Camper camper = camperTableView.getSelectionModel().getSelectedItem();
                camperNameField.setText(camper.getName());
                camperBalanceField.setText(String.valueOf(camper.getBalance()));
            }

            @Override
            void afterEvent() {
                populateTableViewWithObservableList(camperDAO, camperTableView);
            }
        }.getEventHandler();


        camperUpdate.addEventHandler(ActionEvent.ACTION , camperEventHandler);
        camperDelete.addEventHandler(ActionEvent.ACTION, camperEventHandler);
        camperAdd.addEventHandler(ActionEvent.ACTION, camperEventHandler);
        campersCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, camperEventHandler);
        camperTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, camperEventHandler);
        campersAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, camperEventHandler);
    }

    /*
    //////////////
    ACCOUNTS
    //////////////
     */

    private void createAccountPanelEventHandler(){
        EventHandler<Event> accountEventHandler = new EmployeePanelHandler(accountTableView) {

            @Override
            void add() {
                subject.checkPermission("accounts:add");
                Account account = new Account(usernameField.getText(),
                        new DefaultPasswordService().encryptPassword(passwordField.getText()),
                       accountRoleChoiceBox.getValue());
                accountDAO.add(account);
                accountActivityLogger.add(new AccountActivity(ActivityType.ADD, account));
            }
            @Override
            void update() {
                subject.checkPermission("accounts:update");
                for (Account account: accountTableView.getSelectionModel().getSelectedItems()) {
                    if (account.getAccountRole() != AccountRole.DISABLED) {
                        accountDAO.updateAll(account, new String[]{usernameField.getText(),
                                new DefaultPasswordService().encryptPassword(passwordField.getText()),
                                accountRoleChoiceBox.getValue().name()});

                        accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, account));
                    } else
                        Util.displayAlert(account.getName() + " is disabled and cannot be edited!",
                                Alert.AlertType.ERROR);
                }
            }

            @Override
            void delete() {
                subject.checkPermission("accounts:delete");
                for (Account account : accountTableView.getSelectionModel().getSelectedItems()) {
                    accountDAO.delete(account.getId());
                    accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, account));
                }
            }

            @Override
            void clearFields() {
                usernameField.requestFocus();
                usernameField.clear();
                passwordField.clear();
                permissionsField.clear();
                accountPermissionTableView.getItems().clear();
            }

            @Override
            void populateFields() {
                Account account = accountTableView.getSelectionModel().getSelectedItem();
                usernameField.requestFocus();
                usernameField.setText(account.getName());
                populatePermissionsTable();
                accountRoleChoiceBox.setValue(account.getAccountRole());
            }

            @Override
            void afterEvent() {
                populateTableViewWithObservableList(accountDAO, accountTableView);
            }


        }.getEventHandler();
        accountsCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, accountEventHandler);
        accountAdd.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountUpdate.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountDelete.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountTableView.addEventHandler(MouseEvent.MOUSE_PRESSED, accountEventHandler);
        accountsAnchorPane.addEventFilter(KeyEvent.KEY_RELEASED, accountEventHandler);
    }

    private void populatePermissionsTable()  {
        try {
            accountPermissionTableView.getItems().setAll(accountPermissionDAO.getAllWithUsername(usernameField.getText()));
        } catch (SQLException e){
            e.printStackTrace();
        }

    }


    private void populateAccountRoleChoiceBox(){
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
      private void addPermission(){
          subject.checkPermission("accounts:add");
          AccountPermission accountPermission = new AccountPermission(usernameField.getText(), permissionsField.getText());
          accountPermissionDAO.add(accountPermission);
          populatePermissionsTable();
          accountActivityLogger.add(new AccountActivity(ActivityType.ADD, accountPermission));
      }

      @FXML
      private void deletePermission(){
          subject.checkPermission("accounts:delete");
          for (AccountPermission accountPermission: accountPermissionTableView.getSelectionModel().getSelectedItems()) {
              accountPermissionDAO.delete(accountPermission.getId());
              accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, accountPermission));
          }
          populatePermissionsTable();
      }


      /*
    //////////////
    ACCOUNT ACTIVITY
    //////////////
    */

    private void createAccountActivitiesPanelEventHandler() {
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler(accountActivityLoggerTableView) {
            @Override
            void add() {
                subject.checkPermission("accountactivity:add");
                AccountActivity accountActivity = new AccountActivity(activityUsernameField.getText(), ipField.getText(),
                        macField.getText(), activityTypeChoiceBox.getValue(), Long.parseLong(affectedIdField.getText()),
                        affectedNameField.getText(), activityDateTimeField.getText(), Boolean.parseBoolean(activitySuccessfulField.getText()));
                accountActivityLogger.add(accountActivity);
            }

            @Override
            void update() {
                subject.checkPermission("accountactivity:update");
                for (AccountActivity activityType : accountActivityLoggerTableView.getSelectionModel().getSelectedItems()) {
                    accountActivityLogger.updateAll(activityType, new String[]{activityUsernameField.getText(),
                            ipField.getText(), macField.getText(), activityTypeChoiceBox.getValue().name(), affectedIdField.getText(),
                            affectedNameField.getText(), activityDateTimeField.getText(), activitySuccessfulField.getText()});
                }
            }

            @Override
            void delete() {
                subject.checkPermission("accountactivity:delete");
                for (AccountActivity accountActivity : accountActivityLoggerTableView.getSelectionModel().getSelectedItems())
                    accountActivityLogger.delete(accountActivity.getId());
            }

            @Override
            void afterEvent() {
                populateTableViewWithObservableList(accountActivityLogger, accountActivityLoggerTableView);
            }

            @Override
            void clearFields() {
                activityUsernameField.requestFocus();
                activityUsernameField.clear();
                ipField.clear();
                macField.clear();
                affectedIdField.clear();
                affectedNameField.clear();
                activityDateTimeField.clear();
                activitySuccessfulField.clear();
            }

            @Override
            void populateFields() {
                AccountActivity accountActivity = accountActivityLoggerTableView.getSelectionModel().getSelectedItem();
                activityUsernameField.requestFocus();
                activityUsernameField.setText(accountActivity.getName());
                ipField.setText(accountActivity.getIp());
                macField.setText(accountActivity.getMac());
                affectedIdField.setText(String.valueOf(accountActivity.getAffectedId()));
                affectedNameField.setText(accountActivity.getAffectedName());
                activityDateTimeField.setText(accountActivity.getDateTime());
                activitySuccessfulField.setText(String.valueOf(accountActivity.isSuccessful()));
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

    private void populateAccountActivityTypeChoiceBox(){
        for (ActivityType activityType : ActivityType.values())
            activityTypeChoiceBox.getItems().add(activityType);
        activityTypeChoiceBox.setValue(ActivityType.ADD);
    }


    /*
    //////////////
    PURCHASES ACTIVITY
    //////////////
     */

    private void createPurchasesActivityPanelEventHandler(){
        EventHandler<Event> panelEventHandler = new EmployeePanelHandler(purchasesActivityLoggerTableView) {
            @Override
            void add() {
                subject.checkPermission("purchasesactivity:add");
                PurchasesActivity purchasesActivity = new PurchasesActivity(purchasesCamperNameField.getText(),
                        new BigDecimal(Double.parseDouble(purchasesCamperBalanceField.getText())),
                        Long.parseLong(purchasesItemIdField.getText()), purchasesItemNameField.getText());
                purchasesActivityLogger.add(purchasesActivity);
            }

            @Override
            void update() {
                subject.checkPermission("purchasesactivity:update");
                for (PurchasesActivity purchasesActivity : purchasesActivityLoggerTableView.getSelectionModel().getSelectedItems()) {
                    purchasesActivityLogger.updateAll(purchasesActivity, new String[]{purchasesCamperNameField.getText(),
                            purchasesCamperBalanceField.getText(), purchasesItemIdField.getText(), purchasesItemNameField.getText()});
                }
            }

            @Override
            void delete() {
                subject.checkPermission("purchasesactivity:delete");
                for (PurchasesActivity purchasesActivity : purchasesActivityLoggerTableView.getSelectionModel().getSelectedItems())
                    purchasesActivityLogger.delete(purchasesActivity.getId());
                populateTableViewWithObservableList(itemDAO, itemsTableView);

            }

            @Override
            void afterEvent() {
                populateTableViewWithObservableList(purchasesActivityLogger, purchasesActivityLoggerTableView);
            }

            @Override
            void clearFields() {
                purchasesCamperNameField.clear();
                purchasesCamperBalanceField.clear();
                purchasesItemIdField.clear();
                purchasesItemNameField.clear();
                purchasesCamperNameField.requestFocus();
            }

            @Override
            void populateFields() {
                PurchasesActivity purchasesActivity = purchasesActivityLoggerTableView.getSelectionModel().getSelectedItem();
                purchasesCamperNameField.setText(purchasesActivity.getCamperName());
                purchasesCamperBalanceField.setText(String.valueOf(purchasesActivity.getCamperBalance()));
                purchasesItemIdField.setText(String.valueOf(purchasesActivity.getItemId()));
                purchasesItemNameField.setText(purchasesActivity.getItemName());
                purchasesCamperNameField.requestFocus();
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
         EventHandler<Event> panelEventHandler = new EmployeePanelHandler(announcementTableView) {

             void add() {
                 subject.checkPermission("announcement:add");
                 Announcement announcement = new Announcement(titleField.getText(), dialogField.getText());
                 announcementLogger.add(announcement);
                 accountActivityLogger.add(new AccountActivity(ActivityType.ADD, announcement));
             }

             @Override
             void update() {
                 subject.checkPermission("announcement:update");
                 for (Announcement announcement : announcementTableView.getSelectionModel().getSelectedItems()) {
                     announcementLogger.updateAll(announcement, new String[]{authorField.getText(), titleField.getText(),
                             dialogField.getText(), announcementDateTimeField.getText()});
                     accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, announcement));
                 }
             }

             @Override
             void delete() {
                 subject.checkPermission("announcement:delete");
                 for (Announcement announcement : announcementTableView.getSelectionModel().getSelectedItems()) {
                     announcementLogger.delete(announcement.getId());
                     accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, announcement));
                 }

             }

             @Override
             void afterEvent() {
                 populateTableViewWithObservableList(announcementLogger, announcementTableView);
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
                 titleField.requestFocus();
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


     @SuppressWarnings("unchecked")
     private void populateTableViewWithObservableList(DAO dao, TableView tableView) {
         try {
             ObservableList observableList = FXCollections.observableArrayList(dao.getAll());
             tableView.setItems(observableList);
             TableFilter.forTableView(tableView).apply();
         }catch (Exception e){
             e.printStackTrace();
         }
     }


    private void setCellValueFactory(){
        usernameActivityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        macColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        affectedName.setCellValueFactory(new PropertyValueFactory<>("affectedName"));
        affectedID.setCellValueFactory(new PropertyValueFactory<>("affectedId"));
        dateTimeColumnAccount.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        successfulColumn.setCellValueFactory(new PropertyValueFactory<>("successful"));
        camperNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balanceString"));
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        permissionsPermissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        accountRoleColumn.setCellValueFactory(new PropertyValueFactory<>("accountRole"));
        announcementIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        announcementAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dialogColumn.setCellValueFactory(new PropertyValueFactory<>("dialog"));
        announcementDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        activityIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        activityUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        activityIpColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        activityMacColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        activityAffectedIdColumn.setCellValueFactory(new PropertyValueFactory<>("affectedId"));
        activityAffectedNameColumn.setCellValueFactory(new PropertyValueFactory<>("affectedName"));
        activityActivityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        activityDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        activitySuccessfulColumn.setCellValueFactory(new PropertyValueFactory<>("successful"));
        itemsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceString"));
        itemsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        purchasesIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasesCamperNameColumn.setCellValueFactory(new PropertyValueFactory<>("camperName"));
        purchasesCamperBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("camperBalanceString"));
        purchasesItemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        purchasesItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        purchasesLoggerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasesLoggerCamperNameColumn.setCellValueFactory(new PropertyValueFactory<>("camperName"));
        purchasesLoggerCamperBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("camperBalance"));
        purchasesLoggerItemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        purchasesLoggerItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

    }
}
