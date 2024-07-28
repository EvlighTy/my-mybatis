package cn.evlight.mybatis.transaction.jdbc;

import cn.evlight.mybatis.transaction.Transaction;
import cn.evlight.mybatis.transaction.TransactionFactory;
import cn.evlight.mybatis.type.enums.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit) {
        return new JdbcTransaction(dataSource, transactionIsolationLevel, autoCommit);
    }

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }
}
