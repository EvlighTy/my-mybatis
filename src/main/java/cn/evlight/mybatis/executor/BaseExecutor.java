package cn.evlight.mybatis.executor;

import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.mapping.BoundSql;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public abstract class BaseExecutor implements Executor {

    protected final Logger logger = LoggerFactory.getLogger(BaseExecutor.class);
    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;
    protected boolean closed;

    protected BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }

    @Override
    public <E> List<E> query(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("executor is closed");
        }
        return doQuery(mappedStatement, parameter, resultSetHandler, boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement mappedStatement, Object parameter, ResultSetHandler resultSetHandler, BoundSql boundSql);

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("executor is closed");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.commit();
            }
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close(boolean force) {
        if (!closed) {
            try {
                try {
                    rollback(force);
                } finally {
                    transaction.close();
                }
            } catch (Exception e) {
                logger.warn("occur a error during closing executor:" + e, e);
            } finally {
                closed = true;
                transaction = null;
            }
        }
    }
}
