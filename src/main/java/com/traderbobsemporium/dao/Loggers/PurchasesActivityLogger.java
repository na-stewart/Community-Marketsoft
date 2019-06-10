package main.java.com.traderbobsemporium.dao.Loggers;

import com.sun.org.apache.regexp.internal.RE;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.math.BigDecimal;
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
public class PurchasesActivityLogger implements DAO<PurchasesActivity> {
    @Override
    public PurchasesActivity get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("purchasesactivity WHERE id =" + id);
        PurchasesActivity purchasesActivity = null;
        if (resultSet.next())
            purchasesActivity = new PurchasesActivity(resultSet);
        resultSet.close();
        return purchasesActivity;
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
    public void updateAll(PurchasesActivity purchasesActivity, String[] params) {
        if (!params[0].isEmpty())
            purchasesActivity.setCamperName(params[0]);
        if (!params[1].isEmpty())
            purchasesActivity.setCamperBalance(BigDecimal.valueOf(Long.parseLong(params[1])));
        if (!params[2].isEmpty())
            purchasesActivity.setItemId(Long.parseLong(params[2]));
        if (!params[3].isEmpty())
            purchasesActivity.setItemName(params[3]);
        update(purchasesActivity);
    }

    @Override
    public void update(PurchasesActivity updated) {
        DatabaseUtil.UPDATE("UPDATE purchasesactivity SET camperName = '" + updated.getCamperName() + "'," +
                "camperBalance = '" + updated.getCamperBalance() + "'," +
                "itemId = '" + updated.getItemId() + "'," +
                "itemName = '" + updated.getItemName() + "'" +
                " WHERE id =" + updated.getId() + ";");
    }

    @Override
    public void add(PurchasesActivity purchasesActivity) {
        DatabaseUtil.UPDATE("INSERT INTO purchasesactivity VALUES('" + purchasesActivity.getId() + "','" +
                purchasesActivity.getCamperName() + "','" + purchasesActivity.getCamperBalance() + "','" +
                purchasesActivity.getItemId() + "','" + purchasesActivity.getItemName() + "')");
    }

    @Override
    public void delete(long id) {
        DatabaseUtil.UPDATE("DELETE FROM purchasesactivity WHERE id = '" + id  + "'");

    }


}
