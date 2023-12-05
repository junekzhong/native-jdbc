package com.zys.nativejdbc.api;

import java.sql.Connection;

/**
 * @author junekzhong
 * @date 2023/12/5 10:36
 */
public interface ConnectionFactory {

    Connection get();
}
