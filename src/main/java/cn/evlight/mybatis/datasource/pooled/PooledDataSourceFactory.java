package cn.evlight.mybatis.datasource.pooled;

import cn.evlight.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/25
 */
public class PooledDataSourceFactory extends UnPooledDataSourceFactory {

    @Override
    public DataSource getDatasource() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
