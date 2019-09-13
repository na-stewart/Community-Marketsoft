package main.java.com.marketsoftcommunityapi.repository.Loggers;

import main.java.com.marketsoftcommunityapi.model.logging.AccountActivity;
import main.java.com.marketsoftcommunityapi.model.logging.ActivityType;
import main.java.com.marketsoftcommunityapi.repository.Repo;
import main.java.com.marketsoftcommunityapi.model.logging.Announcement;
import main.java.com.marketsoftcommunityapi.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AnnouncementRepo extends Repo<Announcement> {


    public AnnouncementRepo() {
        super("announcement");
    }

    @Override
    public void update(Announcement updated)  {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE announcement SET " +
                     "username = ?, title = ?, dialog = ?, date = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getTitle());
            preparedStatement.setString(3, updated.getDialog());
            preparedStatement.setString(4, updated.getDateTime());
            preparedStatement.setInt(5, updated.getId());
            preparedStatement.execute();
            getAccountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, updated));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Announcement announcement) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO announcement(id," +
                             "username, title, dialog, date) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, announcement.getId());
            preparedStatement.setString(2, announcement.getName());
            preparedStatement.setString(3, announcement.getTitle());
            preparedStatement.setString(4, announcement.getDialog());
            preparedStatement.setString(5, announcement.getDateTime());
            preparedStatement.execute();
            getAccountActivityRepo().add(new AccountActivity(ActivityType.ADD, announcement));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
