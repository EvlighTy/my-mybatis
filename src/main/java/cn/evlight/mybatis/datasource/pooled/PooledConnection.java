package cn.evlight.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/25
 */
public class PooledConnection implements InvocationHandler {

    private final int hashCode;
    private PooledDataSource dataSource;
    private Connection realConnection;
    private Connection proxyConnection;
    private long checkoutTimeStamp;
    private long createdTimeStamp;
    private long lastUsedTimeStamp;
    private int connectionTypeCode;
    private boolean valid;

    public PooledConnection(Connection connection, PooledDataSource pooledDataSource) {
        this.hashCode = connection.hashCode();
        this.dataSource = pooledDataSource;
        this.realConnection = connection;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, this);
        this.checkoutTimeStamp = System.currentTimeMillis();
        this.createdTimeStamp = System.currentTimeMillis();
        this.lastUsedTimeStamp = System.currentTimeMillis();
        this.valid = true;
    }

    public int getHashCode() {
        return hashCode;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public long getCheckoutTimeStamp() {
        return checkoutTimeStamp;
    }

    public void setCheckoutTimeStamp(long checkoutTimeStamp) {
        this.checkoutTimeStamp = checkoutTimeStamp;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public long getLastUsedTimeStamp() {
        return lastUsedTimeStamp;
    }

    public void setLastUsedTimeStamp(long lastUsedTimeStamp) {
        this.lastUsedTimeStamp = lastUsedTimeStamp;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public boolean isValid() {
        return valid && null != realConnection && dataSource.pingConnection(this);
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            //归还连接
            dataSource.pushConnection(this);
            return null;
        } else {
            if (!Object.class.equals(method.getDeclaringClass())) {
                checkConnectionState();
            }
            return method.invoke(realConnection, args);
        }
    }

    private void checkConnectionState() {
        if (isValid()) {
            return;
        }
        throw new RuntimeException("Connection is invalid");
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection) {
            return realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode();
        } else if (obj instanceof Connection) {
            return this.hashCode == obj.hashCode();
        }
        return false;
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - lastUsedTimeStamp;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimeStamp;
    }

    public int getRealHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }
}
