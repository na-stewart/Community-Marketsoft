package main.java.com.traderbobsemporium.model.Logging;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Announcement{
    private Long id;
    private String title;
    private String dialog;
    private String dateTime;

    public Announcement(Long id, String title, String dialog, String dateTime) {
        this.id = id;
        this.title = title;
        this.dialog = dialog;
        this.dateTime = dateTime;
    }

    public Announcement(ResultSet resultSet) throws SQLException {
        id = resultSet.getLong("id");
        title = resultSet.getString("title");
        dialog = resultSet.getString("dialog");
        dateTime = resultSet.getString("dateTime");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "title='" + title + '\'' +
                ", dialog='" + dialog + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}

