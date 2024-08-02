package cn.evlight.mybatis.datasource.unpooled;

import cn.evlight.mybatis.datasource.DatasourceFactory;
import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.SystemMetaObject;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/24
 */
public class UnPooledDataSourceFactory implements DatasourceFactory {

    protected DataSource dataSource;

    public UnPooledDataSourceFactory() {
        this.dataSource = new UnPooledDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        MetaObject metaObject = SystemMetaObject.forObject(dataSource);
        for (Object key : properties.keySet()) {
            String propertyName = (String) key;
            if (metaObject.hasSetter(propertyName)) {
                String value = (String) properties.get(propertyName);
                Object convertedValue = convertValue(metaObject, value, propertyName);
                metaObject.setValue(propertyName, convertedValue);
            }
        }
    }

    private Object convertValue(MetaObject metaObject, String value, String propertyName) {
        Class<?> type = metaObject.getSetterType(propertyName);
        if (type == Integer.class || type == int.class) {
            return Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return Long.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.valueOf(value);
        }
        return value;
    }

    @Override
    public DataSource getDatasource() {
        return this.dataSource;
    }
}
