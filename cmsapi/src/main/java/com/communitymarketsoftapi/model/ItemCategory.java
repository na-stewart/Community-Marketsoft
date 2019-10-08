package com.communitymarketsoftapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ItemCategory extends Model {

    public ItemCategory(){
        super(null);   }

    public ItemCategory(String name) {
        super(name);
    }

    public ItemCategory(ResultSet resultSet) throws SQLException {
        super(resultSet.getString("category"));
    }


}
