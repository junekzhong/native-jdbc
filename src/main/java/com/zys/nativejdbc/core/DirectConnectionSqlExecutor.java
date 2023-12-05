package com.zys.nativejdbc.core;

import java.sql.Connection;

/**
 * @author junekzhong
 * @date 2023/12/5 10:28
 */
public class DirectConnectionSqlExecutor extends AbstractSqlExecutor {

    private ConnectionHolder connectionHolder;

    public DirectConnectionSqlExecutor(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public Connection get() {
        try {
            return this.connectionHolder.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
