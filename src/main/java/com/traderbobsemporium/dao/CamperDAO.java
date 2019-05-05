package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CamperDAO implements AbstractDAO<Camper> {
    @Override
    public Camper get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULTSET("SELECT * FROM camper WHERE id =" + id);
        return new Camper(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("balance"));
    }

    @Override
    public List<Camper> getAll() throws SQLException {
        List<Camper> campers = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULTSET("SELECT * FROM camper");
        while (resultSet.next())
            campers.add(new Camper(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("balance")));
        return campers;
    }

    @Override
    public void update(Camper camper, String[] params) throws SQLException {
        camper.setName(params[0]);
        camper.setBalance(Integer.parseInt(params[1]));
        DatabaseUtil.UPDATE("UPDATE camper SET name = '" + camper.getName() + "'," +
                "balance = '" + camper.getBalance() + "' WHERE id =" + camper.getId() + ";");
    }

    @Override
    public void add(Camper camper) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO camper VALUES('" + camper.getId() + "','" + camper.getName() + "','" +
                camper.getBalance() + "')");
    }

    @Override
    public void delete(Camper camper) {

    }
}
