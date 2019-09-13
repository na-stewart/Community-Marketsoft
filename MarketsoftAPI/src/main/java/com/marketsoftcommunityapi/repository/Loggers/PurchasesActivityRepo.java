package main.java.com.marketsoftcommunityapi.repository.Loggers;

import main.java.com.marketsoftcommunityapi.model.logging.PurchasesActivity;
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
public class PurchasesActivityRepo extends LogRepo<PurchasesActivity> {
    private List<PurchasesActivity> queue = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean();

    public PurchasesActivityRepo() {
        super("purchasesactivity");
    }

    @Override
    public void update(PurchasesActivity updated) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE purchasesactivity SET " +
                     "customerName = ?, itemId = ?, itemName = ?, itemType = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setInt(2, updated.getItemId());
            preparedStatement.setString(3, updated.getItemName());
            preparedStatement.setString(4, updated.getItemType());
            preparedStatement.setString(5, updated.getDate());
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void add(PurchasesActivity purchasesActivity) {
        queue.add(purchasesActivity);

    }

    public void start(){
        running.set(true);
        new Thread(this::update).start();

    }

    @Override
    public void stop() {
        running.set(false);

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

    private void logToDatabase(PurchasesActivity purchasesActivity){
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO purchasesactivity " +
                     "(id, customerName, itemId, itemName, itemType, Date) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, purchasesActivity.getId());
            preparedStatement.setString(2, purchasesActivity.getName());
            preparedStatement.setInt(3, purchasesActivity.getItemId());
            preparedStatement.setString(4, purchasesActivity.getItemName());
            preparedStatement.setString(5, purchasesActivity.getItemType());
            preparedStatement.setString(6, purchasesActivity.getDate());
            preparedStatement.execute();

        } catch (SQLException e) {
          e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }
}
