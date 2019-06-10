package main.java.com.traderbobsemporium.dao;


import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.math.BigDecimal;
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
public class CamperDAO implements DAO<Camper> {


    @Override
    public Camper get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("camper WHERE id =" + id);
        Camper camper = null;
        if (resultSet.next())
            camper = new Camper(resultSet);
        resultSet.close();
        return camper;
    }

    @Override
    public List<Camper> getAll() throws SQLException {
        List<Camper> campers = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("camper");
        while (resultSet.next())
            campers.add(new Camper(resultSet));
        resultSet.close();
        return campers;
    }

    @Override
    public void updateAll(Camper camper, String[] params) {
        if (!params[0].isEmpty())
            camper.setName(params[0]);
        if (!params[1].isEmpty())
            camper.setBalance(new BigDecimal(params[1]));
        update(camper);
    }

    @Override
    public void update(Camper updated) {
        DatabaseUtil.UPDATE("UPDATE camper SET name = '" + updated.getName() + "'," +
                "balance = '" + updated.getBalance() + "' WHERE id =" + updated.getId() + ";");
    }


    @Override
    public void add(Camper camper) {
        DatabaseUtil.UPDATE("INSERT INTO camper VALUES('" + camper.getId() + "','" + camper.getName() + "','" +
                camper.getBalance() + "')");
    }

    @Override
    public void delete(long id) {
        DatabaseUtil.UPDATE("DELETE FROM camper WHERE id = '" + id + "'");
    }
}
