package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.ItemType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemDAO extends DAO<Item> {
    private final String receiveQuery = "SELECT * FROM item ";

    public ItemDAO() {
        super("item");
    }

    @Override
    public void updateAll(Item item, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            item.setName(params[0]);
        if (!params[1].isEmpty())
            item.setQuantity(Integer.parseInt(params[1]));
        if (!params[2].isEmpty())
            item.setPrice(new BigDecimal(params[2]));
        if (!params[3].isEmpty())
            item.setImageURL(params[3]);
        if (!params[4].isEmpty())
            item.setItemType(ItemType.valueOf(params[4]));
        update(item);
    }

    @Override
    public void update(Item updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET " +
                     "name = ?, imageURL = ?, price = ?, quantity = ?, itemType = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getImageURL());
            preparedStatement.setBigDecimal(3, updated.getPrice());
            preparedStatement.setInt(4, updated.getQuantity());
            preparedStatement.setString(5, updated.getItemType().name());
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();
        }
    }

    @Override
    public void add(Item item) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item (id, name," +
                     "price, quantity, imageURL, itemType) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setBigDecimal(3, item.getPrice());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.setString(5, item.getImageURL());
            preparedStatement.setString(6, item.getItemType().name());
            preparedStatement.execute();
        }
    }
}
