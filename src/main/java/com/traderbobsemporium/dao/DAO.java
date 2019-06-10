package main.java.com.traderbobsemporium.dao;



import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

public interface DAO<T> {
    T get(long id) throws SQLException;
    List<T> getAll() throws SQLException;
    void updateAll(T t, String[] params);
    void update(T updated);
    void add(T t);
    void delete(long id);

}
