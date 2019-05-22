package main.java.com.traderbobsemporium.controllers.Employee;
import javafx.application.Platform;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.dao.DatabaseAccessFactory;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.gui.GUIPanel;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Logging.Announcement;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

//https://camo.githubusercontent.com/8708a8dcb49d365b1786a5093d8f3fd37aeb18a2/68747470733a2f2f7770696d672e77616c6c7374636e2e636f6d2f61353839346331622d663661662d343536652d383264662d3131353164613038333962662e706e67 <- design link
public class EmployeeController implements Initializable {
    private DAO<Camper> camperDAO = new DatabaseAccessFactory<Camper>().getDAO("camper");
    private DAO<Account> accountDAO = new DatabaseAccessFactory<Account>().getDAO("account");
    private DAO<AccountActivity> accountActivityLogger = new DatabaseAccessFactory<AccountActivity>().getLogger("accountactivity");
    private DAO<Announcement> announcementLogger = new DatabaseAccessFactory<Announcement>().getLogger("announcements");

    //TODO: cleanup var names. aka: Dashboard(Tableview)//Logger(TableView)
    private GUIPanel[] panels;
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
            affectedName, affectedID, dateTimeColumnAccount;
    @FXML
    private TableView<PurchasesActivity> purchasesActivityTableView;
    @FXML
    private TableColumn<PurchasesActivity, String> camperPurchasingNameColumn, itemNameColumn, quantityColumn,
            balanceSpentColumn, dateTimeColumnPurchases;
    @FXML
    private PieChart accountActivityFrequencyChart;
    @FXML
    private TextArea announcementsArea;
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
    private Button camperUpdate, camperDelete;
    @FXML
    private AnchorPane accountsAnchorPane;
    @FXML
    private ImageView accountsCancel;
    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, String> accountIdColumn, usernameColumn, passwordColumn, permissionsColumn, accountRoleColumn;
    @FXML
    private TextField usernameField, passwordField, permissionsField;
    @FXML
    private ChoiceBox<AccountRole> accountRoleChoiceBox;
    @FXML
    private Button accountUpdate, accountDelete;
    @FXML
    private TabPane logsTabPane;
    @FXML
    private AnchorPane accountActivityAnchorPane;
    @FXML
    private TableView<AccountActivity> accountActivityLoggerTableView;
    @FXML
    private TableColumn<AccountActivity, String> activityIdColumn, activityUsernameColumn, activityIpColumn, activityMacColumn,
            activityActivityTypeColumn, activityAffectedIdColumn, activityAffectedNameColumn, activityDateTimeColumn;
    @FXML
    private TextField activityUsernameField, ipField, macField, affectedIdField, affectedNameField, activityDateTimeField;
    @FXML
    private ChoiceBox<ActivityType> activityTypeChoiceBox;
    @FXML
    private Button activityUpdate, activityDelete;
    @FXML
    private AnchorPane announcementsAnchorPane;
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
    private Button announcementsUpdate, announcementsDelete;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panels = new GUIPanel[]{new GUIPanel(dashboardScrollPane, "Dashboard"),
                new GUIPanel(campersAnchorPane, "Campers"), new GUIPanel(accountsAnchorPane, "Accounts"),
                new GUIPanel(logsTabPane, "Logs")};
        setCellValueFactory();
        loadDash();
        loadDataManagerPanels();
        setTableSelectionMethods();
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());
        dashboardTilePane.prefHeightProperty().bind(dashboardScrollPane.heightProperty());
    }

    private void setTableSelectionMethods(){
        camperTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        announcementTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        accountActivityLoggerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private void loadDash(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> loadDashboard());
            }
        };
        new Timer().schedule(task, 0, 20000);
    }

    private void loadDashboard() {
        try {
            accountActivityTableView.getItems().setAll(accountActivityLogger.getAll());
            populateAccountActivityFrequency();
            populateAnnouncementsTextArea();
            populatePurchasedItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataManagerPanels(){
        try {
            camperTableView.getItems().setAll(camperDAO.getAll());
            createCamperPanelEventHandler();
            accountTableView.getItems().setAll(accountDAO.getAll());
            populateAccountRoleChoiceBox();
            createAccountPanelEventHandler();
            announcementTableView.getItems().setAll(announcementLogger.getAll());
            createAnnouncementPanelEventHandler();
            accountActivityLoggerTableView.getItems().setAll(accountActivityLogger.getAll());
            createAccountActivitiesPanelEventHandler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    //////////////
    SIDE/TOP BAR
    /////////////
    */

    @FXML
    private void openPanel(MouseEvent mouseEvent){
        displayPanel(((Text) mouseEvent.getSource()).getText());
    }

    private void displayPanel(String display){
        for (GUIPanel guiPanel : panels)
            if (guiPanel.getName().equals(display))
                guiPanel.getNode().setVisible(true);
            else
                guiPanel.getNode().setVisible(false);
    }

    @FXML
    private void logout(){
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.closeByName("EmplGUI");
        SecurityUtils.getSubject().logout();
        guiManager.getByName("LoginGUI").display();
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

    private void populatePurchasedItems(){
        //TODO purchasedItems
    }

    private void removeUnusedData(ObservableList<PieChart.Data> data){
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


    /*
    //////////////
    CAMPERS
    //////////////
     */

    private void createCamperPanelEventHandler(){
        EventHandler<Event> camperEventHandler = new PanelEventHandler(camperTableView) {
            @Override
            public void handle(Event event) {
                onEvent(event);
            }
            @Override
            void add() throws SQLException {
                Camper camper = new Camper(camperNameField.getText(), Integer.parseInt(camperBalanceField.getText()));
                camperDAO.add(camper);
                camperTableView.getItems().add(camper);
                accountActivityLogger.add(new AccountActivity(ActivityType.ADD, camper));
            }
            @Override
            void update() throws SQLException {
                for (Camper camper : camperTableView.getSelectionModel().getSelectedItems()) {
                    camperDAO.update(camper, new String[]{camperNameField.getText(), camperBalanceField.getText()});
                    accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, camper));
                }
                camperTableView.getItems().setAll(camperDAO.getAll());

            }
            @Override
            void delete() throws SQLException {
                for (Camper camper : camperTableView.getSelectionModel().getSelectedItems()) {
                    camperDAO.delete(camper.getId());
                    accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, camper));
                }
                camperTableView.getItems().removeAll(camperTableView.getSelectionModel().getSelectedItems());
            }

            @Override
            void onSuccessfulEvent() {
                camperNameField.clear();
                camperBalanceField.clear();
                camperNameField.requestFocus();
                camperTableView.getSelectionModel().clearSelection();
            }
        };
        campersAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, camperEventHandler);
        campersCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, camperEventHandler);
        camperUpdate.addEventHandler(ActionEvent.ACTION , camperEventHandler);
        camperDelete.addEventHandler(ActionEvent.ACTION, camperEventHandler);
    }

    @FXML
    private void fillCamperFields(){
        Camper camper = camperTableView.getSelectionModel().getSelectedItem();
        camperNameField.setText(camper.getName());
        camperBalanceField.setText(String.valueOf(camper.getBalance()));
    }

    /*
    //////////////
    ACCOUNTS
    //////////////
     */

    private void createAccountPanelEventHandler(){
        EventHandler<Event> accountEventHandler = new PanelEventHandler(accountTableView) {
            @Override
            void add() throws SQLException {
                Account account = new Account(usernameField.getText(),
                        new DefaultPasswordService().encryptPassword(passwordField.getText()),
                        permissionsField.getText(), accountRoleChoiceBox.getValue());
                accountDAO.add(account);
                accountTableView.getItems().add(account);
                accountActivityLogger.add(new AccountActivity(ActivityType.ADD, account));
            }
            @Override
            void update() throws SQLException {
                for (Account account: accountTableView.getSelectionModel().getSelectedItems()) {
                    accountDAO.update(account, new String[]{usernameField.getText(), passwordField.getText(),
                            accountRoleChoiceBox.getValue().name(),
                            permissionsField.getText()});
                    accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, account));
                }
                accountTableView.getItems().setAll(accountDAO.getAll());
            }

            @Override
            void delete() throws SQLException {
                for (Account account : accountTableView.getSelectionModel().getSelectedItems()) {
                    accountDAO.delete(account.getId());
                    accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, account));
                }
                accountTableView.getItems().removeAll(accountTableView.getSelectionModel().getSelectedItems());
            }

            @Override
            void onSuccessfulEvent() {
                usernameField.requestFocus();
                usernameField.clear();
                passwordField.clear();
                permissionsField.clear();
                accountTableView.getSelectionModel().clearSelection();
            }

            @Override
            public void handle(Event event) {
                onEvent(event);
            }
        };
        accountsCancel.addEventHandler(MouseEvent.MOUSE_PRESSED, accountEventHandler);
        accountUpdate.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountDelete.addEventHandler(ActionEvent.ACTION, accountEventHandler);
        accountsAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, accountEventHandler);
    }

    @FXML
    private void fillAccountFields(){
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        usernameField.setText(account.getName());
        passwordField.setText(account.getPassword());
        permissionsField.setText(account.getPermissions());
        accountRoleChoiceBox.setValue(account.getAccountRole());
    }


    private void populateAccountRoleChoiceBox(){
        for (AccountRole accountType : AccountRole.values())
            accountRoleChoiceBox.getItems().add(accountType);
        accountRoleChoiceBox.setValue(AccountRole.UNCONFIRMED);
    }

      /*
    //////////////
    ACCOUNT ACTIVITY
    //////////////
    */

      //todo Account Activity

    private void createAccountActivitiesPanelEventHandler(){
        EventHandler<Event> panelEventHandler = new PanelEventHandler(accountActivityLoggerTableView) {
            @Override
            void add() throws SQLException {
                AccountActivity accountActivity = new AccountActivity(activityUsernameField.getText(), ipField.getText(),
                        macField.getText(), activityTypeChoiceBox.getValue(), Long.parseLong(affectedIdField.getText()),
                        affectedNameField.getText(), activityDateTimeField.getText());
                accountActivityLogger.add(accountActivity);
            }


            @Override
            void update() throws SQLException {
                for (AccountActivity activityType : accountActivityLogger.getAll()) {
                    accountActivityLogger.update(activityType, new String[]{activityUsernameField.getText(),
                    ipField.getText(), macField.getText(), activityTypeChoiceBox.getValue().name(), affectedID.getText(),
                    affectedNameField.getText(), activityDateTimeField.getText()});
                }
                accountActivityTableView.getItems().setAll(accountActivityLogger.getAll());
            }
            @Override
            void delete() throws SQLException {
                for (AccountActivity accountActivity : accountActivityLoggerTableView.getSelectionModel().getSelectedItems())
                    accountActivityLogger.delete(accountActivity.getId());
                accountActivityLoggerTableView.getItems().removeAll(accountActivityLoggerTableView.getSelectionModel().getSelectedItems());
            }

            @Override
            void onSuccessfulEvent() {
                activityUsernameField.clear();
                ipField.clear();
                macField.clear();
                affectedIdField.clear();
                affectedNameField.clear();
                activityDateTimeField.clear();
            }

            @Override
            public void handle(Event event) {
                onEvent(event);
            }
        };
        activityUpdate.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        activityDelete.addEventHandler(ActionEvent.ACTION, panelEventHandler);
        accountActivityAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, panelEventHandler);
    }

    @FXML
    private void fillAccountActivityFields(){
        AccountActivity accountActivity = accountActivityLoggerTableView.getSelectionModel().getSelectedItem();
        activityUsernameField.setText(accountActivity.getName());
        ipField.setText(accountActivity.getIp());
        macField.setText(accountActivity.getMac());
        affectedIdField.setText(String.valueOf(accountActivity.getAffectedId()));
        affectedNameField.setText(accountActivity.getAffectedName());
        activityDateTimeField.setText(accountActivity.getDateTime());
        activityTypeChoiceBox.setValue(accountActivity.getActivityType());
    }


     /*
    //////////////
    ANNOUNCEMENT
    //////////////
     */

     private void createAnnouncementPanelEventHandler(){
         EventHandler<Event> panelEventHandler = new PanelEventHandler(announcementTableView) {
             @Override
             public void handle(Event event) {
                if (!dialogField.isFocused())
                    onEvent(event);
             }
             @Override
             void add() throws SQLException {
                 Announcement announcement = new Announcement(titleField.getText(), dialogField.getText());
                 announcementLogger.add(announcement);
                 announcementTableView.getItems().add(announcement);
                 accountActivityLogger.add(new AccountActivity(ActivityType.ADD, announcement));
             }

             @Override
             void update() throws SQLException {
                 for (Announcement announcement : announcementTableView.getSelectionModel().getSelectedItems()) {
                     announcementLogger.update(announcement, new String[]{authorField.getText(), titleField.getText(),
                             dialogField.getText(), announcementDateTimeField.getText()});
                     accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, announcement));
                 }
                 announcementTableView.getItems().setAll(announcementLogger.getAll());

             }

             @Override
             void delete() throws SQLException {
                 for (Announcement announcement : announcementTableView.getSelectionModel().getSelectedItems()){
                     announcementLogger.delete(announcement.getId());
                     accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, announcement));
                 }
                 announcementTableView.getItems().removeAll(announcementTableView.getSelectionModel().getSelectedItems());
             }

             @Override
             void onSuccessfulEvent() {
                titleField.clear();
                authorField.clear();
                dialogField.clear();
                announcementDateTimeField.clear();
                announcementTableView.getSelectionModel().clearSelection();
             }
         };
         announcementsUpdate.addEventHandler(ActionEvent.ACTION, panelEventHandler);
         announcementsDelete.addEventHandler(ActionEvent.ACTION, panelEventHandler);
         announcementsAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, panelEventHandler);
     }

     @FXML
     private void fillAnnouncementFields(){
         Announcement announcement = announcementTableView.getSelectionModel().getSelectedItem();
         titleField.setText(announcement.getTitle());
         authorField.setText(announcement.getName());
         dialogField.setText(announcement.getDialog());
         announcementDateTimeField.setText(announcement.getDateTime());
     }



    private void setCellValueFactory(){
        usernameActivityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        macColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        affectedName.setCellValueFactory(new PropertyValueFactory<>("affectedName"));
        affectedID.setCellValueFactory(new PropertyValueFactory<>("affectedId"));
        dateTimeColumnAccount.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        camperPurchasingNameColumn.setCellValueFactory(new PropertyValueFactory<>("camperName"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        balanceSpentColumn.setCellValueFactory(new PropertyValueFactory<>("balanceSpent"));
        dateTimeColumnPurchases.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        camperNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        permissionsColumn.setCellValueFactory(new PropertyValueFactory<>("permissions"));
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
    }
}