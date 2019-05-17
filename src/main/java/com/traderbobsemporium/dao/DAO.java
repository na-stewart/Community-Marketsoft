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
// TODO: 5/12/2019 ABSTRACT CLASS INSTEAD OF INTERFACE 
public interface DAO<T> {
    T get(long id) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t, String[] params) throws SQLException, MalformedURLException;
    void add(T t) throws SQLException;
    void delete(long id) throws SQLException;

}
