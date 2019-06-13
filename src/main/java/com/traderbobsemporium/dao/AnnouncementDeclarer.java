package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.AccountPermission;
import main.java.com.traderbobsemporium.model.Announcement;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AnnouncementDeclarer implements DAO<Announcement> {
    private final String receiveQuery = "SELECT * FROM announcement ";

    @Override
    public Announcement get(long id) throws SQLException {
        Announcement announcement = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = " + id)) {
                if (resultSet.next())
                    announcement = new Announcement(resultSet);
                return announcement;
            }
        }
    }

    @Override
    public List<Announcement> getAll() throws SQLException {
        return getAll(null);
    }


    @Override
    public List<Announcement> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<Announcement> announcements = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    announcements.add(new Announcement(resultSet));
                return announcements;
            }
        }
    }

    @Override
    public void updateAll(Announcement announcement, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            announcement.setName(params[0]);
        if (!params[1].isEmpty())
            announcement.setTitle(params[1]);
        if (!params[2].isEmpty())
            announcement.setDialog(params[2]);
        if (!params[3].isEmpty())
            announcement.setDateTime(params[3]);
        update(announcement);
    }

    @Override
    public void update(Announcement updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE announcement SET " +
                     "username = '" + updated.getName() + "'," +
                     "title = '" + updated.getTitle() + "'," +
                     "dialog = '" + updated.getDialog() + "'," +
                     "dateTime = '" + updated.getDateTime() + "'" +
                     " WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void add(Announcement announcement) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO announcement VALUES('" +announcement.getId() + "','" +
                             announcement.getName() + "','" +announcement.getTitle() + "','" + announcement.getDialog() +
                             "','" + announcement.getDateTime() + "')")) {
            preparedStatement.execute();
        }

    }

    @Override
    public void delete(Announcement announcement) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM announcement WHERE id = '" + announcement.getId() + "'")) {
            preparedStatement.execute();
        }
    }
}
