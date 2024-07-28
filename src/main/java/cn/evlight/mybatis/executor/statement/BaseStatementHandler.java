package cn.evlight.mybatis.executor.statement;

import cn.evlight.mybatis.executor.Executor;
import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final MappedStatement mappedStatement;
    protected final Object parameter;
    protected final ResultSetHandler resultSetHandler;
    protected final Executor executor;
    protected BoundSql boundSql;

    protected BaseStatementHandler(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, Executor executor, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.parameter = parameter;
        this.resultSetHandler = this.configuration.newResultResultHandler(executor, mappedStatement, boundSql);
        this.executor = executor;
        this.boundSql = boundSql;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = instantiateStatement(connection);
        statement.setQueryTimeout(500);
        statement.setFetchSize(10000);
        return statement;
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
