package main.java.com.traderbobsemporium.controllers.Employee;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import main.java.com.traderbobsemporium.dao.DAO;
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
    abstract void update() throws SQLException, MalformedURLException;
    abstract void delete() throws SQLException;

    public PanelEventHandler(TableView tableView) {
        this.tableView = tableView;
    }

    void onEvent(Event event){
        if (event.getEventType().getName().equals("KEY_PRESSED"))
            onKeyEvent((KeyEvent) event);
        else
            onActionEvent((ActionEvent) event);

    }

    private void onKeyEvent(KeyEvent keyEvent){
        try {
            switch (keyEvent.getCode()) {
                case ENTER:
                    if (tableView.getSelectionModel().getSelectedItem() != null)
                        update();
                    else
                        add();
                    break;
                case BACK_SPACE:
                    delete();
                    break;
            }
        } catch (Exception e){
            new ExceptionDialog(e).showAndWait();
        }

    }

    private void onActionEvent(ActionEvent actionEvent){
        String buttonText = ((Button) actionEvent.getSource()).getText();
        try {
            switch (buttonText) {
                case "Update":
                    if (tableView.getSelectionModel().getSelectedItem() != null)
                        update();
                    else
                        add();
                    break;

                case "Delete":
                    delete();
                    break;
            }
        } catch (Exception e){
            new ExceptionDialog(e).showAndWait();
        }
    }




}
