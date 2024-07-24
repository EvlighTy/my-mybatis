package cn.evlight.mybatis.mapping;

import cn.evlight.mybatis.datasource.DatasourceFactory;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public class Environment {
    private final String id;
    private final DataSource dataSource;

    public Environment(String id, DatasourceFactory datasourceFactory, DataSource dataSource) {
        this.id = id;

        this.dataSource = dataSource;
    }

    public String getId() {
        return id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
