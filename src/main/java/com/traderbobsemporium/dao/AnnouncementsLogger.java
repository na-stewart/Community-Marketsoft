package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.Announcement;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
class AnnouncementsLogger implements DAO<Announcement> {


    @Override
    public Announcement get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("announcement WHERE id =" + id);
        Announcement announcement = null;
        if (resultSet.next())
            announcement = new Announcement(resultSet);
        resultSet.close();
        return announcement;
    }

    @Override
    public List<Announcement> getAll() throws SQLException {
        List<Announcement> announcements = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("announcement");
        while (resultSet.next())
            announcements.add(new Announcement(resultSet));
        Collections.reverse(announcements);
        resultSet.close();
        return announcements;
    }

    @Override
    public void update(Announcement announcement, String[] params) throws SQLException {
        announcement.setName(params[0]);
        announcement.setTitle(params[1]);
        announcement.setDialog(params[2]);
        announcement.setDateTime(params[3]);
        DatabaseUtil.UPDATE("UPDATE announcement SET username = '" + announcement.getName() + "'," +
                "title = '" + announcement.getTitle() + "'," +
                "dialog = '" + announcement.getDialog() + "'," +
                "dateTime = '" + announcement.getDateTime() + "'" +
                " WHERE id =" + announcement.getId() + ";");
    }

    @Override
    public void add(Announcement announcement) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO announcement VALUES('" +announcement.getId() + "','" +
                announcement.getName() + "','" +announcement.getTitle() + "','" + announcement.getDialog() +
                "','" + announcement.getDateTime() + "')");
    }

    @Override
    public void delete(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM announcement WHERE id = '" + id  + "'");
    }
}
