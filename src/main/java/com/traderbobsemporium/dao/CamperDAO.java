package main.java.com.traderbobsemporium.dao;


import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CamperDAO extends DAO<Camper> {

    public CamperDAO() {
        super("camper");
    }

    @Override
    public void updateAll(Camper camper, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            camper.setName(params[0]);
        if (!params[1].isEmpty())
            camper.setBalance(new BigDecimal(params[1]));
        update(camper);
    }

    @Override
    public void update(Camper updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE camper SET " +
                     "name = ?, balance = ? WHERE id = ?")) {
            preparedStatement.setString(1,updated.getName());
            preparedStatement.setBigDecimal(2, updated.getBalance());
            preparedStatement.setInt(3, updated.getId());
            preparedStatement.execute();
        }

    }


    @Override
    public void add(Camper camper) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO camper " +
                     "(id, name, balance) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, camper.getId());
            preparedStatement.setString(2, camper.getName());
            preparedStatement.setBigDecimal(3, camper.getBalance());
            preparedStatement.execute();
        }
    }

}
