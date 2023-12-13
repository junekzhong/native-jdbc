package com.zys.nativejdbc.api;

import java.util.List;
import java.util.Map;

/**
 * sql执行器接口，用于执行底层jdbc sql
 *
 * @author junekzhong
 * @date 2023/12/5 09:51
 */
public interface SqlExecutor {

    <T> T query(String sql, RowConverter<T> rowConverter);

    <T> T query(String sql, Class<T> tClass);

    <T> T query(String sql, List params, RowConverter<T> rowConverter);

    <T> T query(String sql, List params, Class<T> tClass);

    <T> List<T> queryForList(String sql, RowConverter<T> rowConverter);

    <T> List<T> queryForList(String sql, Class<T> tClass);

    <T> List<T> queryForList(String sql, List params, RowConverter<T> rowConverter);

    <T> List<T> queryForList(String sql, List params, Class<T> tClass);

    Map<String, Object> queryForMap(String sql);

    Map<String, Object> queryForMap(String sql, List params);

    List<Map<String, Object>> queryForMapList(String sql);

    List<Map<String, Object>> queryForMapList(String sql, List params);

    int execute(String sql);

    int execute(String sql, List params);

    int batchExecute(String sql, List<Object[]> params);

    int execute(String sql, PreparedStatementSetter pss);

    int batchExecute(String sql, BatchPreparedStatementSetter bpss);

    long count(String sql);

    long count(String sql, List params);
}
