package com.communitymarketsoftapi.repository;

import com.communitymarketsoftapi.model.Customer;
import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.model.logging.ActivityType;
import com.communitymarketsoftapi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CustomerRepo extends Repo<Customer> {

    public CustomerRepo() {
        super("customer");
    }

    @Override
    public void update(Customer updated) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET " +
                     "name = ?, balance = ?, dailyLimit = ? WHERE id = ?")) {
            preparedStatement.setString(1,updated.getName());
            preparedStatement.setBigDecimal(2, updated.getBalance());
            preparedStatement.setInt(3, updated.getDailyLimit());
            preparedStatement.setInt(4, updated.getId());
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, updated));
        }

    }


    @Override
    public void add(Customer customer) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer " +
                     "(id, name, balance, dailyLimit, communityId) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setBigDecimal(3, customer.getBalance());
            preparedStatement.setInt(4, customer.getDailyLimit());
            preparedStatement.setString(5, customer.getCommunityId());
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.ADD, customer));
        }
    }
}
