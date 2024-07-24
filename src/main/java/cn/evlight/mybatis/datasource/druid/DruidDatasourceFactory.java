package cn.evlight.mybatis.datasource.druid;

import cn.evlight.mybatis.datasource.DatasourceFactory;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public class DruidDatasourceFactory implements DatasourceFactory {

    private DruidDataSource dataSource;

    @Override
    public void setProperties(Properties properties) {
        if (null == dataSource || dataSource.isClosed()) {
            synchronized (DruidDatasourceFactory.class) {
                if (null == dataSource || dataSource.isClosed()) {
                    dataSource = new DruidDataSource();
                }
            }
        }
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
    }

    @Override
    public DataSource getDatasource() {
        return dataSource;
    }
}
