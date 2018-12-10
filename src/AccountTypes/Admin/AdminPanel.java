package AccountTypes.Admin;

import Data.Customers.EmployeeType;
import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Item.Item;
import Data.Item.ItemType;
import Manager.DbManagers.DatabaseManager;
import Manager.Interfaces.MultiQuery;
import Manager.DbTable;
import Util.LoggedInAccountUtil;
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
    public void retrieveDatabaseData(DbTable tables, Object dataViewer) throws SQLException {
        String tableName = tables.name().toLowerCase();
        ResultSet resultSet = databaseManager.receiver("SELECT * FROM " + tableName);
        TableView tableView = (TableView) dataViewer;
        ObservableList list = FXCollections.observableArrayList();
        while (resultSet.next()) {
            switch (tableName) {
                case "employee": populateEmployee(resultSet, list);

                    break;
                case "item":
                    populateItem(resultSet, list);
                    break;
                case "camper":
                    populateCamper(resultSet, list);
                    break;

            }
        }
        tableView.setItems(list);
        resultSet.close();
    }

    private void populateEmployee(ResultSet resultSet, ObservableList<Employee> employeeList) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        EmployeeType accountType = EmployeeType.intToEmployeeType(resultSet.getInt(4));
        employeeList.add(new Employee(id, username, password, accountType));
        if (LoggedInAccountUtil.thisAccountType == EmployeeType.EMPLOYEE)
           employeeList.removeIf(this::removeForbiddenAccountVisibility);

    }

    private Boolean removeForbiddenAccountVisibility(Employee employee){
        return employee.getAccountType() == EmployeeType.EMPLOYEE ||
                employee.getAccountType() == EmployeeType.ADMIN ||
                employee.getAccountType() == EmployeeType.UNCONFIRMED;
    }

    private void populateItem(ResultSet resultSet, ObservableList<Item> itemList) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int price = resultSet.getInt(3);
        String imageUrl = resultSet.getString(4);
        ItemType itemType = ItemType.intToItemType(resultSet.getInt(5));
        itemList.add(new Item(id, name, price, imageUrl, itemType));
    }

    private void populateCamper(ResultSet resultSet, ObservableList<Camper> camperList) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int balance = resultSet.getInt(3);
        camperList.add(new Camper(id, name, balance));
    }

}
