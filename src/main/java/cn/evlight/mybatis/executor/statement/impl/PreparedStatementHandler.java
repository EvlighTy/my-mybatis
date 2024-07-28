package cn.evlight.mybatis.executor.statement.impl;

import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.executor.statement.BaseStatementHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class PreparedStatementHandler extends BaseStatementHandler {

    public PreparedStatementHandler(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, Executor executor, BoundSql boundSql) {
        super(mappedStatement, parameter, resultSetHandler, executor, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        ((PreparedStatement) statement).setLong(1, Long.parseLong((((Object[]) parameter)[0]).toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.execute();
        return this.resultSetHandler.handle(preparedStatement);
    }
}
