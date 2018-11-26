package AccountTypes.Admin;

import AccountTypes.AccountType;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Manager.DatabaseManager;
import Manager.DatabaseViewer;
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

    @Override
    public void updateDatabase(String query) throws SQLException {
        databaseManager.update(query);
    }

    @Override
    public void retrieveDatabaseData(String query, DatabaseViewer dataViewer) throws SQLException {
        ResultSet resultSet = databaseManager.receiver(query);
        TableView tableView = (TableView) dataViewer.getDataViewer();
        ObservableList list = FXCollections.observableArrayList();
        while (resultSet.next()) {
          switch (dataViewer.getTable()) {
              case "employee":
                  populateEmployee(resultSet, list);
                  break;
              case "camper":
                  populateCamper(resultSet, list);
                  break;
              case "consumable":
                  break;
          }
        }
        tableView.setItems(list);
    }

    private void populateEmployee(ResultSet resultSet, ObservableList<Employee> employeeList) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        AccountType accountType = AccountType.intToAccountTypePerms(resultSet.getInt(4));
        String macAddress = resultSet.getString(5);
        employeeList.add(new Employee(id, username, password, accountType, macAddress));

    }

    private void populateCamper(ResultSet resultSet, ObservableList<Camper> camperList) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int balance = resultSet.getInt(3);
        camperList.add(new Camper(id, name, balance));


    }
}
