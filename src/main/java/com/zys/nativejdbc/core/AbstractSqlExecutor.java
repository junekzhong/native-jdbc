package com.zys.nativejdbc.core;

import com.zys.nativejdbc.api.*;
import sun.reflect.misc.ReflectUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junekzhong
 * @date 2023/12/5 10:39
 */
public abstract class AbstractSqlExecutor implements SqlExecutor, ConnectionFactory {

    @Override
    public <T> T query(String sql, RowConverter<T> rowConverter) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
            int rowCount = 0;
            T result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = rowConverter.convert(resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> T query(String sql, Class<T> tClass) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
            int rowCount = 0;
            T result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = convertResult2ClassEntity(tClass, resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> T query(String sql, List params, RowConverter<T> rowConverter) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
            int rowCount = 0;
            T result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = rowConverter.convert(resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> T query(String sql, List params, Class<T> tClass) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
            int rowCount = 0;
            T result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = convertResult2ClassEntity(tClass, resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, RowConverter<T> rowConverter) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(rowConverter.convert(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> tClass) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(convertResult2ClassEntity(tClass, resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, List params, RowConverter<T> rowConverter) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(rowConverter.convert(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, List params, Class<T> tClass) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(convertResult2ClassEntity(tClass, resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int rowCount = 0;
            Map<String, Object> result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = convertResult2Map(columnCount, metaData, resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql, List params) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int rowCount = 0;
            Map<String, Object> result = null;
            while (resultSet.next()) {
                checkSingleRow(++rowCount);
                result = convertResult2Map(columnCount, metaData, resultSet);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public List<Map<String, Object>> queryForMapList(String sql) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResult(sql, connection);
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
            JdbcUtils.close(connection);
        }
    }

    @Override
    public List<Map<String, Object>> queryForMapList(String sql, List params) {
        Connection connection = null;
        try {
            connection = get();
            ResultSet resultSet = getResultWithParams(sql, connection, params);
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
            JdbcUtils.close(connection);
        }
    }

    @Override
    public int execute(String sql) {
        Connection connection = null;
        try {
            connection = get();
            return connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public int execute(String sql, List params) {
        Connection connection = null;
        try {
            connection = get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public int execute(String sql, PreparedStatementSetter pss) {
        Connection connection = null;
        try {
            connection = get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            pss.setValues(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public int batchExecute(String sql, List<Object[]> params) {
        Connection connection = null;
        try {
            connection = get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                Object[] subParams = params.get(i);
                for (int j = 0; j < subParams.length; j ++) {
                    preparedStatement.setObject(j + 1, subParams[j]);
                }
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            return ints.length;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public int batchExecute(String sql, BatchPreparedStatementSetter bpss) {
        Connection connection = null;
        try {
            connection = get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < bpss.batchSize(); i++) {
                bpss.setValues(preparedStatement, i);
            }
            int[] ints = preparedStatement.executeBatch();
            return ints.length;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(connection);
        }
    }

    private ResultSet getResultWithParams(String sql, Connection connection, List params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject((i + 1), params.get(i));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    private ResultSet getResult(String sql, Connection connection) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
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

    private void checkSingleRow(int rowCount) {
        if (rowCount > 1) {
            throw new RuntimeException("找到大于1条数据");
        }
    }
}
