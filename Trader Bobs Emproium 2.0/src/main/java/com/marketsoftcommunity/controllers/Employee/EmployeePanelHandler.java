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
import main.java.com.marketsoftcommunity.controllers.Employee.EventType;
import main.java.com.marketsoftcommunity.model.Model;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.Util;
import org.controlsfx.control.table.TableFilter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public abstract class EmployeePanelHandler<T> {
    private EventType eventType;
    private TableView<T> tableView;
    private GeneralDTO<T> dto;
    private EventHandler<Event> eventHandler = this::onEvent;


    EmployeePanelHandler(TableView<T> tableView, GeneralDTO<T> dao) {
        this.tableView = tableView;
        this.dto = dao;
        try {
            populateTableViewWithObservableList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        //refreshTables();


    }

    abstract T panelModel();

    abstract void updateSelectedModel(T t);

    abstract void populateFields();

    abstract void clearFields();

    public void postEvent() {
    }


    private void refreshTables(){
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                populateTableViewWithObservableList();
                System.out.println("test");
            } catch (Exception e) {
                e.printStackTrace();
                ses.shutdown();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

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
            } else if (keyEvent.getCode() == KeyCode.DELETE)
                delete();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
            Util.displayAlert("Could not complete request! Reasons may include the following:\n-When creating an account, you used an email that already exists within the database." +
                    "\n-A field reached the max amount of characters allowed.\n-You cannot change the email of a community owner.\n-Insufficient permissions", Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Util.displayAlert("A field that requires a number has a non numerical character!", Alert.AlertType.ERROR);
        } catch (ApiException e) {
            e.printStackTrace();
            Util.displayAlert("Could not complete request! Reasons may include the following:\n-When creating an account, you used an email that already exists within the database." +
                    "\n-A field reached the max amount of characters allowed.\n-You cannot change the email of a community owner.\n-Insufficient permissions", Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add() throws IOException, ApiException {
        eventType = EventType.ADD;
        T model = panelModel();
        dto.add(model);
        allAfterEvents();

    }

    private void update() throws IOException, ApiException {
        eventType = EventType.UPDATE;
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            updateSelectedModel(t);
            dto.update(t);
        }
        allAfterEvents();
    }

    private void delete() throws IOException, ApiException {
        eventType = EventType.DELETE;
        Util.displayAlert("Are you sure you want to delete these items? This data is not recoverable!", Alert.AlertType.CONFIRMATION);
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            Model model = (Model) t;
            dto.delete(model.getId());
        }
        allAfterEvents();
    }


    private void allAfterEvents() throws IOException, ApiException {
        postEvent();
        populateTableViewWithObservableList();
        clearFields();
    }


    private void onMouseEvent(MouseEvent mouseEvent) {
        String name = mouseEvent.getSource().getClass().getSimpleName();
        if (name.equals("TableView") && tableView.getSelectionModel().getSelectedItems().size() == 1)
            populateFields();
        else
            clearFields();
    }


    private void populateTableViewWithObservableList() throws IOException, ApiException {
        if (AuthUtil.hasPermission(dto.getModel() + ":display")) {
            ObservableList<T> observableList;
            observableList = FXCollections.observableArrayList(dto.getAll());
            tableView.setItems(observableList);
            TableFilter.forTableView(tableView).apply();
        }
    }


    EventHandler<Event> getEventHandler() {
        return eventHandler;
    }

    public EventType getEventType() {
        return eventType;
    }
}
