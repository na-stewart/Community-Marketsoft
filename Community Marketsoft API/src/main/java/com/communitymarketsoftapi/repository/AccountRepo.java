package com.communitymarketsoftapi.repository;

import com.communitymarketsoftapi.model.Account;
import com.communitymarketsoftapi.model.exception.CommunityOwnerUpdateException;
import com.communitymarketsoftapi.model.exception.InvalidEmailFormatException;
import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.model.logging.ActivityType;
import com.communitymarketsoftapi.util.DbUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountRepo extends Repo<Account> {
    private boolean register = false;
    private DefaultPasswordService passwordService = new DefaultPasswordService();


    public AccountRepo() {
        super("account");
    }

    public Account getByUsername(String username) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM account WHERE username=?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Account(resultSet);
            }
        }
       return null;
    }

    @Override
    public void update(Account updated) throws SQLException {
        checkIfEmailIsValid(updated.getName());
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "username = ?, password = ?, accountRole = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, pass(updated));
            preparedStatement.setString(3, updated.getAccountRole().name());
            preparedStatement.setInt(4, updated.getId());
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, updated));
        }
    }

    public void updateAllAccountsWithSubscriptionId(String currentSubscriptionId, String newSubscriptionId) throws SQLException, StripeException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "subscriptionId = ? WHERE subscriptionId = ?")) {
            preparedStatement.setString(1, newSubscriptionId);
            preparedStatement.setString(2, currentSubscriptionId);
            preparedStatement.execute();
        }
    }

    public void updateSubscriptionFields(Account updated) throws SQLException {
        checkIfEmailIsValid(updated.getName());
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "subscriptionId = ?, communityId = ?, accountRole = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getSubscriptionId());
            preparedStatement.setString(2, updated.getCommunityId());
            preparedStatement.setString(3, updated.getAccountRole().name());
            preparedStatement.setInt(4, updated.getId());
            preparedStatement.execute();
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        Account account = get(id);
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account WHERE id = ?")) {
            checkIfAccountIsCommunityOwner(account);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            accountActivityRepo().add(new AccountActivity(ActivityType.DELETE, account));
        }
    }

    private void checkIfAccountIsCommunityOwner(Account account) {
        try {
            if (account.getName().equals(Customer.retrieve(account.getCommunityId()).getEmail()))
                throw new CommunityOwnerUpdateException();
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }

    private String pass (Account updated) throws SQLException {
        Account account = get(updated.getId());
        return updated.getPassword().equals(account.getPassword()) ? account.getPassword() : passwordService.encryptPassword(updated.getPassword());
    }

    @Override
    public void add(Account account) throws SQLException {
        checkIfEmailIsValid(account.getName());
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account (id, username, password," +
                     "accountRole, communityId, subscriptionId, timeZone) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setString(3, passwordService.encryptPassword(account.getPassword()));
            preparedStatement.setString(4, account.getAccountRole().name());
            preparedStatement.setString(5,account.getCommunityId());
            preparedStatement.setString(6, account.getSubscriptionId());
            preparedStatement.setString(7, account.getTimeZone());
            preparedStatement.execute();
            if (!register)
                accountActivityRepo().add(new AccountActivity(ActivityType.ADD, account));
            else
                register = false;


        }
    }

    public void checkIfEmailIsValid(String string) throws InvalidEmailFormatException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (!pat.matcher(string).matches())
            throw new InvalidEmailFormatException();
    }

    public void setRegister(boolean register) {
        this.register = register;
    }
}
