package cn.evlight.mybatis.transaction;

import cn.evlight.mybatis.type.enums.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public interface TransactionFactory {

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit);

    Transaction newTransaction(Connection connection);
}
