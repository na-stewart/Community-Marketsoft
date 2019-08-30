package main.java.com.traderbobsemporium.controllers.Employee;
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
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.dao.Loggers.AccountActivityLogger;
import main.java.com.traderbobsemporium.model.*;
import main.java.com.traderbobsemporium.model.logging.AccountActivity;
import main.java.com.traderbobsemporium.model.logging.ActivityType;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
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
    private Subject subject = SecurityUtils.getSubject();
    private TableView<T> tableView;
    private DAO<T> dao;
    private EventHandler<Event> eventHandler = this::onEvent;
    private String panelName;
    private AccountActivityLogger accountActivityLogger;

    EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName, AccountActivityLogger accountActivityLogger){
        this.accountActivityLogger = accountActivityLogger;
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
        populateTableViewWithObservableList();
    }


    EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName){
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
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
        } catch (AuthorizationException e) {
            e.printStackTrace();
            Util.displayAlert("Insufficient Permissions!", Alert.AlertType.WARNING);
        } catch (NumberFormatException e){
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
         catch (AuthorizationException e) {
            e.printStackTrace();
            Util.displayAlert("Insufficient Permissions!", Alert.AlertType.WARNING);
        } catch (NumberFormatException e){
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

    private void add() throws SQLException {
        subject.checkPermission(panelName + ":add");
        eventType = EventType.ADD;
        T model = panelModel();
        dao.add(model);
        logActivity(ActivityType.ADD, model);
        allAfterEvents();

    }

    private void update() throws SQLException {
        subject.checkPermission(panelName + ":update");
        eventType = EventType.UPDATE;
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            updateSelectedModel(t);
            dao.update(t);
            logActivity(ActivityType.UPDATE, t);
        }
        allAfterEvents();
    }

    private void delete() {
        subject.checkPermission(panelName + ":delete");
        eventType = EventType.DELETE;
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            Model model = (Model) t;
            dao.delete(model.getId());
            logActivity(ActivityType.DELETE, t);
        }
        allAfterEvents();
    }


    private void allAfterEvents(){
        postEvent();
        populateTableViewWithObservableList();
        clearFields();
    }

    private void logActivity(ActivityType accountType, T t) {
        if (accountActivityLogger != null)
            accountActivityLogger.add(new AccountActivity(accountType, (Model) t));
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
            SecurityUtils.getSubject().checkPermission(panelName + ":display");
            ObservableList observableList = FXCollections.observableArrayList(dao.getAll());
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
