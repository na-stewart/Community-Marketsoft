package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.ItemType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
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
public class ItemDAO implements DAO<Item> {

    @Override
    public Item get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("item WHERE id =" + id);
        Item item = null;
        if (resultSet.next())
            item = new Item(resultSet);
        resultSet.close();
        return item;
    }

    @Override
    public List<Item> getAll() throws SQLException {
        List<Item> items = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("item");
        while (resultSet.next())
            items.add(new Item(resultSet));
        resultSet.close();
        return items;
    }

    public List<Item> getAllWithType(ItemType itemType) throws SQLException {
        List<Item> items = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("item WHERE itemType = '" + itemType.name() + "'");
        while (resultSet.next())
            items.add(new Item(resultSet));
        resultSet.close();
        return items;
    }

    @Override
    public void updateAll(Item item, String[] params) {
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
    public void update(Item updated) {
        DatabaseUtil.UPDATE("UPDATE item SET name = '" + updated.getName() + "'," +
                "imageURL = '" + updated.getImageURL() + "'," + "price = '" + updated.getPrice() + "'," +
                "quantity = '" + updated.getQuantity() + "'," + "itemType = '" + updated.getItemType().name() +
                "' WHERE id =" + updated.getId() + ";");
    }

    @Override
    public void add(Item item) {
        DatabaseUtil.UPDATE("INSERT INTO item VALUES('" + item.getId() + "','" + item.getName() + "','" +
                item.getPrice() + "','" + item.getQuantity() + "','" + item.getImageURL() + "','"
                + item.getItemType().name() + "')");
    }

    @Override
    public void delete(long id) {
        DatabaseUtil.UPDATE("DELETE FROM item WHERE id = '" + id + "'");
    }
}
