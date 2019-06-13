package main.java.com.traderbobsemporium.dao;


import main.java.com.traderbobsemporium.model.Announcement;
import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

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
public class CamperDAO implements DAO<Camper> {
    private final String receiveQuery = "SELECT * FROM camper ";

    @Override
    public Camper get(long id) throws SQLException {
        Camper camper = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = " + id)) {
                if (resultSet.next())
                    camper = new Camper(resultSet);
                return camper;
            }
        }
    }

    @Override
    public List<Camper> getAll() throws SQLException {
        return getAll(null);
    }

    @Override
    public List<Camper> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<Camper> campers = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    campers.add(new Camper(resultSet));
                return campers;
            }
        }
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
                     "name = '" + updated.getName() + "'," +
                     "balance = '" + updated.getBalance() + "' WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }

    }


    @Override
    public void add(Camper camper) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO camper VALUES" +
                     "('" + camper.getId() + "','" + camper.getName() + "','" + camper.getBalance() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Camper camper) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM camper WHERE id = '" + camper.getId() + "'")) {
            preparedStatement.execute();
        }
    }

}
