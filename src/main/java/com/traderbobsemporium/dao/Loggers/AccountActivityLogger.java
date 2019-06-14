package main.java.com.traderbobsemporium.dao.Loggers;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.controlsfx.dialog.ExceptionDialog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityLogger implements DAO<AccountActivity> {
    private final String receiveQuery = "SELECT * FROM accountactivity ";
    private List<AccountActivity> queue = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean();

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
            accountActivity.setActivityType(ActivityType.valueOf(params[1]));
        if (!params[2].isEmpty())
            accountActivity.setAffectedId(Long.parseLong(params[2]));
        if (!params[3].isEmpty())
            accountActivity.setAffectedName(params[3]);
        if (!params[4].isEmpty())
            accountActivity.setDateTime(params[4]);
        update(accountActivity);
    }

    @Override
    public void update(AccountActivity updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountactivity SET " +
                     "username = '" + updated.getName() + "'," +
                     "activityType = '" + updated.getActivityType().name() + "'," +
                     "affectedID = '" + updated.getAffectedId() + "'," +
                     "affectedName = '" + updated.getAffectedName() + "'," +
                     "dateTime = '" + updated.getDateTime() + "'" +
                     " WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    public void start(){
        running.set(true);
        new Thread(this::update).start();
    }

    private void update(){
        while(running.get()) {
            for (int i = 0; i < queue.size(); i++)
               logToDatabase(queue.get(i));
            queue.clear();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void logToDatabase(AccountActivity accountActivity){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountactivity VALUES(' " +
                     accountActivity.getId() + "','" + accountActivity.getName() + "','" + accountActivity.getActivityType() + "','" +
                     accountActivity.getAffectedId() + "','" + accountActivity.getAffectedName() + "','" + Util.dateTime() + "')")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        running.set(false);
    }

    @Override
    public void add(AccountActivity accountActivity) {
        queue.add(accountActivity);
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
