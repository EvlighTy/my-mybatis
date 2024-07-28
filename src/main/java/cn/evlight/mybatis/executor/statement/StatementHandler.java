package cn.evlight.mybatis.executor.statement;

import cn.evlight.mybatis.executor.resultset.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public interface StatementHandler {

    Statement prepare(Connection connection) throws SQLException;

    void parameterize(Statement statement) throws SQLException;

    <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws SQLException;
}
