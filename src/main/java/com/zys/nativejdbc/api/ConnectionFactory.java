package com.zys.nativejdbc.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connection工厂，定义了Connection相关的方法
 *
 * @author junekzhong
 * @date 2023/12/5 10:36
 */
public interface ConnectionFactory {

    /**
     * 获取连接
     *
     * @return
     */
    Connection getConnection();

    /**
     * 关闭连接
     *
     * @param connection
     */
    default void close(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    default void close(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    default void close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
