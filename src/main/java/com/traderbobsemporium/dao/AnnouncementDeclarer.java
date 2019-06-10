package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Announcement;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

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
public class AnnouncementDeclarer implements DAO<Announcement> {


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
    public void updateAll(Announcement announcement, String[] params) {
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
    public void update(Announcement updated) {
        DatabaseUtil.UPDATE("UPDATE announcement SET username = '" + updated.getName() + "'," +
                "title = '" + updated.getTitle() + "'," +
                "dialog = '" + updated.getDialog() + "'," +
                "dateTime = '" + updated.getDateTime() + "'" +
                " WHERE id =" + updated.getId() + ";");
    }

    @Override
    public void add(Announcement announcement) {
        DatabaseUtil.UPDATE("INSERT INTO announcement VALUES('" +announcement.getId() + "','" +
                announcement.getName() + "','" +announcement.getTitle() + "','" + announcement.getDialog() +
                "','" + announcement.getDateTime() + "')");
    }

    @Override
    public void delete(long id) {
        DatabaseUtil.UPDATE("DELETE FROM announcement WHERE id = '" + id  + "'");
    }
}
