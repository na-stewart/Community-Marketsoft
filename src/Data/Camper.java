package Data;

import javafx.scene.control.TextField;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Camper {
    private String id;
    private String name;
    private String balance;

    public Camper(String id, String name, String balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }
}
