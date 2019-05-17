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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import main.java.com.traderbobsemporium.dao.CamperDAO;
import main.java.com.traderbobsemporium.dao.loggers.Logger;
import main.java.com.traderbobsemporium.factory.LoggerFactory;
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
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

//https://camo.githubusercontent.com/8708a8dcb49d365b1786a5093d8f3fd37aeb18a2/68747470733a2f2f7770696d672e77616c6c7374636e2e636f6d2f61353839346331622d663661662d343536652d383264662d3131353164613038333962662e706e67 <- design link
public class EmployeeController implements Initializable {
    private Logger logger;
    private GUIPanel[] panels;
    @FXML
    private TextField panelXField, panelYField;
    @FXML
    private TableView<AccountActivity> accountActivityTableView;
    @FXML
    private TableColumn<AccountActivity, String> usernameColumn, ipColumn, macColumn, activityTypeColumn,
            affectedName, affectedID, dateTimeColumnAccount;
    @FXML
    private TableView<PurchasesActivity> purchasesActivityTableView;
    @FXML
    private TableColumn<PurchasesActivity, String> camperPurchasingNameColumn, itemNameColumn, quantityColumn, balanceSpentColumn,
            dateTimeColumnPurchases;
    @FXML
    private ScrollPane dashboardScrollPane;
    @FXML
    private TilePane dashboardTilePane;
    @FXML
    private PieChart accountActivityFrequencyChart;
    @FXML
    private TextArea announcementsArea;
    @FXML
    private AnchorPane campersAnchorPane;
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
    private TableView<Account> accountTableView;
    @FXML
    private TextField nameField, passwordField, permissionsField;
    @FXML
    private ChoiceBox<AccountRole> accountRoleChoiceBox;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panels = new GUIPanel[]{new GUIPanel(dashboardScrollPane, "Dashboard"),
                new GUIPanel(campersAnchorPane, "Campers")};
        setCellValueFactory();
        loadDashboard();
        createCamperPanelEventHandler();
        loadDataManagerPanels();
        setTableSelectionMethods();
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());
        dashboardTilePane.prefHeightProperty().bind(dashboardScrollPane.heightProperty());
    }

    private void setTableSelectionMethods(){
        camperTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadDashboard() {
        try {
            populateAccountActivityTable();
            populateAccountActivityFrequency();
            populateAnnouncementsTextArea();
            populatePurchasedItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataManagerPanels(){
        try {
            camperTableView.getItems().setAll(new CamperDAO().getAll());
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
        logger = new LoggerFactory().create("announcements");
        StringBuilder stringBuilder = new StringBuilder();
        for (Object announcementObj : logger.getLogs()) {
            Announcement announcement = (Announcement) announcementObj;
            stringBuilder.append(announcement.getTitle()).append("\n");
            stringBuilder.append(announcement.getDateTime()).append("\n");
            stringBuilder.append("-----------------------------------\n");
            stringBuilder.append(announcement.getDialog()).append("\n");
            stringBuilder.append("\n\n\n");
        }
        announcementsArea.setText(stringBuilder.toString());

    }

    private void populateAccountActivityTable() throws SQLException {
        logger = new LoggerFactory().create("accountactivity");
        accountActivityTableView.getItems().clear();
        accountActivityTableView.getItems().addAll(logger.getLogs());
    }

    private void populateAccountActivityFrequency() throws SQLException {
        int frequency = 0;
        logger = new LoggerFactory().create("accountactivity");
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String[] activityTypes = Arrays.stream(ActivityType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        for (String activityType : activityTypes) {
            for (Object accountActivityObj : logger.getLogs()) {
                AccountActivity accountActivity = (AccountActivity) accountActivityObj;
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
    CAMPERS
    //////////////
     */

    private void createCamperPanelEventHandler(){
        List<Camper> selectedCampers = camperTableView.getSelectionModel().getSelectedItems();
        List<Camper> getItems = camperTableView.getItems();
        String[] camperParams = new String[]{camperNameField.getText(), camperBalanceField.getText()};
        CamperDAO camperDAO = new CamperDAO();
        EventHandler<Event> eventHandler = new PanelEventHandler(camperTableView) {
            @Override
            public void handle(Event event) {
                onEvent(event);
            }

            @Override
            void add() throws SQLException {
                Camper camper = new Camper(Util.NEW_ID(), camperParams[0], Integer.parseInt(camperParams[1]));
                camperDAO.add(camper);
                getItems.add(camper);
                clearCamperFields();

            }
            @Override
            void update() throws SQLException {
                for (Camper camper : selectedCampers)
                    camperDAO.update(camper, camperParams);
                camperTableView.getItems().setAll(camperDAO.getAll());
            }
            @Override
            void delete() throws SQLException {
                for (Camper camper : selectedCampers)
                    camperDAO.delete(camper.getId());
                getItems.removeAll(selectedCampers);
            }
        };
        campersAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
        camperUpdate.addEventHandler(ActionEvent.ACTION ,eventHandler);
        camperDelete.addEventHandler(ActionEvent.ACTION, eventHandler);
    }

    /*
    //////////////
    ACCOUNTS
    //////////////
     */

    private void createAccountPanelEventHandler(){
        
    }

    @FXML
    private void fillCamperFields(){
        Camper camper = camperTableView.getSelectionModel().getSelectedItem();
        camperNameField.setText(camper.getName());
        camperBalanceField.setText(String.valueOf(camper.getBalance()));
    }

    private void clearCamperFields(){
        camperNameField.clear();
        camperBalanceField.clear();
        camperNameField.requestFocus();
    }


    private void setCellValueFactory(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        macColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        affectedName.setCellValueFactory(new PropertyValueFactory<>("affectedItemName"));
        affectedID.setCellValueFactory(new PropertyValueFactory<>("affectedItemId"));
        dateTimeColumnAccount.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        camperPurchasingNameColumn.setCellValueFactory(new PropertyValueFactory<>("camperName"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        balanceSpentColumn.setCellValueFactory(new PropertyValueFactory<>("balanceSpent"));
        dateTimeColumnPurchases.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        camperNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        camperIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

    }
}
