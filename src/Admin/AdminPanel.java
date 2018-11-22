package Admin;

import AccountTypes.AccountType;
import Data.Camper;
import Data.Employee;
import Manager.DatabaseManager;
import Manager.MultiQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class AdminPanel implements MultiQuery {

    private DatabaseManager databaseManager = new DatabaseManager();
    private ResultSet resultSet;

    @Override
    public void updateDatabase(String query) throws SQLException {
        databaseManager.update(query);
    }

    @Override
    public void retrieveDatabaseData(String query, Object dataViewer) throws SQLException {
        resultSet = databaseManager.receiver(query);
        while (resultSet.next()) {
            if (query.contains("employee"))
                populateEmployeeTable((TableView<Employee>) dataViewer);
            else if (query.contains("camper"))
                populateCamperTable((TableView<Camper>) dataViewer);
            //else if (query.contains("consumable"))
        }
    }

    private void populateEmployeeTable(TableView<Employee> tableView) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        AccountType accountType = AccountType.intToAccountTypePerms(resultSet.getInt(4));
        String macAddress = resultSet.getString(5);
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        employeeList.add(new Employee(id, username, password, accountType, macAddress));
        tableView.setItems(employeeList);
    }

    private void populateCamperTable(TableView<Camper> tableView) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int balance = resultSet.getInt(3);
        ObservableList<Camper> camperList = FXCollections.observableArrayList();
        camperList.add(new Camper(id, name, balance));
        tableView.setItems(camperList);
    }
}
