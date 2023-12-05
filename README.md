# native-jdbc

## 主要目标

不借助任何的第三方持久层框架，实现jdbc的基本操作，主打一个轻量级，功能不用实现的太复杂、太多

## 示例

**初始化DirectConnectionSqlExecutor**
```java
ConnectionHolder connectionHolder = new ConnectionHolder("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "root", "123456");
sqlExecutor = new DirectConnectionSqlExecutor(connectionHolder);
```
**初始化DataSourceSqlExecutor**
```java
DruidDataSource dataSource = new DruidDataSource();
dataSource.setUsername("root");
dataSource.setPassword("123456");
dataSource.setUrl("jdbc:mysql://localhost:3306/test");
datasourceSqlExecutor = new DatasourceSqlExecutor(dataSource);
```

**查询单条数据**

> 1.RowConvert方式
```java
GuijiTableEntity result = sqlExecutor.query(sql, new RowConverter<GuijiTableEntity>() {
    @Override
    public GuijiTableEntity convert(ResultSet resultSet) {
        GuijiTableEntity guijiTableEntity = new GuijiTableEntity();
        try {
            guijiTableEntity.setOrder_code(resultSet.getString("order_code"));
            guijiTableEntity.setTable_name(resultSet.getString("table_name"));
            guijiTableEntity.setTotal_data(resultSet.getString("total_data"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guijiTableEntity;
    }
});
```
---

> 2.Class方式
```java
GuijiTableEntity query = sqlExecutor.query(sql, GuijiTableEntity.class);
```
---
> 3.Map方式
```java
Map<String, Object> data = sqlExecutor.queryForMap(sql);
```
---
> 4.带参数
```java
GuijiTableEntity data = sqlExecutor.query(wsql, Arrays.asList(523584), GuijiTableEntity.class);
```
---

**查询列表**

这里就不一一列举所有的方法了，与单个查询类似，列表查询也提供了多种方式进行构建，这里使用class方式来演示
```java
List<GuijiTableEntity> guijiTableEntities = datasourceSqlExecutor.queryForList(wsql, GuijiTableEntity.class);
```

**执行非查询操作**

> 1.执行insert语句
```java
String executeSql = "insert into test values(?)";
sqlExecutor.execute(executeSql, Arrays.asList(6));
```
> 2.批量执行insert语句
```java
List<Object[]> list = Arrays.asList(objects, objects2, objects3);
sqlExecutor.batchExecute(executeSql, list);
```
> 3.BatchPreparedStatementSetter方式实现批量执行
```java
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

## 结束语

以上就是本项目主要实现的功能了，主要是简单的数据库增删改查。

