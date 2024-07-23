package cn.evlight.mybatis.session;

import cn.evlight.mybatis.bind.MapperRegistry;
import cn.evlight.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry();
    private final Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        return mapperRegistry.getMapper(clazz, sqlSession);
    }

    public void addMapper(Class<?> clazz) {
        mapperRegistry.addMapper(clazz);
    }

    public boolean hashMapper(Class<?> clazz) {
        return mapperRegistry.hasMapper(clazz);
    }

    public void scan(String packagePath) {
        mapperRegistry.scan(packagePath);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatementMap.get(id);
    }

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatementMap.put(mappedStatement.getId(), mappedStatement);
    }
}
