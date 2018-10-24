package Data;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Camper {
    private int id;
    private String name;
    private int coinCount;

    public Camper(int id, String name, int coinCount) {
        this.id = id;
        this.name = name;
        this.coinCount = coinCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCoinCount() {
        return coinCount;
    }
}
