package com.zys;

import com.zys.nativejdbc.core.ConnectionBuilder;
import org.junit.Test;

/**
 * @author junekzhong
 * @date 2023/12/5 20:53
 */
public class TestBuilder {

    @Test
    public void test1() {
        ConnectionBuilder connectionBuilder = ConnectionBuilder.builder()
                .driver("com.mysql.cj.jdbc.Driver")
                .jdbcUrl("jdbc:mysql://localhost:3306/test")
                .username("root")
                .password("123456").build();
        System.out.println(connectionBuilder);
    }
}
