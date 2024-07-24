package cn.evlight.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public interface DatasourceFactory {

    void setProperties(Properties properties);

    DataSource getDatasource();
}
