package com.zys.nativejdbc.api;

import java.sql.ResultSet;

/**
 * 行转换器，将查询结果转换成自定义结果
 *
 * @author junekzhong
 * @date 2023/12/5 09:55
 */
public interface RowConverter<T> {

    T convert(ResultSet resultSet);
}
