package com.zys.nativejdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author junekzhong
 * @date 2023/12/5 10:26
 */
public class ConnectionHolder {

    private String driver;

    private String jdbcUrl;

    private String username;

    private String password;

    public ConnectionHolder(String driver, String jdbcUrl, String username, String password) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public Connection open() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

}
