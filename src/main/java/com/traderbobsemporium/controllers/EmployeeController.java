package main.java.com.traderbobsemporium.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.dao.loggers.ActivityLoggerFactory;
import main.java.com.traderbobsemporium.dao.loggers.Logger;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

//https://camo.githubusercontent.com/8708a8dcb49d365b1786a5093d8f3fd37aeb18a2/68747470733a2f2f7770696d672e77616c6c7374636e2e636f6d2f61353839346331622d663661662d343536652d383264662d3131353164613038333962662e706e67 <- design link
public class EmployeeController implements Initializable {
    @FXML
    private TableView<AccountActivity> accountActivityTableView;
    @FXML
    private TableColumn<AccountActivity, String> usernameColumn, ipColumn, macColumn, activityTypeColumn,
            affectedName, affectedID, dateTimeColumn;
    @SuppressWarnings("unchecked")
    private Logger<AccountActivity> accountActivity = new ActivityLoggerFactory().logger("AccountActivity");
    @FXML
    private ScrollPane dashboardScrollPane;
    @FXML
    private TilePane dashboardTilePane;
    @FXML
    private TextField panelXField, panelYField;
    @FXML
    private PieChart pieChart;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactory();
        loadDashboard();
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());
    }

    private void loadDashboard(){
        try {
            populateAccountActivityTable();
            populateAccountActivityFrequency();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //TODO below
        //
        /*
        populatePurchasedItems();
        populatePurchasesActivityTable();
        */

    }

    private void populateAccountActivityTable() throws SQLException {


            for (AccountActivity accountActivity : accountActivity.getAll()){
                accountActivityTableView.getItems().add(accountActivity);
            }
        }


    private void populateAccountActivityFrequency() throws SQLException {
        int frequency = 0;
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String[] activityTypes = Arrays.stream(ActivityType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        AccountActivity[] accountActivities = accountActivity.getAll().toArray(new AccountActivity[0]);
        for (String activityType : activityTypes) {
            for (AccountActivity accountActivity : accountActivities) {
                if (accountActivity.getActivityType().name().equals(activityType)) {
                    frequency++;
                }
            }
            data.add(new PieChart.Data(activityType, frequency));
            frequency = 0;
        }
        removeUnusedData(data);
        pieChart.setData(data);
    }

    private void removeUnusedData(ObservableList<PieChart.Data> data){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPieValue() == 0)
                data.remove(i--);
        }
    }


    private void populatePurchasesActivityTable(){


    }

    private void populatePurchasedItems(){

    }



    @FXML
    private void setFullScreen(){
        GUI thisGUI = GUIManager.getInstance().getGUIByName("EmplGUI");
        thisGUI.getStage().setFullScreen(!thisGUI.getStage().isFullScreen());
    }

    @FXML
    private void setDashboardTileSizes(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dashboardTilePane.setPrefTileWidth(Double.parseDouble(panelXField.getText()));
            dashboardTilePane.setPrefTileHeight(Double.parseDouble(panelYField.getText()));
        }
    }

    private void setCellValueFactory(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        macColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));
        affectedName.setCellValueFactory(new PropertyValueFactory<>("affectedItemName"));
        affectedID.setCellValueFactory(new PropertyValueFactory<>("affectedItemId"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
    }
}
