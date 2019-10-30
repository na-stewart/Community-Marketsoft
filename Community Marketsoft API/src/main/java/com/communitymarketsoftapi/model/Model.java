package com.communitymarketsoftapi.model;

import com.communitymarketsoftapi.util.AccountUtil;
import com.communitymarketsoftapi.util.Util;

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
        this.communityId = AccountUtil.COMMUNITY_ID();
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
