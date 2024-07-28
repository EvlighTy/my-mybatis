package cn.evlight.mybatis.mapping;

import cn.evlight.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public class Environment {

    private final String id;
    private final DataSource dataSource;
    private final TransactionFactory transactionFactory;

    public Environment(String id, DataSource dataSource, TransactionFactory transactionFactory) {
        this.id = id;
        this.dataSource = dataSource;
        this.transactionFactory = transactionFactory;
    }

    public String getId() {
        return id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }
}
