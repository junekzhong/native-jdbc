package com.zys.nativejdbc.api;

import java.sql.PreparedStatement;

/**
 * @author junekzhong
 * @date 2023/12/5 10:14
 */
public interface PreparedStatementSetter {

    void setValues(PreparedStatement ps);
}
