package cn.evlight.mybatis.bind;

import cn.evlight.mybatis.session.SqlSession;
import cn.hutool.core.lang.ClassScanner;

import java.util.HashMap;
import java.util.Set;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class MapperRegistry {

    private final HashMap<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(clazz);
        if (null == mapperProxyFactory) {
            throw new RuntimeException("unknown mapper");
        }
        return mapperProxyFactory.newInstance(sqlSession);
    }

    public boolean hasMapper(Class<?> clazz) {
        return knownMappers.containsKey(clazz);
    }

    public void addMapper(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new RuntimeException(clazz + " is not an interface!");
        }
        if (hasMapper(clazz)) {
            throw new RuntimeException(clazz + " is already known to mapper registry");
        }
        knownMappers.put(clazz, new MapperProxyFactory<>(clazz));
    }

    public void scan(String packagePath) {
        Set<Class<?>> classes = ClassScanner.scanPackage(packagePath);
        classes.forEach(this::addMapper);
    }
}
