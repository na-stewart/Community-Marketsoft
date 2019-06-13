package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.ItemType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

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
public class ItemDAO implements DAO<Item> {
    private final String receiveQuery = "SELECT * FROM item ";

    @Override
    public Item get(long id) throws SQLException {
        Item item = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = " + id)) {
                if (resultSet.next())
                    item = new Item(resultSet);
                return item;
            }
        }
    }

    @Override
    public List<Item> getAll() throws SQLException {
        return getAll(null);
    }


    @Override
    public List<Item> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<Item> items = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    items.add(new Item(resultSet));
                return items;
            }
        }
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
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET name = '" + updated.getName() + "'," +
                     "imageURL = '" + updated.getImageURL() + "'," + "price = '" + updated.getPrice() + "'," +
                     "quantity = '" + updated.getQuantity() + "'," + "itemType = '" + updated.getItemType().name() +
                     "' WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void add(Item item) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item VALUES('" +
                     item.getId() + "','" + item.getName() + "','" + item.getPrice() + "','" + item.getQuantity() +
                     "','" + item.getImageURL() + "','" + item.getItemType().name() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Item item) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM item WHERE id = '" + item.getId() + "'")) {
            preparedStatement.execute();
        }
    }

}
