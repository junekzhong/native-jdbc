package com.zys.nativejdbc.api;

import java.sql.Connection;

/**
 * Connection工厂，定义了获取Connection的方法
 *
 * @author junekzhong
 * @date 2023/12/5 10:36
 */
public interface ConnectionFactory {

    Connection get();
}
