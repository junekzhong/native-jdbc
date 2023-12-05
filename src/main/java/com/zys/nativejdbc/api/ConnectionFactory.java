package com.zys.nativejdbc.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection工厂，定义了Connection相关的方法
 *
 * @author junekzhong
 * @date 2023/12/5 10:36
 */
public interface ConnectionFactory {

    /**
     * 获取连接
     * @return
     */
    Connection get();

    /**
     * 关闭连接
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
}
