package main.java.com.traderbobsemporium.controllers.Employee;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
    private Object object;

    @RequiresAuthentication
    abstract void add() throws SQLException;
    @RequiresAuthentication
    abstract void update() throws SQLException;
    @RequiresAuthentication
    abstract void delete() throws SQLException;
    abstract void onSuccessfulEvent();

    public PanelEventHandler(TableView tableView) {
        this.object = tableView.getSelectionModel().getSelectedItem();
    }

    void onEvent(Event event){
        if (event.getEventType().getName().equals("KEY_PRESSED"))
            onKeyEvent((KeyEvent) event);
        else
            onActionEvent((ActionEvent) event);

    }

    private void onKeyEvent(KeyEvent keyEvent){
        try {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (object != null)
                    update();
                else
                    add();
                onSuccessfulEvent();
            } else if (keyEvent.getCode() == KeyCode.DELETE) {
                delete();
                onSuccessfulEvent();
            }
        } catch (Exception e){
            new ExceptionDialog(e).showAndWait();
            e.printStackTrace();
        }
    }

    private void onActionEvent(ActionEvent actionEvent){
        String buttonText = ((Button) actionEvent.getSource()).getText();
        try {
            if (buttonText.equals("Update")){
                if (object != null)
                    update();
                else
                    add();
                onSuccessfulEvent();
            } else {
                delete();
                onSuccessfulEvent();
            }
        } catch (Exception e){
            new ExceptionDialog(e).showAndWait();
            e.printStackTrace();
        }
    }




}
