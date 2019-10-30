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
import main.java.com.marketsoftcommunity.model.Model;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.Util;
import org.controlsfx.control.table.TableFilter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
        loadTableViewWithPermissions();
        continuallyRefreshTableViews();


    }

    private void loadTableViewWithPermissions(){
        if (AuthUtil.hasPermission(dto.getModel() + ":display")) {
            try {
                populateTableViewWithObservableList();
            } catch (IOException | ApiException e) {
                e.printStackTrace();
            }
        }
    }

    abstract T panelModel();

    abstract void updateSelectedModel(T t);

    abstract void populateFields();

    abstract void clearFields();

    public void postEvent() {
    }


    private void continuallyRefreshTableViews(){
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                if (tableView.getSelectionModel().getSelectedItem() == null)
                    populateTableViewWithObservableList();
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
        }catch (NumberFormatException e) {
            e.printStackTrace();
            Util.displayAlert("A field that required a number value has a non numeric value.", Alert.AlertType.ERROR);
        } catch (ApiException  | IOException e) {
            e.printStackTrace();
            Util.displayAlert("Could not complete request! Reasons may include the following:\n-Insufficient permissions " +
                    "\n-When creating an account, you used an email that already exists within the database." + "\n-A field reached the max amount of characters allowed." +
                    "\n-You cannot manipulate community owner information.", Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Util.displayAlert("A critical field has been left blank.", Alert.AlertType.ERROR);
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
            Util.displayAlert("A field that required a number value has a non numeric value.", Alert.AlertType.ERROR);
        } catch (ApiException  | IOException e) {
            e.printStackTrace();
            Util.displayAlert("Could not complete request! Reasons may include the following:\n-When creating an account, you used an email that already exists within the database." +
                    "\n-A field reached the max amount of characters allowed.\n-You cannot change the email of a community owner.\n-Insufficient permissions", Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Util.displayAlert("A critical field has been left blank.", Alert.AlertType.ERROR);
        }
    }

    private void add() throws IOException, ApiException {
        if (eventType != EventType.DELETE) {
            T model = panelModel();
            dto.add(model);
        }
        eventType = EventType.ADD;
        allAfterEvents();

    }

    private void update() throws IOException, ApiException {

        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            updateSelectedModel(t);
            dto.update(t);
        }
        eventType = EventType.UPDATE;
        allAfterEvents();
    }

    private void delete() throws IOException, ApiException {
        Util.displayAlert("Are you sure you want to delete these items? This data is not recoverable!", Alert.AlertType.CONFIRMATION);
        for (T t : tableView.getSelectionModel().getSelectedItems()) {
            Model model = (Model) t;
            dto.delete(model.getId());
        }
        eventType = EventType.DELETE;
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
        ObservableList<T> observableList;
        observableList = FXCollections.observableArrayList(dto.getAll());
        tableView.setItems(observableList);
        TableFilter.forTableView(tableView).apply();

    }


    EventHandler<Event> getEventHandler() {
        return eventHandler;
    }

    public EventType getEventType() {
        return eventType;
    }
}
