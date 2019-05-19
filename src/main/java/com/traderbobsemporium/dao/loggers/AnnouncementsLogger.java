package main.java.com.traderbobsemporium.dao.loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.Announcement;
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
public class AnnouncementsLogger implements Logger<Announcement> {

    @Override
    public void log(Announcement announcement) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO announcement VALUES('" +announcement.getId() + "','" +
                announcement.getName() + "','" +announcement.getTitle() + "','" + announcement.getDialog() +
                "','" + announcement.getDateTime() + "')");
    }

    @Override
    public void deleteLog(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM announcement WHERE id = '" + id  + "'");
    }

    @Override
    public ArrayList<Announcement> getLogs() throws SQLException {
        ArrayList<Announcement> announcements = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("announcement");
        while (resultSet.next())
            announcements.add(new Announcement(resultSet));
        resultSet.close();
        return announcements;
    }
}
