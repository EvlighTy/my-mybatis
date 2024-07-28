package cn.evlight.mybatis.executor.statement.impl;

import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.executor.statement.BaseStatementHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class SimpleStatementHandler extends BaseStatementHandler {

    protected SimpleStatementHandler(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, Executor executor, BoundSql boundSql) {
        super(mappedStatement, parameter, resultSetHandler, executor, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement) {
        //N/A
    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException {
        statement.execute(boundSql.getSql());
        return resultSetHandler.handle(statement);
    }
}
