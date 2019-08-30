package main.java.com.traderbobsemporium.dao.Loggers;
import main.java.com.traderbobsemporium.model.logging.AccountActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityLogger extends ThreadedDAO<AccountActivity> {
    private List<AccountActivity> queue = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean();

    public AccountActivityLogger() {
        super("accountactivity");
    }

    @Override
    public void update(AccountActivity updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountactivity SET " +
                     "username = ?, activityType = ?, affectedID = ?, affectedName = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getActivityType().name());
            preparedStatement.setInt(3, updated.getAffectedId());
            preparedStatement.setString(4, "affectedName");
            preparedStatement.setString(5, "date");
            preparedStatement.setInt(6, updated.getId());
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
                LoggingUtil.logExceptionToFile(e);
            }
        }
    }

    private void logToDatabase(AccountActivity accountActivity){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountactivity(id, " +
                     "username, activityType, affectedID, affectedName, date) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, accountActivity.getId());
            preparedStatement.setString(2, accountActivity.getName());
            preparedStatement.setString(3, accountActivity.getActivityType().name());
            preparedStatement.setInt(4, accountActivity.getAffectedId());
            preparedStatement.setString(5, accountActivity.getAffectedName());
            preparedStatement.setString(6, accountActivity.getDate());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    public void stop(){
        running.set(false);
    }

    @Override
    public void add(AccountActivity accountActivity) {
        queue.add(accountActivity);
    }

}
