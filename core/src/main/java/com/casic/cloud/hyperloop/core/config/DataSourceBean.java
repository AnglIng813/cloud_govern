package com.casic.cloud.hyperloop.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * @Author: LDC
 * @Date: 2018/10/25 16:48
 * @version: V1.0
 */
@Configuration
@Slf4j
public class DataSourceBean {
    @Value("${datasource.name}")
    private String name;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Bean     //声明其为Bean实例
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setName(name);
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        String connectionInitSqls = "SET NAMES utf8mb4";
        StringTokenizer tokenizer = new StringTokenizer(connectionInitSqls, ";");
        datasource.setConnectionInitSqls(Collections.list(tokenizer));//重点设置该参数
        datasource.setMaxActive(20);
        datasource.setInitialSize(1);
        datasource.setMaxWait(6000);
        datasource.setMinIdle(1);
        datasource.setTimeBetweenEvictionRunsMillis(6000);
        datasource.setMinEvictableIdleTimeMillis(300000);
        datasource.setValidationQuery("select 'x'");
        datasource.setTestWhileIdle(true);
        datasource.setTestOnBorrow(false);
        datasource.setTestOnReturn(false);
        datasource.setPoolPreparedStatements(true);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(20);
        try {
            datasource.setFilters("stat");
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }

        return datasource;
    }
}
