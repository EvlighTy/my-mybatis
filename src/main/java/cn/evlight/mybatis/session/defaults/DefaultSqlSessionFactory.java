package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.mapping.Environment;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.session.SqlSessionFactory;
import cn.evlight.mybatis.transaction.Transaction;
import cn.evlight.mybatis.type.enums.TransactionIsolationLevel;

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
        Environment environment = configuration.getEnvironment();
        Transaction transaction = environment.getTransactionFactory().newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
        Executor executor = configuration.newExecutor(transaction);
        return new DefaultSqlSession(configuration, executor);
    }
}
