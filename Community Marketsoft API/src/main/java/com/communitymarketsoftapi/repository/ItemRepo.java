package com.communitymarketsoftapi.repository;

import com.communitymarketsoftapi.model.Item;
import com.communitymarketsoftapi.model.ItemCategory;
import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.model.logging.ActivityType;
import com.communitymarketsoftapi.repository.Loggers.AccountActivityRepo;
import com.communitymarketsoftapi.util.AccountUtil;
import com.communitymarketsoftapi.util.DbUtil;
import com.communitymarketsoftapi.util.Util;

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
    private AccountActivityRepo accountActivityRepo = new AccountActivityRepo();
    private Repo<ItemCategory> categoryRepo = new Repo<ItemCategory>("itemcategory") {

        @Override
        public void update(ItemCategory updated) {

        }

        @Override
        public void add(ItemCategory itemCategory) {
            try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO itemcategory" +
                         "(id, category, communityId) VALUES (?, ?, ?)")) {
                preparedStatement.setInt(1, itemCategory.getId());
                preparedStatement.setString(2, itemCategory.getName());
                preparedStatement.setString(3, itemCategory.getCommunityId());
                preparedStatement.execute();
                accountActivityRepo.add(new AccountActivity(ActivityType.ADD, itemCategory));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    public ItemRepo() {
        super("item");
    }


    @Override
    public void update(Item updated) throws SQLException {
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
            accountActivityRepo.add(new AccountActivity(ActivityType.UPDATE, updated));
        }
    }

    public List<Item> getAllByCategory(String itemCategoryName) throws SQLException {
        List<Item> list = new ArrayList<>();
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM item WHERE itemType = ? AND communityId = ?")) {
            statement.setString(1, itemCategoryName);
            statement.setString(2, AccountUtil.COMMUNITY_ID());
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    list.add(new Item(resultSet));
                }
                return list;
            }
        }
    }

    @Override
    public void add(Item item) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item (id, name," +
                     "price, quantity, imageURL, itemType, communityId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setBigDecimal(3, item.getPrice());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.setString(5, item.getImageURL());
            preparedStatement.setString(6, item.getItemType());
            preparedStatement.setString(7, item.getCommunityId());
            preparedStatement.execute();
            accountActivityRepo.add(new AccountActivity(ActivityType.ADD, item));
        }
    }

    public Repo<ItemCategory> getCategoryRepo() {
        return categoryRepo;
    }
}
