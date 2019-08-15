package main.java.com.traderbobsemporium.dao;



import main.java.com.traderbobsemporium.util.DatabaseUtil;

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

    public DAO(String tableBeingSelected) {
        this.tableBeingSelected = tableBeingSelected;
        selectQuery =   "SELECT * FROM " + tableBeingSelected;
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

    public void delete(int id){
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+ tableBeingSelected + " WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract void updateAll(T t, String[] params) throws SQLException;
    public abstract void update(T updated) throws SQLException;
    public abstract  void add(T t) throws SQLException;

    public String getTableBeingSelected() {
        return tableBeingSelected;
    }
}
