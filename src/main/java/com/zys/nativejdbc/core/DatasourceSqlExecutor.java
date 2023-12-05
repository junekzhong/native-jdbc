package com.zys.nativejdbc.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author junekzhong
 * @date 2023/12/5 14:43
 */
public class DatasourceSqlExecutor extends AbstractSqlExecutor {

    private DataSource dataSource;

    public DatasourceSqlExecutor(DataSource dataSource) {
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
