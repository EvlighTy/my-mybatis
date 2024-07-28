package cn.evlight.mybatis.transaction.jdbc;

import cn.evlight.mybatis.transaction.Transaction;
import cn.evlight.mybatis.type.enums.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class JdbcTransaction implements Transaction {

    protected DataSource dataSource;
    protected Connection connection;
    protected TransactionIsolationLevel transactionIsolationLevel = TransactionIsolationLevel.NONE;
    protected boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit) {
        this.dataSource = dataSource;
        this.transactionIsolationLevel = transactionIsolationLevel;
        this.autoCommit = autoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void commit() throws SQLException {
        if (null != connection && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (null != connection && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (null != connection && !connection.getAutoCommit()) {
            connection.close();
        }
    }
}
