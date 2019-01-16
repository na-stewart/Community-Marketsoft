package Data;

import Data.Customers.Camper;
import Data.Customers.Employee;
import Data.Customers.EmployeeType;
import Data.Item.Item;
import Data.Item.ItemType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class DataObjectBuilder {
    private ResultSet resultSet;

    public DataObjectBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private Camper camperData() throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int balance = resultSet.getInt("balance");
        return new Camper(id, name, balance);
    }

    private Item itemData() throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        String imageUrl = resultSet.getString("imageurl");
        ItemType itemType = ItemType.intToItemType(resultSet.getInt("itemtype"));
        return new Item(id, name, price, imageUrl, itemType);

    }

    private Employee employeeData() throws SQLException {
        int id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        EmployeeType accountType = EmployeeType.intToEmployeeType(resultSet.getInt("employeetype"));
        return new Employee(id, username, password, accountType);
    }

    public Object getData(String tableType) throws SQLException {
        switch (tableType) {
            case "employee":
                return employeeData();
            case "item":
                return itemData();
            case "camper":
                return camperData();
        }
        return null;
    }
}

