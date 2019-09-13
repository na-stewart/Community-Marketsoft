package main.java.com.marketsoftcommunity.controllers.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.com.marketsoftcommunity.consumers.GeneralDTO;
import main.java.com.marketsoftcommunity.model.*;
import main.java.com.marketsoftcommunity.model.logging.AccountActivity;
import main.java.com.marketsoftcommunity.model.logging.ActivityType;
import main.java.com.marketsoftcommunity.util.LoggingUtil;
import main.java.com.marketsoftcommunity.util.Util;

import org.controlsfx.control.table.TableFilter;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
abstract class EmployeePanelHandler<T>{
    private EventType eventType;
    private TableView<T> tableView;
    private GeneralDTO<T> dto;
    private EventHandler<Event> eventHandler = this::onEvent;
    private String panelName;


    EmployeePanelHandler(TableView<T> tableView, GeneralDTO<T> dao, String panelName){
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dto = dao;
        populateTableViewWithObservableList();
    }

    abstract T panelModel();

    abstract void updateSelectedModel(T t);

    abstract void populateFields();

    abstract void clearFields();

    public void postEvent() { }


    private void onEvent(Event event) {
        switch (event.getEventType().getName()) {
            case "KEY_RELEASED":
                onKeyEvent((KeyEvent) event);
                break;
            case "ACTION":
                onActionEvent((ActionEvent) event);
                break;
            case "MOUSE_PRESSED":
                onMouseEvent((MouseEvent) event);
                break;
        }
    }

    private void onKeyEvent(KeyEvent keyEvent) {
        try {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (keyEvent.isAltDown())
                    update();
                else
                    add();
            }
            else if (keyEvent.getCode() == KeyCode.DELETE)
                delete();
        }  catch (NumberFormatException e){
            e.printStackTrace();
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (SQLException e){
            e.printStackTrace();
            Util.displayAlert("Database query failed! Make sure username's are unique and that you are using" +
                    " valid characters!", Alert.AlertType.ERROR);
            LoggingUtil.logExceptionToFile(e);
        } catch (Exception e){
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    private void onActionEvent(ActionEvent actionEvent) {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        try {
            switch (buttonText) {
                case "Add":
                    add();
                    break;
                case "Update":
                    update();
                    break;
                case "Delete":
                    delete();
                    break;
            }
        }
          catch (NumberFormatException e){
            e.printStackTrace();
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (SQLException e){
            e.printStackTrace();
            Util.displayAlert("Database query failed! Make sure username's are unique and that you are using" +
                    " valid characters!", Alert.AlertType.ERROR);
            LoggingUtil.logExceptionToFile(e);
        } catch (Exception e){
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    private void add() throws Exception {
        eventType = EventType.ADD;
        T model = panelModel();
        dto.add(model);
        allAfterEvents();

    }

    private void update() throws Exception {
        eventType = EventType.UPDATE;
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            updateSelectedModel(t);
            dto.update(t);
        }
        allAfterEvents();
    }

    private void delete() throws Exception {
        eventType = EventType.DELETE;
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            System.out.println("check");
            Model model = (Model) t;
            dto.delete(model.getId());
        }
        allAfterEvents();
    }


    private void allAfterEvents(){
        postEvent();
        populateTableViewWithObservableList();
        clearFields();
    }


    private void onMouseEvent(MouseEvent mouseEvent){
        String name =  mouseEvent.getSource().getClass().getSimpleName();
        if (name.equals("TableView") && tableView.getSelectionModel().getSelectedItems().size() == 1)
            populateFields();
        else
            clearFields();
    }


    private void populateTableViewWithObservableList() {
        try {
            ObservableList observableList = FXCollections.observableArrayList(dto.getAll());
            tableView.setItems(observableList);
            TableFilter.forTableView(tableView).apply();
        }catch (Exception e){
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }
    EventHandler<Event> getEventHandler() {
        return eventHandler;
    }

    public EventType getEventType() {
        return eventType;
    }
}
