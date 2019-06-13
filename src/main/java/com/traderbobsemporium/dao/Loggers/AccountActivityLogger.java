package main.java.com.traderbobsemporium.dao.Loggers;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityLogger implements DAO<AccountActivity> {
    private final String receiveQuery = "SELECT * FROM accountactivity ";

    @Override
    public AccountActivity get(long id) throws SQLException {
        AccountActivity accountActivity = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = "+ id)) {
                if (resultSet.next())
                    accountActivity = new AccountActivity(resultSet);
                return accountActivity;
            }
        }
    }

    @Override
    public List<AccountActivity> getAll() throws SQLException {
        return getAll(null);
    }

    @Override
    public List<AccountActivity> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '"+ clause[1] + "'": receiveQuery;
        List<AccountActivity> accountActivities = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    accountActivities.add(new AccountActivity(resultSet));
                return accountActivities;
            }
        }
    }

    @Override
    public void updateAll(AccountActivity accountActivity, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            accountActivity.setName(params[0]);
        if (!params[1].isEmpty())
            accountActivity.setIp(params[1]);
        if (!params[2].isEmpty())
            accountActivity.setMac(params[2]);
        if (!params[3].isEmpty())
            accountActivity.setActivityType(ActivityType.valueOf(params[3]));
        if (!params[4].isEmpty())
            accountActivity.setAffectedId(Long.parseLong(params[4]));
        if (!params[5].isEmpty())
            accountActivity.setAffectedName(params[5]);
        if (!params[6].isEmpty())
            accountActivity.setDateTime(params[6]);
        update(accountActivity);
    }

    @Override
    public void update(AccountActivity updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountactivity SET username = '" + updated.getName() + "'," +
                     "ip = '" + updated.getIp() + "'," +
                     "mac = '" + updated.getMac() + "'," +
                     "activityType = '" + updated.getActivityType().name() + "'," +
                     "affectedID = '" + updated.getAffectedId() + "'," +
                     "affectedName = '" + updated.getAffectedName() + "'," +
                     "dateTime = '" + updated.getDateTime() + "'" +
                     " WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }


    @Override
    public void add(AccountActivity accountActivity) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountactivity VALUES(' "+
                     accountActivity.getId() +  "','" + accountActivity.getName() + "','" + accountActivity.getIp()
                     + "','" + accountActivity.getMac() + "','" + accountActivity.getActivityType() + "','" + accountActivity.getAffectedId() + "','" +
                     accountActivity.getAffectedName() + "','" + Util.dateTime() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(AccountActivity accountActivity) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM accountactivity WHERE " +
                     "id = '" + accountActivity.getId() + "'")) {
            preparedStatement.execute();
        }
    }
}
