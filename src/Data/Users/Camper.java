package Data.Users;

import Data.DatabaseQueryReceiver;
import java.sql.SQLException;

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
    private DatabaseQueryReceiver databaseQueryReceiver = new DatabaseQueryReceiver();


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
        if (!this.name.equals(name)) {
            databaseQueryReceiver.update("UPDATE camper SET name = '" + name + "' WHERE id =" + id + ";");
            this.name = name;
        }
    }

    public void setBalance(int balance) throws SQLException {
        if (this.balance != balance) {
            databaseQueryReceiver.update("UPDATE camper SET balance = '" + balance + "' WHERE id =" + id + ";");
            this.balance = balance;
        }
    }


}
