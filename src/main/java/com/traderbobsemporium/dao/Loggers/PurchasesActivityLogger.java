package main.java.com.traderbobsemporium.dao.Loggers;

import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import org.controlsfx.dialog.ExceptionDialog;

import java.math.BigDecimal;
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
public class PurchasesActivityLogger extends ThreadedDAO<PurchasesActivity> {
    private List<PurchasesActivity> queue = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean();

    public PurchasesActivityLogger() {
        super("purchasesactivity");
    }

    @Override
    public void updateAll(PurchasesActivity purchasesActivity, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            purchasesActivity.setName(params[0]);
        if (!params[1].isEmpty())
            purchasesActivity.setCamperBalance(new BigDecimal(params[1]));
        if (!params[2].isEmpty())
            purchasesActivity.setItemId(Integer.parseInt(params[2]));
        if (!params[3].isEmpty())
            purchasesActivity.setItemName(params[3]);
        if (!params[4].isEmpty())
            purchasesActivity.setDate(params[4]);
        update(purchasesActivity);
    }

    @Override
    public void update(PurchasesActivity updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE purchasesactivity SET " +
                     "camperName = ?, camperBalance = ?, itemId = ?, date = ?, itemName = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setBigDecimal(2, updated.getCamperBalance());
            preparedStatement.setInt(3, updated.getItemId());
            preparedStatement.setString(4, updated.getItemName());
            preparedStatement.execute();
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
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO purchasesactivity " +
                     "(id, camperName, camperBalance, itemId, date, itemName) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, purchasesActivity.getId());
            preparedStatement.setString(2, purchasesActivity.getName());
            preparedStatement.setBigDecimal(3, purchasesActivity.getCamperBalance());
            preparedStatement.setInt(4, purchasesActivity.getItemId());
            preparedStatement.setString(5, purchasesActivity.getDate());
            preparedStatement.setString(6, purchasesActivity.getItemName());
            preparedStatement.execute();

        } catch (SQLException e) {
          e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

}
