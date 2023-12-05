package com.zys.nativejdbc.api;

import java.sql.PreparedStatement;

/**
 * @author junekzhong
 * @date 2023/12/5 10:25
 */
public interface BatchPreparedStatementSetter {

    int batchSize();

    void setValues(PreparedStatement ps, int i);
}
