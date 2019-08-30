package main.java.com.traderbobsemporium.dao;



import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

public abstract class DAO<T> {
    private String selectQuery;
    private String tableBeingSelected;
    private ResultSetModelFactory<T> resultSetModelFactory;

    /*
    Events: Add update delete
    create a list of objects and event
    on apply, commit those changes to the database via for loop
     */

    public DAO(String tableBeingSelected) {
        this.tableBeingSelected = tableBeingSelected;
        selectQuery =  "SELECT * FROM " + tableBeingSelected;
        resultSetModelFactory = new ResultSetModelFactory<>(tableBeingSelected);
    }


    public T get(int id){
        T model = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery + " WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    model = resultSetModelFactory.getModel(resultSet);
                return model;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() throws SQLException {
            List<T> modelList = new ArrayList<>();
            T model;
            try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
                 Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(selectQuery)) {
                    while (resultSet.next()) {
                        model = resultSetModelFactory.getModel(resultSet);
                        modelList.add(model);
                    }
                    return modelList;
                }
            }
    }

    public List<T> getAll(String whereClause, String param){
        return getAll(whereClause, new String[]{param});
    }


    public List<T> getAll(String whereClause, String[] params){
        List<T> list = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery + " " + whereClause)) {
            for (int i = 0; i < params.length; i++)
                statement.setString(i + 1, params[i]);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    list.add(resultSetModelFactory.getModel(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return list;
    }

    public void delete(int id){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+ tableBeingSelected + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    public abstract void update(T updated) throws SQLException;
    public abstract  void add(T t) throws SQLException;
}
