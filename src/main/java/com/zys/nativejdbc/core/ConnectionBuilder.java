package com.zys.nativejdbc.core;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Connection包装类，用于快速创建Connection
 *
 * @author junekzhong
 * @date 2023/12/5 10:26
 */
public class ConnectionBuilder {

    private String driver;

    private String jdbcUrl;

    private String username;

    private String password;

    private ConnectionBuilder() {

    }

    public Connection open() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String driver;

        private String jdbcUrl;

        private String username;

        private String password;

        private Builder() {

        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public ConnectionBuilder build() {
            ConnectionBuilder connectionBuilder = new ConnectionBuilder();
            connectionBuilder.driver = this.driver;
            connectionBuilder.jdbcUrl = this.jdbcUrl;
            connectionBuilder.username = this.username;
            connectionBuilder.password = this.password;
            return connectionBuilder;
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
