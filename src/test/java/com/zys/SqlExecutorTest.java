package com.zys;

import com.alibaba.druid.pool.DruidDataSource;
import com.zys.nativejdbc.api.BatchPreparedStatementSetter;
import com.zys.nativejdbc.api.RowConverter;
import com.zys.nativejdbc.api.SqlExecutor;
import com.zys.nativejdbc.core.ConnectionHolder;
import com.zys.nativejdbc.core.DatasourceSqlExecutor;
import com.zys.nativejdbc.core.DirectConnectionSqlExecutor;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junekzhong
 * @date 2023/12/5 11:33
 */
public class SqlExecutorTest {

    private SqlExecutor sqlExecutor;

    private SqlExecutor datasourceSqlExecutor;

    private String sql = "select order_code, table_name, total_data from guiji_table_entity ";

    @Before
    public void init() {
        ConnectionHolder connectionHolder = new ConnectionHolder("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "root", "123456");
        sqlExecutor = new DirectConnectionSqlExecutor(connectionHolder);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        datasourceSqlExecutor = new DatasourceSqlExecutor(dataSource);
    }

    @Test
    public void testQueryForRowConverter() {
        Map result = sqlExecutor.query(sql, new RowConverter<Map>() {
            @Override
            public Map convert(ResultSet resultSet) {
                Map data = new HashMap();
                try {
                    data.put("orderCode", resultSet.getString("order_code"));
                    data.put("tableName", resultSet.getString("table_name"));
                    data.put("totalData", resultSet.getLong("total_data"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return data;
            }
        });
        System.out.println(result);
    }

    @Test
    public void testQueryForClass() {
        GuijiTableEntity query = sqlExecutor.query(sql, GuijiTableEntity.class);
        System.out.println(query);
    }

    @Test
    public void testQueryForMap() {
        Map<String, Object> data = sqlExecutor.queryForMap(sql);
        System.out.println(data);
    }

    @Test
    public void testQueryForParam() {
        String wsql = sql + " where total_data = ?";
        GuijiTableEntity data = sqlExecutor.query(wsql, Arrays.asList(523584), GuijiTableEntity.class);
        System.out.println(data);
        data = sqlExecutor.query(wsql, Arrays.asList(523584), new RowConverter<GuijiTableEntity>() {
            @Override
            public GuijiTableEntity convert(ResultSet resultSet) {
                GuijiTableEntity guijiTableEntity = new GuijiTableEntity();
                try {
                    guijiTableEntity.setTable_name(resultSet.getString("table_name"));
                    guijiTableEntity.setTotal_data(resultSet.getString("total_data"));
                    guijiTableEntity.setOrder_code(resultSet.getString("order_code"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return guijiTableEntity;
            }
        });
        System.out.println(data);
    }

    @Test
    public void testQueryList() {
        String wsql = sql + " limit 1, 10";
        List<GuijiTableEntity> guijiTableEntities = datasourceSqlExecutor.queryForList(wsql, GuijiTableEntity.class);
        System.out.println(guijiTableEntities);
        guijiTableEntities = sqlExecutor.queryForList(wsql, new RowConverter<GuijiTableEntity>() {
            @Override
            public GuijiTableEntity convert(ResultSet resultSet) {
                GuijiTableEntity guijiTableEntity = new GuijiTableEntity();
                try {
                    guijiTableEntity.setTable_name(resultSet.getString("table_name"));
                    guijiTableEntity.setTotal_data(resultSet.getString("total_data"));
                    guijiTableEntity.setOrder_code(resultSet.getString("order_code"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return guijiTableEntity;
            }
        });
        System.out.println("aaaaaa-------------------------------------");
        System.out.println(guijiTableEntities);
        wsql = sql + " where total_data > ?";
        guijiTableEntities = sqlExecutor.queryForList(wsql, Arrays.asList(240750900), GuijiTableEntity.class);
        System.out.println(guijiTableEntities);
        guijiTableEntities = sqlExecutor.queryForList(wsql, Arrays.asList(240750900), new RowConverter<GuijiTableEntity>() {
            @Override
            public GuijiTableEntity convert(ResultSet resultSet) {
                GuijiTableEntity guijiTableEntity = new GuijiTableEntity();
                try {
                    guijiTableEntity.setTable_name(resultSet.getString("table_name"));
                    guijiTableEntity.setTotal_data(resultSet.getString("total_data"));
                    guijiTableEntity.setOrder_code(resultSet.getString("order_code"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return guijiTableEntity;
            }
        });
        System.out.println(guijiTableEntities);
    }

    @Test
    public void testQueryMap() {
        String wsql = sql + " where total_data > ?";
        List<Map<String, Object>> stringObjectMap = sqlExecutor.queryForMapList(sql);
        System.out.println(stringObjectMap);
        List<Map<String, Object>> maps = sqlExecutor.queryForMapList(wsql, Arrays.asList(240750900));
        System.out.println(maps);
        Map<String, Object> data = sqlExecutor.queryForMap(wsql, Arrays.asList(240750900));
        System.out.println(data);
        maps = sqlExecutor.queryForMapList(wsql, Arrays.asList(240750900));
        System.out.println(maps);
    }

    @Test
    public void execute() {
        String executeSql = "insert into test values(?)";
        System.out.println(sqlExecutor.execute(executeSql, Arrays.asList(6)));
        Object[] objects = {7};
        Object[] objects2 = {8};
        Object[] objects3 = {9};
        final List<Object[]> list = Arrays.asList(objects, objects2, objects3);
        System.out.println(sqlExecutor.batchExecute(executeSql, list));
        System.out.println(sqlExecutor.batchExecute(executeSql, new BatchPreparedStatementSetter() {
            @Override
            public int batchSize() {
                return list.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) {
                Object[] objects = list.get(i);
                try {
                    for (Object object : objects) {
                        ps.setObject(1, object);
                    }
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }

}


