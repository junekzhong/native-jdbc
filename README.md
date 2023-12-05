# native-jdbc

## 主要目标

不借助任何的第三方持久层框架，实现jdbc的基本操作，主打一个轻量级，功能不用实现的太复杂、太多

## 示例

查询
```java
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

GuijiTableEntity query = sqlExecutor.query(sql, GuijiTableEntity.class);

List<GuijiTableEntity> guijiTableEntities = datasourceSqlExecutor.queryForList(wsql, GuijiTableEntity.class);

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
```

执行事务操作

非批量操作
```java
String executeSql = "insert into test values(?)";
sqlExecutor.execute(executeSql, Arrays.asList(6));
```

批量操作

```java
String executeSql = "insert into test values(?)";
        
Object[] objects = {7};
Object[] objects2 = {8};
Object[] objects3 = {9};
final List<Object[]> list = Arrays.asList(objects, objects2, objects3);
        
sqlExecutor.batchExecute(executeSql, list);

sqlExecutor.batchExecute(executeSql, new BatchPreparedStatementSetter() {
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
});
```
