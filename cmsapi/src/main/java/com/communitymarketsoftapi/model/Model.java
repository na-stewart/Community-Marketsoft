package com.communitymarketsoftapi.model;

import com.communitymarketsoftapi.util.DbUtil;
import com.communitymarketsoftapi.util.Util;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.SecurityUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Model {
    private int id;
    private String name;
    private transient String communityId;

    public Model(){ }

    public Model (String name){
        this.id = Util.NEW_ID();
        this.name = name;
        this.communityId = Util.LOGGED_IN_ACCOUNT_COMMUNITYID();
    }

    public Model (String name, String communityId){
        this.id = Util.NEW_ID();
        this.name = name;
        this.communityId = communityId;
    }


    public Model(int id, String name, String communityId) {
        this.id = id;
        this.name = name;
        this.communityId = communityId;
    }

    public Model(int id, String name) {
        this.id = id;
        this.name = name;
        this.communityId = getCommunityId();
    }


    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
