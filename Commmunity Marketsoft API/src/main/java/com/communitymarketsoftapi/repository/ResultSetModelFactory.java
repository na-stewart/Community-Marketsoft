package com.communitymarketsoftapi.repository;

import com.communitymarketsoftapi.model.*;
import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.model.logging.PurchasesActivity;

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
        if (table.equals("accountactivity"))
            returnedModel = new AccountActivity(resultSet);
        if (table.equals("itemcategory"))
            returnedModel = new ItemCategory(resultSet);
        if (table.equals("purchasesactivity"))
            returnedModel = new PurchasesActivity((resultSet));
        return (T) returnedModel;
    }
}
