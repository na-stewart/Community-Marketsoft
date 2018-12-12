package Data;

import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.Item.Item;
import Data.Item.ItemType;
import Util.LoggedInAccountUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class ObservableDataSupplier {

    public void populateCamperList(ResultSet resultSet, ObservableList<Camper> campers) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int balance = resultSet.getInt(3);
        campers.add(new Camper(id, name, balance));
    }

    public void populateItemList(ResultSet resultSet, ObservableList<Item> items) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int price = resultSet.getInt(3);
        String imageUrl = resultSet.getString(4);
        ItemType itemType = ItemType.intToItemType(resultSet.getInt(5));
        items.add(new Item(id, name, price, imageUrl, itemType));

    }

    public void populateEmployeeList(ResultSet resultSet, ObservableList<Employee> employees) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        EmployeeType accountType = EmployeeType.intToEmployeeType(resultSet.getInt(4));
        employees.add(new Employee(id, username, password, accountType));
        if (LoggedInAccountUtil.thisAccountType == EmployeeType.EMPLOYEE)
            employees.removeIf(this::removeForbiddenAccountVisibility);

    }
    private Boolean removeForbiddenAccountVisibility(Employee employee){
        return employee.getAccountType() == EmployeeType.EMPLOYEE ||
                employee.getAccountType() == EmployeeType.ADMIN ||
                employee.getAccountType() == EmployeeType.UNCONFIRMED;
    }


}
