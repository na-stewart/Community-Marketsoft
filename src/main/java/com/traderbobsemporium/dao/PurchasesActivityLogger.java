package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.net.MalformedURLException;
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
class PurchasesActivityLogger implements DAO<PurchasesActivity> {
    @Override
    public PurchasesActivity get(long id) throws SQLException {
        return null;
    }

    @Override
    public List<PurchasesActivity> getAll() throws SQLException {
        ArrayList<PurchasesActivity> purchases = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("purchasesactivity");
        while (resultSet.next())
            purchases.add(new PurchasesActivity(resultSet));
        resultSet.close();
        return purchases;
    }

    @Override
    public void update(PurchasesActivity purchasesActivity, String[] params) throws SQLException{

    }

    @Override
    public void add(PurchasesActivity purchasesActivity) throws SQLException {

    }

    @Override
    public void delete(long id) throws SQLException {

    }
}
