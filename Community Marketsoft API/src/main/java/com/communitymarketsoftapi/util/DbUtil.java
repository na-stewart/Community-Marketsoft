package com.communitymarketsoftapi.util;

import com.communitymarketsoftapi.repository.Repo;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
public class DbUtil {
    @Autowired
    public static HikariDataSource DATA_SOURCE;
    private DbUtil(){

    }




    // Use this code snippet in your app.
// If you need more information about configurations or implementing the sample code, visit the AWS docs:
// https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-samples.html#prerequisites


}
