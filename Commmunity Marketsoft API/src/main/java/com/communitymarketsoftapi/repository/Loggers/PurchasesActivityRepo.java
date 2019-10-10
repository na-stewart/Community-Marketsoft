package com.communitymarketsoftapi.repository.Loggers;

import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.model.logging.ActivityType;
import com.communitymarketsoftapi.model.logging.PurchasesActivity;
import com.communitymarketsoftapi.repository.Repo;
import com.communitymarketsoftapi.util.DbUtil;
import com.communitymarketsoftapi.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class PurchasesActivityRepo extends Repo<PurchasesActivity> {

    public PurchasesActivityRepo() {
        super("purchasesactivity");
    }

    @Override
    public void update(PurchasesActivity updated) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE purchasesactivity SET " +
                     "customerName = ?, itemId = ?, itemName = ?, itemType = ?, DATE = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setInt(2, updated.getItemId());
            preparedStatement.setString(3, updated.getItemName());
            preparedStatement.setString(4, updated.getItemType());
            preparedStatement.setString(5, updated.getDate());
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.ADD, updated));
        }
    }

    public List<PurchasesActivity> getPurchasesByCustomerAndDate(String customerName, String date) throws SQLException {
        List<PurchasesActivity> purchasesActivities = new ArrayList<>();
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM purchasesactivity WHERE customerName = ? AND date = ? AND communityId = ?")) {
            statement.setString(1, customerName);
            statement.setString(2, date);
            statement.setString(3, Util.LOGGED_IN_ACCOUNT_COMMUNITYID());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                   purchasesActivities.add(new PurchasesActivity(resultSet));
                }
                return purchasesActivities;
            }
        }
    }

    @Override
    public void add(PurchasesActivity purchasesActivity) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO purchasesactivity " +
                     "(id, customerName, itemId, itemName, itemType, DATE, communityId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, purchasesActivity.getId());
            preparedStatement.setString(2, purchasesActivity.getName());
            preparedStatement.setInt(3, purchasesActivity.getItemId());
            preparedStatement.setString(4, purchasesActivity.getItemName());
            preparedStatement.setString(5, purchasesActivity.getItemType());
            preparedStatement.setString(6, purchasesActivity.getDate());
            preparedStatement.setString(7, purchasesActivity.getCommunityId());
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, purchasesActivity));

        }
    }

}
