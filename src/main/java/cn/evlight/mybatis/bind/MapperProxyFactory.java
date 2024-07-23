package cn.evlight.mybatis.bind;

import cn.evlight.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class MapperProxyFactory<T> {

    private final Class<T> clazz;

    public MapperProxyFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<>(clazz, sqlSession);
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{clazz},
                mapperProxy);
    }
}
