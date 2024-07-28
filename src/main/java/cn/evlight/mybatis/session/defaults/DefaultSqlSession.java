package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;

import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        List<T> result = executor.query(mappedStatement, parameter, null, mappedStatement.getBoundSql());
        return result.get(0);
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
