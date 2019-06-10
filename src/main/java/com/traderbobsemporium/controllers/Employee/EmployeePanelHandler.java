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
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.authz.AuthorizationException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
abstract class EmployeePanelHandler {
    private TableView tableView;
    private EventHandler<Event> eventHandler = this::onEvent;

    public EmployeePanelHandler(TableView tableView) {
        this.tableView = tableView;
    }

    abstract void add();

    abstract void update();

    abstract void delete();

    abstract void afterEvent();

    abstract void clearFields();

    abstract void populateFields();

    void onEvent(Event event) {
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
                afterEvent();
                clearFields();
            } else if (keyEvent.getCode() == KeyCode.DELETE) {
                delete();
                afterEvent();
                clearFields();
            }
        } catch (AuthorizationException e) {
            Util.displayAlert("Insufficient Permissions!", Alert.AlertType.WARNING);
        }
    }

    private void onActionEvent(ActionEvent actionEvent) {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        try {
          switch (buttonText){
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
            afterEvent();
            clearFields();
            System.out.println(DatabaseUtil.SUCCESSFUL_QUERY);

        }
         catch (AuthorizationException e) {
            Util.displayAlert("Insufficient Permissions!", Alert.AlertType.WARNING);
        }
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



}
