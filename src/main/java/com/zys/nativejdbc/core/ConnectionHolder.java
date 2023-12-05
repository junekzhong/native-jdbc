package com.zys.nativejdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Connection包装类，用于快速创建Connection
 *
 * @author junekzhong
 * @date 2023/12/5 10:26
 */
public class ConnectionHolder {

    private String driver;

    private String jdbcUrl;

    private String username;

    private String password;

    private ConnectionHolder() {

    }

    public Connection open() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static ConnectionHolderBuilder builder() {
        return new ConnectionHolderBuilder();
    }

    public static class ConnectionHolderBuilder {

        private String driver;

        private String jdbcUrl;

        private String username;

        private String password;

        public ConnectionHolderBuilder() {
        }

        public ConnectionHolderBuilder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public ConnectionHolderBuilder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public ConnectionHolderBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ConnectionHolderBuilder password(String password) {
            this.password = password;
            return this;
        }

        public ConnectionHolder build() {
            ConnectionHolder connectionHolder = new ConnectionHolder();
            connectionHolder.driver = this.driver;
            connectionHolder.jdbcUrl = this.jdbcUrl;
            connectionHolder.username = this.username;
            connectionHolder.password = this.password;
            return connectionHolder;
        }
    }

    @Override
    public String toString() {
        return "ConnectionHolder{" +
                "driver='" + driver + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
