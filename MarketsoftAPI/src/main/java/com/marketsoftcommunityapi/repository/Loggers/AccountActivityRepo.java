package main.java.com.marketsoftcommunityapi.repository.Loggers;

import main.java.com.marketsoftcommunityapi.model.logging.AccountActivity;
import main.java.com.marketsoftcommunityapi.util.DbUtil;
import main.java.com.marketsoftcommunityapi.util.LoggingUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityRepo extends LogRepo<AccountActivity> {
    private List<AccountActivity> queue = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean();

    public AccountActivityRepo() {
        super("accountactivity");
    }

    @Override
    public void update(AccountActivity updated) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountactivity SET " +
                     "username = ?, activityType = ?, affectedID = ?, affectedName = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getActivityType().name());
            preparedStatement.setInt(3, updated.getAffectedId());
            preparedStatement.setString(4, "affectedName");
            preparedStatement.setString(5, "date");
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
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
