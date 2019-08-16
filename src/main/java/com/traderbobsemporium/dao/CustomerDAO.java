package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Customer;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CustomerDAO extends DAO<Customer>{

    public CustomerDAO() {
        super("customer");
    }

    @Override
    public void updateAll(Customer customer, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            customer.setName(params[0]);
        if (!params[1].isEmpty())
            customer.setBalance(new BigDecimal(params[1]));
        update(customer);
    }

    @Override
    public void update(Customer updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET " +
                     "name = ?, balance = ? WHERE id = ?")) {
            preparedStatement.setString(1,updated.getName());
            preparedStatement.setBigDecimal(2, updated.getBalance());
            preparedStatement.setInt(3, updated.getId());
            preparedStatement.execute();
        }

    }


    @Override
    public void add(Customer customer) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer " +
                     "(id, name, balance) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setBigDecimal(3, customer.getBalance());
            preparedStatement.execute();
        }
    }
}
