package com.communitymarketsoftapi.repository;


import com.communitymarketsoftapi.model.Model;
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

public abstract class Repo<T> {
    private String selectQuery;
    private String tableBeingSelected;
    private ResultSetModelFactory<T> resultSetModelFactory;


    public Repo(String tableBeingSelected) {
        this.tableBeingSelected = tableBeingSelected;
        selectQuery =  "SELECT * FROM " + tableBeingSelected;
        resultSetModelFactory = new ResultSetModelFactory<>(tableBeingSelected);
    }

    public T get(int id) throws SQLException {
        T model = null;
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
            PreparedStatement statement = connection.prepareStatement(selectQuery + " WHERE id = ? AND communityId = ?")) {
                statement.setInt(1, id);
                statement.setString(2, AccountUtil.COMMUNITY_ID());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    model = resultSetModelFactory.getModel(resultSet);
                return model;
            }
        }

    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() throws SQLException {
        List<T> modelList = new ArrayList<>();
        T model;
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery + " WHERE communityId = ?")) {
            statement.setString(1, AccountUtil.COMMUNITY_ID());
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    model = resultSetModelFactory.getModel(resultSet);
                    modelList.add(model);
                }
                return modelList;
            }
        }

    }

    public void delete(int id) throws SQLException {
        delete(id, true);
    }


    public void delete(int id, boolean log) throws SQLException {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+ tableBeingSelected + " WHERE id = ?")) {
            Model model = (Model) get(id);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            if (log)
                accountActivityRepo().add(new AccountActivity(ActivityType.DELETE, model));
        }
    }

    public abstract void update(T updated) throws SQLException;
    public abstract  void add(T t) throws SQLException;

    protected AccountActivityRepo accountActivityRepo(){
        return AccountActivityRepo.getInstance();
    }
}
