package cn.evlight.mybatis.session;

import cn.evlight.mybatis.bind.MapperRegistry;
import cn.evlight.mybatis.datasource.druid.DruidDatasourceFactory;
import cn.evlight.mybatis.mapping.Environment;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.type.TypeAliasRegistry;

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
    private Environment environment;
    private final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    {
        typeAliasRegistry.register("Druid", DruidDatasourceFactory.class);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

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
