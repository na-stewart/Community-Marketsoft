package main.java.com.traderbobsemporium.dao.loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class PurchasesActivityLogger implements Logger<PurchasesActivity> {
    @Override
    public void log(PurchasesActivity purchasesActivity) throws SQLException {

    }

    @Override
    public void deleteLog(long id) throws SQLException {

    }

    @Override
    public ArrayList<PurchasesActivity> getLogs() throws SQLException {
        ArrayList<PurchasesActivity> purchases = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("purchasesactivity");
        while (resultSet.next())
            purchases.add(new PurchasesActivity(resultSet));
        resultSet.close();
        return purchases;
    }
}
