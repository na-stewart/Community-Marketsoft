package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements AbstractDAO<Item> {

    @Override
    public Item get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("item WHERE id =" + id);
        Item item = new Item(resultSet);
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

    @Override
    public void update(Item item, String[] params) throws SQLException, MalformedURLException {
        item.setName(params[0]);
        item.setImageURL(new URL(params[1]));
        item.setPrice(Integer.parseInt(params[2]));
        DatabaseUtil.UPDATE("UPDATE item SET name = '" + item.getName() + "'," +
                "url = '" + item.getImageURL() + "'," + "price = '" + item.getPrice()
                + "' WHERE id =" + item.getId() + ";");
    }

    @Override
    public void add(Item item) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO item VALUES('" + item.getId() + "','" + item.getName() + "','" +
                item.getImageURL()+ "','" + item.getPrice()+ "')");
    }

    @Override
    public void delete(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM item WHERE id = '" + id + "'");
    }
}
