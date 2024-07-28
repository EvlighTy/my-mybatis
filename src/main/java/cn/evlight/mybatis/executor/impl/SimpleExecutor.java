package cn.evlight.mybatis.executor.impl;

import cn.evlight.mybatis.executor.BaseExecutor;
import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.executor.statement.StatementHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        try {
            StatementHandler statementHandler = configuration.newStatementHandler(this, mappedStatement, parameter, resultSetHandler, boundSql);
            Statement statement = statementHandler.prepare(transaction.getConnection());
            statementHandler.parameterize(statement);
            return statementHandler.query(statement, resultSetHandler);
        } catch (SQLException e) {
            logger.error("occur a error during execute sql" + e, e);
            return null;
        }
    }
}
