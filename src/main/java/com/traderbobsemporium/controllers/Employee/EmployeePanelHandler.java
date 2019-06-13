package main.java.com.traderbobsemporium.controllers.Employee;

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
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
abstract class EmployeePanelHandler<T>{
    private Subject subject = SecurityUtils.getSubject();
    private TableView<T> tableView;
    private DAO<T> dao;
    private EventHandler<Event> eventHandler = this::onEvent;
    private String[] updateParams;
    private T profileBeingAdded;
    private String panelName;
    private boolean doLog;
    private AccountActivityLogger accountActivityLogger;

    public EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName, AccountActivityLogger accountActivityLogger){
        this.doLog = true;
        this.accountActivityLogger = accountActivityLogger;
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
    }


    public EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName){
        this.doLog = false;
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
    }

    abstract void beforeEvent();

    abstract void afterEvent();

    abstract void populateFields();

    abstract void clearFields();


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
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (Exception e){
            e.printStackTrace();
            new ExceptionDialog(e).showAndWait();
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
            Util.displayAlert("Insufficient Permissions!", Alert.AlertType.WARNING);
        } catch (NumberFormatException e){
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (Exception e){
            e.printStackTrace();
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void add() throws SQLException {
        subject.checkPermission(panelName + ":add");
        beforeEvent();
        dao.add(profileBeingAdded);
        accountActivityLogger.add(new AccountActivity(ActivityType.ADD, (Profile) profileBeingAdded));
        afterExecute();

    }

    private void update() throws SQLException {
        subject.checkPermission(panelName + ":update");
        beforeEvent();
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            dao.updateAll(t, updateParams);
            accountActivityLogger.add(new AccountActivity(ActivityType.UPDATE, (Profile) t));
        }
        afterExecute();
    }

    private void delete() throws SQLException {
        subject.checkPermission(panelName + ":delete");
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            dao.delete(t);
            accountActivityLogger.add(new AccountActivity(ActivityType.DELETE, (Profile) t));
        }
        afterExecute();
    }

    private void afterExecute(){
        afterEvent();
        clearFields();
    }

    private void onMouseEvent(MouseEvent mouseEvent){
        String name =  mouseEvent.getSource().getClass().getSimpleName();
        if (name.equals("TableView") && tableView.getSelectionModel().getSelectedItems().size() == 1)
            populateFields();
        else
            clearFields();
    }
    EventHandler<Event> getEventHandler() {
        return eventHandler;
    }

    public void setDataObject(T t) {
        this.profileBeingAdded = t;
    }

    public void setUpdateParams(String[] params) {
        this.updateParams = params;
    }
}
