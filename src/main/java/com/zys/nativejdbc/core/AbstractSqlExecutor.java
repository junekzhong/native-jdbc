package com.zys.nativejdbc.core;

import com.zys.nativejdbc.api.*;
import com.zys.nativejdbc.exception.InvalidResultException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象类，内部实现了具体的sql执行逻辑
 *
 * @author junekzhong
 * @date 2023/12/5 10:39
 */
public abstract class AbstractSqlExecutor implements SqlExecutor, ConnectionFactory {

    @Override
    public <T> T query(String sql, RowConverter<T> rowConverter) {
        return query(sql, null, rowConverter);
    }

    @Override
    public <T> T query(String sql, Class<T> tClass) {
        return query(sql, null, tClass);
    }

    @Override
    public <T> T query(String sql, List params, RowConverter<T> rowConverter) {
        List<T> result = queryForList(sql, params, rowConverter);
        checkSingleData(result);
        return result.get(0);
    }

    @Override
    public <T> T query(String sql, List params, Class<T> tClass) {
        List<T> result = queryForList(sql, params, tClass);
        checkSingleData(result);
        return result.get(0);
    }

    @Override
    public <T> List<T> queryForList(String sql, RowConverter<T> rowConverter) {
        return queryForList(sql, null ,rowConverter);
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> tClass) {
        return queryForList(sql, null, tClass);
    }

    @Override
    public <T> List<T> queryForList(String sql, List params, RowConverter<T> rowConverter) {
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if (null != params && params.size() > 0) {
                resultSet = getResultWithParams(statement, params);
            } else {
                resultSet = getResult(sql, statement);
            }
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(rowConverter.convert(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, List params, Class<T> tClass) {
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if (null != params && params.size() > 0) {
                resultSet = getResultWithParams(statement, params);
            } else {
                resultSet = getResult(sql, statement);
            }
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(convertResult2ClassEntity(tClass, resultSet));
            }
            return result;
        } catch (Exception e) {
            close(resultSet);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        return queryForMap(sql, null);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, List params) {
        List<Map<String, Object>> result = queryForMapList(sql, params);
        checkSingleData(result);
        return result.get(0);
    }

    @Override
    public List<Map<String, Object>> queryForMapList(String sql) {
        return queryForMapList(sql, null);
    }

    @Override
    public List<Map<String, Object>> queryForMapList(String sql, List params) {
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if (null != params && params.size() > 0) {
                resultSet = getResultWithParams(statement, params);
            } else {
                resultSet = getResult(sql, statement);
            }
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> result = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> data = convertResult2Map(columnCount, metaData, resultSet);
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    @Override
    public int execute(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public int execute(String sql, List params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public int execute(String sql, PreparedStatementSetter pss) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            pss.setValues(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public int batchExecute(String sql, List<Object[]> params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                Object[] subParams = params.get(i);
                for (int j = 0; j < subParams.length; j ++) {
                    preparedStatement.setObject(j + 1, subParams[j]);
                }
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            connection.commit();
            return ints.length;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public int batchExecute(String sql, BatchPreparedStatementSetter bpss) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < bpss.batchSize(); i++) {
                bpss.setValues(preparedStatement, i);
            }
            int[] ints = preparedStatement.executeBatch();
            connection.commit();
            return ints.length;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public long count(String sql) {
        return count(sql, new ArrayList());
    }

    @Override
    public long count(String sql, List params) {
        String countSql = "select count(*) from (" + sql + ") countWrapperTable";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(countSql);
            resultSet = getResultWithParams(preparedStatement, params);
            long result = 0;
            while (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
    }

    private ResultSet getResultWithParams(PreparedStatement preparedStatement, List params) throws SQLException {
        ResultSet resultSet;
        try {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject((i + 1), params.get(i));
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    private ResultSet getResult(String sql, Statement statement) throws SQLException {
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    private <T> T convertResult2ClassEntity(Class<T> tClass, ResultSet resultSet) throws InstantiationException, IllegalAccessException, SQLException {
        Field[] fields = tClass.getDeclaredFields();
        T t = tClass.newInstance();
        for (Field field : fields) {
            String fieldValue = resultSet.getString(field.getName());
            if ((null != fieldValue && !"".equals(fieldValue.trim()))) {
                field.setAccessible(true);
                field.set(t, fieldValue);
            }
        }
        return t;
    }

    private Map<String, Object> convertResult2Map(int columnCount, ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = resultSet.getObject(columnName);
            result.put(columnName, value);
        }
        return result;
    }

    private String checkSingleRowErrorTip = "预期返回1条数据，当前返回超过1条数据";

    private void checkSingleData(List data) {
        if (null != data && data.size() > 1) {
            throw new InvalidResultException(checkSingleRowErrorTip);
        }
    }
}
