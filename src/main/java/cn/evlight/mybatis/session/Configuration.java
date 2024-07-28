package cn.evlight.mybatis.session;

import cn.evlight.mybatis.bind.MapperRegistry;
import cn.evlight.mybatis.datasource.druid.DruidDatasourceFactory;
import cn.evlight.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.evlight.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.executor.impl.SimpleExecutor;
import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.executor.resultset.impl.DefaultResultSetHandler;
import cn.evlight.mybatis.executor.statement.StatementHandler;
import cn.evlight.mybatis.executor.statement.impl.PreparedStatementHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.Environment;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.transaction.Transaction;
import cn.evlight.mybatis.transaction.jdbc.JdbcTransactionFactory;
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
        typeAliasRegistry.register("druid", DruidDatasourceFactory.class);
        typeAliasRegistry.register("unpooled", UnPooledDataSourceFactory.class);
        typeAliasRegistry.register("pooled", PooledDataSourceFactory.class);
        typeAliasRegistry.register("jdbc", JdbcTransactionFactory.class);
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

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(mappedStatement, parameter, resultSetHandler, executor, boundSql);
    }

    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    public ResultSetHandler newResultResultHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(boundSql);
    }
}
