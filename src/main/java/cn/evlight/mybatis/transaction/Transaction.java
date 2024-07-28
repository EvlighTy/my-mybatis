package cn.evlight.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
