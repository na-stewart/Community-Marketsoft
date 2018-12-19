package Data;

import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.Item.Item;
import Data.Item.ItemType;
import Tables.TableType;
import Util.LoggedInAccountUtil;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class DataObjectSupplier {
    private ResultSet resultSet;

    public DataObjectSupplier(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private Camper camperData() throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int balance = resultSet.getInt(3);
        return new Camper(id, name, balance);
    }

    private Item itemData() throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int price = resultSet.getInt(3);
        String imageUrl = resultSet.getString(4);
        ItemType itemType = ItemType.intToItemType(resultSet.getInt(5));
        return new Item(id, name, price, imageUrl, itemType);

    }

    private Employee employeeData() throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        EmployeeType accountType = EmployeeType.intToEmployeeType(resultSet.getInt(4));
        return new Employee(id, username, password, accountType);
    }

    public Object getData(TableType tableType) throws SQLException {
        switch (tableType) {
            case EMPLOYEE:
                return employeeData();
            case ITEM:
                return itemData();
            case CAMPER:
                return camperData();
        }
        return null;
    }
}

