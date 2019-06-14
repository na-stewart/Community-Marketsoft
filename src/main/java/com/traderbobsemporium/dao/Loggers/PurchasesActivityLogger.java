package main.java.com.traderbobsemporium.dao.Loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
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
public class PurchasesActivityLogger implements DAO<PurchasesActivity> {
    private final String receiveQuery = "SELECT * FROM purchasesactivity ";

    @Override
    public PurchasesActivity get(long id) throws SQLException {
        PurchasesActivity purchasesActivity = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = "+ id)) {
                if (resultSet.next())
                    purchasesActivity = new PurchasesActivity(resultSet);
                return purchasesActivity;
            }
        }
    }

    @Override
    public List<PurchasesActivity> getAll() throws SQLException {
        return getAll(null);
    }

    @Override
    public List<PurchasesActivity> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<PurchasesActivity> purchasesActivities = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    purchasesActivities.add(new PurchasesActivity(resultSet));
                return purchasesActivities;
            }
        }
    }

    @Override
    public void updateAll(PurchasesActivity purchasesActivity, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            purchasesActivity.setCamperName(params[0]);
        if (!params[1].isEmpty())
            purchasesActivity.setCamperBalance(new BigDecimal(params[1]));
        if (!params[2].isEmpty())
            purchasesActivity.setItemId(Long.parseLong(params[2]));
        if (!params[3].isEmpty())
            purchasesActivity.setItemName(params[3]);
        update(purchasesActivity);
    }

    @Override
    public void update(PurchasesActivity updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE purchasesactivity SET " +
                     "camperName = '" + updated.getCamperName() + "'," +
                     "camperBalance = '" + updated.getCamperBalance() + "'," +
                     "itemId = '" + updated.getItemId() + "'," +
                     "itemName = '" + updated.getItemName() + "'" +
                     " WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void add(PurchasesActivity purchasesActivity) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO purchasesactivity " +
                     "VALUES('" + purchasesActivity.getId() + "','" + purchasesActivity.getCamperName() + "','" +
                     purchasesActivity.getCamperBalance() + "','" + purchasesActivity.getItemId() + "','" +
                     purchasesActivity.getItemName() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(PurchasesActivity purchasesActivity) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM purchasesactivity WHERE id =" +
                     " '" + purchasesActivity.getId() + "'")) {
            preparedStatement.execute();
        }
    }



}
