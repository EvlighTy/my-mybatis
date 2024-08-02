package cn.evlight.mybatis.refection.object;

import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public interface ObjectFactory {

    void setProperties(Properties properties);

    <T> T create(Class<T> clazz);

    <T> T create(Class<T> clazz, List<Class<?>> ConstructorArgTypes, List<Object> ConstructorArgs);
}
