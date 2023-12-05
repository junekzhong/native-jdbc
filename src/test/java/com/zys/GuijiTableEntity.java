package com.zys;

/**
 * @author junekzhong
 * @date 2023/12/5 12:00
 */
public class GuijiTableEntity {
    private String order_code;

    private String table_name;

    private String total_data;

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTotal_data() {
        return total_data;
    }

    public void setTotal_data(String total_data) {
        this.total_data = total_data;
    }

    @Override
    public String toString() {
        return "GuijiTableEntity{" +
                "order_code='" + order_code + '\'' +
                ", table_name='" + table_name + '\'' +
                ", total_data='" + total_data + '\'' +
                '}';
    }
}
