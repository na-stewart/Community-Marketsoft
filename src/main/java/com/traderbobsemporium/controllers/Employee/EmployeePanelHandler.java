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
import main.java.com.traderbobsemporium.model.*;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Logging.Announcement;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;

import java.math.BigDecimal;
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
    private String panelName;
    private AccountActivityLogger accountActivityLogger;

    EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName, AccountActivityLogger accountActivityLogger){
        this.accountActivityLogger = accountActivityLogger;
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
    }


    EmployeePanelHandler(TableView<T> tableView, DAO<T> dao, String panelName){
        this.panelName = panelName.toLowerCase();
        this.tableView = tableView;
        this.dao = dao;
    }

    abstract String[] panelFields();

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
            e.printStackTrace();
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (SQLException e){
            e.printStackTrace();
            Util.displayAlert("Database query failed! Make sure username's are unique and that you are using" +
                    " valid characters!", Alert.AlertType.ERROR);
            LoggingUtil.logExceptionToFile(e);
        } catch (Exception e){
            e.printStackTrace();
            new ExceptionDialog(e).showAndWait();
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
            new ExceptionDialog(e).showAndWait();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    private void add() throws SQLException {
        subject.checkPermission(panelName + ":add");
        T panelObj =  getPanelObject();
        dao.add(panelObj);
        logActivity(ActivityType.ADD, panelObj);
        allAfterEvents();

    }

    private void update() throws SQLException {
        subject.checkPermission(panelName + ":update");
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            dao.updateAll(t, panelFields());
            logActivity(ActivityType.UPDATE, t);
        }
        allAfterEvents();
    }

    private void delete() {
        subject.checkPermission(panelName + ":delete");
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            Model model = (Model) t;
            dao.delete(model.getId());
            logActivity(ActivityType.DELETE, t);
        }
        allAfterEvents();
    }


    private void allAfterEvents(){
        afterEvent();
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

    @SuppressWarnings("unchecked")
    private T getPanelObject(){
        String[] params = panelFields();
        switch (panelName){
            case "items":
                return (T) new Item(params[0], Integer.valueOf(params[1]), new BigDecimal(params[2]), params[3], ItemType.valueOf(params[4]));
            case "campers":
                return (T) new Camper(params[0], new BigDecimal(params[1]));
            case "accounts":
                return (T) new Account(params[0], params[1], AccountRole.valueOf(params[2]));
            case "accountactivity":
                return (T) new AccountActivity(params[0], ActivityType.valueOf(params[1]), Integer.parseInt(params[2]),
                        params[3], params[4]);
            case "purchasesactivity":
                return (T) new PurchasesActivity(params[0], new BigDecimal(params[1]), Integer.parseInt(params[2]), params[3], params[4]);
            case "announcement":
                return (T) new Announcement(params[1], params[2]);

        }
        return null;
    }


    EventHandler<Event> getEventHandler() {
        return eventHandler;
    }

}
