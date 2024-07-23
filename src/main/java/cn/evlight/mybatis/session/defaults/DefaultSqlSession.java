package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("方法: " + statement + " 被代理了");
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("方法: " + statement + " 参数: " + parameter + " 被代理了");
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
