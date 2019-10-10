package com.communitymarketsoftapi.repository.Loggers;

import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.repository.Repo;
import com.communitymarketsoftapi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityRepo extends Repo<AccountActivity> {
    private static AccountActivityRepo accountActivityInstance;

    public AccountActivityRepo() {
        super("accountactivity");
    }

    @Override
    public void update(AccountActivity updated) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountactivity SET " +
                     "username = ?, activityType = ?, affectedID = ?, affectedName = ?, DATE = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getActivityType().name());
            preparedStatement.setInt(3, updated.getAffectedId());
            preparedStatement.setString(4, "affectedName");
            preparedStatement.setString(5, "date");
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();

        }
    }


    @Override
    public void add(AccountActivity accountActivity) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountactivity(id, " +
                     "username, activityType, affectedID, affectedName, date, communityId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, accountActivity.getId());
            preparedStatement.setString(2, accountActivity.getName());
            preparedStatement.setString(3, accountActivity.getActivityType().name());
            preparedStatement.setInt(4, accountActivity.getAffectedId());
            preparedStatement.setString(5, accountActivity.getAffectedName());
            preparedStatement.setString(6, accountActivity.getDate());
            preparedStatement.setString(7, accountActivity.getCommunityId());
            preparedStatement.execute();

        }
    }

    public static AccountActivityRepo getInstance()
    {
        if (accountActivityInstance == null)
            accountActivityInstance = new AccountActivityRepo();
        return accountActivityInstance;
    }

}
