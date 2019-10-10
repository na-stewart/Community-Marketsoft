package main.java.com.marketsoftcommunity.model;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemCategory extends Model {
    public ItemCategory(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}

