package com.zys.nativejdbc.core;

import java.sql.Connection;

/**
 * 直接连接sql执行器，每次获取连接都采用新创建的方式，不使用数据源
 *
 * @author junekzhong
 * @date 2023/12/5 10:28
 */
public class DirectConnectSqlExecutor extends AbstractSqlExecutor {

    private ConnectionBuilder connectionBuilder;

    public DirectConnectSqlExecutor(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    @Override
    public Connection getConnection() {
        try {
            return this.connectionBuilder.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
