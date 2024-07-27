package cn.evlight.mybatis.datasource.pooled;

import cn.evlight.mybatis.datasource.unpooled.UnPooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/25
 */
public class PooledDataSource implements DataSource {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);
    private final UnPooledDataSource dataSource;
    private final PoolState poolState = new PoolState(this);
    private int maxActive = 10; //最大活跃连接数
    private int maxIdle = 5; //最大空闲连接数
    private int checkoutTime = 20000; //连接超时时间
    private int poolTimeToWait = 20000; //等待连接时间
    private String poolPingQuery = "NO PING QUERY SET"; //ping侦测语句
    private boolean poolPingEnabled = false; //是否启用ping侦测
    private int idleTimeForPing = 0;
    private int expectedConnectionTypeCode;
    private final int retryTolerance = 3; //获取连接允许最大重试次数

    public PooledDataSource() {
        this.dataSource = new UnPooledDataSource();
    }

    private int getConnectionTypeCode(String url, String username, String password) {
        return (url + username + password).hashCode();
    }

    public String getDriver() {
        return this.dataSource.getDriver();
    }

    public void setDriver(String driver) {
        this.dataSource.setDriver(driver);
        forceCloseAll();
    }

    private void forceCloseAll() {
        synchronized (poolState) {
            //重新生成设置识别码
            expectedConnectionTypeCode = getConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            //关闭所有活跃链接和空闲连接
            try {
                for (int i = poolState.activeConnections.size(); i > 0; i--) {
                    PooledConnection connection = poolState.activeConnections.remove(i - 1);
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    connection.getRealConnection().close();
                }
                for (int i = poolState.idleConnections.size(); i > 0; i--) {
                    PooledConnection connection = poolState.idleConnections.remove(i - 1);
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    connection.getRealConnection().close();
                }
            } catch (SQLException ignore) {

            }
            logger.info("force fully close all connections");
        }
    }

    public String getUrl() {
        return this.dataSource.getUrl();
    }

    public void setUrl(String url) {
        this.dataSource.setUrl(url);
        forceCloseAll();
    }

    public String getUsername() {
        return this.dataSource.getUsername();
    }

    public void setUsername(String username) {
        this.dataSource.setUsername(username);
        forceCloseAll();
    }

    public String getPassword() {
        return this.dataSource.getPassword();
    }

    public void setPassword(String password) {
        this.dataSource.setPassword(password);
        forceCloseAll();
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
        forceCloseAll();
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        forceCloseAll();
    }

    public int getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(int checkoutTime) {
        this.checkoutTime = checkoutTime;
        forceCloseAll();
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
        forceCloseAll();
    }

    public String getPoolPingQuery() {
        return poolPingQuery;
    }

    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
        forceCloseAll();
    }

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
        forceCloseAll();
    }

    //---------------------------------------------------------------------------------------------------------------------

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean waited = false;
        PooledConnection connection = null;
        int retry = 0;
        long start = 0;
        while (null == connection) {
            synchronized (poolState) {
                if (!poolState.idleConnections.isEmpty()) {
                    //有空闲连接
                    connection = poolState.idleConnections.remove(0);
                    logger.info("checkout connection from pool:" + connection.getRealHashCode());
                } else {
                    //无空闲连接
                    if (poolState.activeConnections.size() < maxActive) {
                        //活跃连接数小于最大值->创建新连接
                        connection = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("create new connection from pool:" + connection.getRealHashCode());
                    } else {
                        //活跃连接数达到阈值->尝试获取最早借出的连接
                        PooledConnection oldConnection = poolState.activeConnections.get(0);
                        long checkoutTime = oldConnection.getCheckoutTime();
                        if (checkoutTime >= this.checkoutTime) {
                            //该连接已超时->丢弃该连接创建新连接
                            poolState.totalCheckoutTime += checkoutTime;
                            poolState.totalTimeoutConnectionCheckoutTime += checkoutTime;
                            poolState.totalTimeoutConnectionCount++;
                            if (!oldConnection.getRealConnection().getAutoCommit()) {
                                oldConnection.getRealConnection().rollback();
                            }
                            poolState.activeConnections.remove(oldConnection);
                            oldConnection.setValid(Boolean.FALSE);
                            connection = new PooledConnection(dataSource.getConnection(), this);
                            logger.info("find a timeout connection,discard to create a new one:" + connection.getRealHashCode());
                        } else {
                            //该连接未超时->请求等待
                            try {
                                if (!waited) {
                                    poolState.totalWaitCount++;
                                    waited = true;
                                }
                                long waitTimeStamp = System.currentTimeMillis();
                                logger.info("request into wait for " + poolTimeToWait);
                                poolState.wait(poolTimeToWait);
                                poolState.totalWaitTime += System.currentTimeMillis() - waitTimeStamp;
                            } catch (InterruptedException e) {
                                logger.error("occur a error in waiting:" + e, e);
                                break;
                            }
                        }
                    }
                }
                if (null != connection) {
                    //成功获取连接
                    if (connection.isValid()) {
                        //连接可用
                        if (!connection.getRealConnection().getAutoCommit()) {
                            connection.getRealConnection().rollback();
                        }
                        connection.setConnectionTypeCode(getConnectionTypeCode(dataSource.getUrl(), username, password));
                        long now = System.currentTimeMillis();
                        connection.setCheckoutTimeStamp(now);
                        connection.setLastUsedTimeStamp(now);
                        poolState.activeConnections.add(connection);
                        poolState.requestCount++;
                        poolState.totalRequestTime += start - now;
                    } else {
                        //连接不可用->丢弃连接重试
                        logger.error("get a bad connection form pool" + connection.getRealHashCode());
                        retry++;
                        connection = null;
                        if (retry > retryTolerance) {
                            //重试次数达到阈值
                            throw new SQLException("could not get a connection from pool");
                        }
                    }
                }
            }
        }
        if (null == connection) {
            throw new SQLException("unknown error,pool return a null connection");
        }
        return connection;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + "is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        DriverManager.setLogWriter(printWriter);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (poolState) {
            poolState.activeConnections.remove(connection);
            if (connection.isValid()) {
                //连接可用
                poolState.totalCheckoutTime += connection.getCheckoutTime();
                if (!connection.getRealConnection().getAutoCommit()) {
                    connection.getRealConnection().rollback();
                }
                if (poolState.idleConnections.size() < maxIdle && expectedConnectionTypeCode == connection.getConnectionTypeCode()) {
                    //连接池当前空闲连接小于最大值->归还连接到连接池
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    newConnection.setCreatedTimeStamp(connection.getCreatedTimeStamp());
                    newConnection.setLastUsedTimeStamp(connection.getLastUsedTimeStamp());
                    poolState.idleConnections.add(newConnection);
                    logger.info("return a connection to pool:" + connection.getRealHashCode());
                    poolState.notifyAll();
                } else {
                    //->关闭连接

                    connection.getRealConnection().close();
                    logger.info("close a connection" + connection.getRealHashCode());
                }
                connection.setValid(Boolean.FALSE);
            } else {
                //连接不可用
                poolState.badConnectionCount++;
                throw new RuntimeException("find a bad connection:" + connection.getHashCode());
            }
        }
    }

    public boolean pingConnection(PooledConnection connection) {
        try {
            Connection realConnection = connection.getRealConnection();
            if (realConnection.isClosed()) {
                return false;
            } else if (this.poolPingEnabled) {
                if (idleTimeForPing >= 0 && connection.getIdleTime() >= idleTimeForPing) {
                    Statement statement = realConnection.createStatement();
                    ResultSet resultSet = statement.executeQuery(poolPingQuery);
                    resultSet.close();
                    if (!realConnection.getAutoCommit()) {
                        realConnection.rollback();
                    }
                    return true;
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int getIdleTimeForPing() {
        return idleTimeForPing;
    }

    public void setIdleTimeForPing(int idleTimeForPing) {
        this.idleTimeForPing = idleTimeForPing;
    }
}
