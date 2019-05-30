package main.java.com.traderbobsemporium.controllers.Employee;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.controlsfx.dialog.ExceptionDialog;



import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
abstract class PanelEventHandler implements EventHandler<Event> {
    private TableView tableView;

    abstract void add() throws SQLException;

    abstract void update() throws SQLException;

    abstract void delete() throws SQLException;

    abstract void onSuccessfulEvent();

    PanelEventHandler(TableView tableView) {
        this.tableView = tableView;
    }

    void onEvent(Event event) {
        switch (event.getEventType().getName()) {
            case "KEY_PRESSED":
                onKeyEvent((KeyEvent) event);
                break;
            case "ACTION":
                onActionEvent((ActionEvent) event);
                break;
            case "MOUSE_PRESSED":
                if (tableView.getSelectionModel().getSelectedItem() != null)
                    onSuccessfulEvent();

                break;
        }

    }

    private void onKeyEvent(KeyEvent keyEvent) {
        try {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (tableView.getSelectionModel().getSelectedItem() != null)
                    update();
                else
                    add();
                onSuccessfulEvent();
            } else if (keyEvent.getCode() == KeyCode.DELETE) {
                delete();
                onSuccessfulEvent();
            }
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
            e.printStackTrace();
        } catch (AuthorizationException e) {
            Util.displayError("You are not authorized to execute this command", Alert.AlertType.ERROR);
        }
    }

    private void onActionEvent(ActionEvent actionEvent) {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        try {
            if (buttonText.equals("Update")) {
                if (tableView.getSelectionModel().getSelectedItem() != null)
                    update();
                else
                    add();
                onSuccessfulEvent();
            } else {
                delete();
                onSuccessfulEvent();
            }
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
            e.printStackTrace();
        } catch (AuthorizationException e) {
            Util.displayError("You are not authorized to execute this command", Alert.AlertType.ERROR);
        }
    }
}
