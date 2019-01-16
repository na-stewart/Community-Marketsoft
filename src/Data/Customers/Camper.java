package Data.Customers;

import Data.DataManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.Optional;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Camper {
    private int id;
    private String name;
    private int balance;
    private DataManager databaseManager = new DataManager();


    public Camper(int id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setName(String name) throws SQLException {
        databaseManager.update( "UPDATE camper SET name = '" + name + "' WHERE id =" + id + ";");
    }

    public void setBalance(int balance) throws SQLException {
        databaseManager.update( "UPDATE camper SET balance = '" + balance + "' WHERE id =" + id + ";");
    }

}
