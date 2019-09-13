package main.java.com.marketsoftcommunityapi.repository;

import main.java.com.marketsoftcommunityapi.model.Item;
import main.java.com.marketsoftcommunityapi.model.ItemCategory;
import main.java.com.marketsoftcommunityapi.model.Model;
import main.java.com.marketsoftcommunityapi.model.logging.AccountActivity;
import main.java.com.marketsoftcommunityapi.model.logging.ActivityType;
import main.java.com.marketsoftcommunityapi.util.DbUtil;
import main.java.com.marketsoftcommunityapi.util.LoggingUtil;
import main.java.com.marketsoftcommunityapi.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemRepo extends Repo<Item> {
    private Repo<ItemCategory> categoryRepo = new Repo<ItemCategory>("itemcategories") {

        @Override
        public void update(ItemCategory updated) {

        }

        @Override
        public void add(ItemCategory itemCategory) {
            try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO itemcategories" +
                         "(id, category) VALUES (?, ?)")) {
                preparedStatement.setInt(1, itemCategory.getId());
                preparedStatement.setString(2, itemCategory.getName());
                preparedStatement.execute();
                getAccountActivityRepo().add(new AccountActivity(ActivityType.ADD , itemCategory));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    public ItemRepo() {
        super("item");
    }


    @Override
    public void update(Item updated) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET " +
                     "name = ?, imageURL = ?, price = ?, quantity = ?, itemType = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getImageURL());
            preparedStatement.setBigDecimal(3, updated.getPrice());
            preparedStatement.setInt(4, updated.getQuantity());
            preparedStatement.setString(5, updated.getItemType());
            preparedStatement.setInt(6, updated.getId());
            preparedStatement.execute();
            getAccountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, updated));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Item item)  {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item (id, name," +
                     "price, quantity, imageURL, itemType) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setBigDecimal(3, item.getPrice());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.setString(5, item.getImageURL());
            preparedStatement.setString(6, item.getItemType());
            preparedStatement.execute();
            getAccountActivityRepo().add(new AccountActivity(ActivityType.ADD, item));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Repo<ItemCategory> getCategoryRepo() {
        return categoryRepo;
    }
}
