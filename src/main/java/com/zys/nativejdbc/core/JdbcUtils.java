package com.zys.nativejdbc.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author junekzhong
 * @date 2023/12/5 10:42
 */
public class JdbcUtils {

    public static void close(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
