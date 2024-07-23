package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.session.SqlSessionFactory;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
