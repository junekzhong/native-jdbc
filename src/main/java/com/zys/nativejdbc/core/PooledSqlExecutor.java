package com.zys.nativejdbc.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源方式的sql执行器，内部包含一个数据源，所有数据库连接来自于该数据源
 *
 * @author junekzhong
 * @date 2023/12/5 14:43
 */
public class PooledSqlExecutor extends AbstractSqlExecutor {

    private DataSource dataSource;

    public PooledSqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
