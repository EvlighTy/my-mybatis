package cn.evlight.mybatis.executor;

import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/27
 */
public interface Executor {

    <E> List<E> query(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean require) throws SQLException;

    void rollback(boolean require) throws SQLException;

    void close(boolean force);
}
