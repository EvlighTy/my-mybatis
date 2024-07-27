package cn.evlight.mybatis.datasource.unpooled;

import cn.evlight.mybatis.datasource.DatasourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/24
 */
public class UnPooledDataSourceFactory implements DatasourceFactory {

    protected Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDatasource() {
        UnPooledDataSource dataSource = new UnPooledDataSource();
        dataSource.setDriver(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
