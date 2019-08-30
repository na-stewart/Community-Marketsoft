package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;

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


    //Rething item categorys

    //text field right below item choice box. Allows you to delete items exc.


    public void addItemCategory(String category){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO itemcategories" +
                     "(id, category) VALUES (?, ?)")) {
            preparedStatement.setInt(1, Util.NEW_ID());
            preparedStatement.setString(2, category);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getItemCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT category FROM itemcategories")) {
                while (resultSet.next()) {
                    categories.add(resultSet.getString("category"));
                }
                return categories;
            }
        }
    }


    public void deleteItemCategory(String category){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM itemcategories WHERE category = ?")) {
            preparedStatement.setString(1, category);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
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
            preparedStatement.setString(5, updated.getItemType());
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
            preparedStatement.setString(6, item.getItemType());
            preparedStatement.execute();
        }
    }

}
