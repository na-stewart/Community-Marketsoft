package main.java.com.traderbobsemporium.model;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class DataObject {
    private long id;
    private String name;

   public DataObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
