package main.java.com.traderbobsemporium.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.loggers.ActivityLoggerFactory;
import main.java.com.traderbobsemporium.loggers.Logger;
import main.java.com.traderbobsemporium.model.AccountActivity;

import java.net.URL;
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
            affectedItemColumn, dateTimeColumn;
    @SuppressWarnings("unchecked")
    private Logger<AccountActivity> accountActivity = new ActivityLoggerFactory().logger("AccountActivity");
    @FXML
    private ScrollPane dashboardScrollPane;
    @FXML
    private TilePane dashboardTilePane;
    @FXML
    private TextField panelXField, panelYField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactory();
        populateAccountActivityTable();
        dashboardTilePane.prefWidthProperty().bind(dashboardScrollPane.widthProperty());

    }

    private void populateAccountActivityTable(){
        for (AccountActivity accountActivity : accountActivity.getLogs()){
            accountActivityTableView.getItems().add(accountActivity);
        }
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
        affectedItemColumn.setCellValueFactory(new PropertyValueFactory<>("affectedItemName"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
    }
}
