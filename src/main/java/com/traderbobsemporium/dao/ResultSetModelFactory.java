package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.*;
import main.java.com.traderbobsemporium.model.logging.AccountActivity;
import main.java.com.traderbobsemporium.model.logging.Announcement;
import main.java.com.traderbobsemporium.model.logging.PurchasesActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
class ResultSetModelFactory<T> {
    private String table;

    ResultSetModelFactory(String query) {
        this.table = query;
    }



    @SuppressWarnings("unchecked")
    T getModel(ResultSet resultSet) throws SQLException {
        Model returnedModel = null;
        if (table.equals("customer"))
            returnedModel = new Customer(resultSet);
        if (table.equals("account"))
            returnedModel = new Account(resultSet);
        if (table.equals("item"))
            returnedModel = new Item(resultSet);
        if (table.equals("accountpermissions"))
            returnedModel = new AccountPermission(resultSet);
        if (table.equals("accountactivity"))
            returnedModel = new AccountActivity(resultSet);
        if (table.equals("announcement"))
            returnedModel = new Announcement(resultSet);
        if (table.equals("purchasesactivity"))
            returnedModel = new PurchasesActivity((resultSet));
        return (T) returnedModel;
    }
}
