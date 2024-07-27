package cn.evlight.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/25
 */
public class PoolState {

    protected final List<PooledConnection> activeConnections = new ArrayList<>();
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    protected final PooledDataSource dataSource;
    protected long requestCount = 0; //总请求连接数
    protected long totalRequestTime = 0; //总请求连接时间
    protected long totalCheckoutTime = 0; //连接总借出时间
    protected long totalTimeoutConnectionCount = 0; //总超时连接数
    protected long totalTimeoutConnectionCheckoutTime = 0; //超时连接总借出时间
    protected long totalWaitCount = 0; //总等待数
    protected long totalWaitTime = 0; //总等待时间
    protected long badConnectionCount = 0; //无效连接数

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : totalRequestTime / requestCount;
    }

    public synchronized long getAverageCheckoutTime() {
        return requestCount == 0 ? 0 : totalCheckoutTime / requestCount;
    }

    public synchronized long getTotalTimeoutConnectionCount() {
        return totalTimeoutConnectionCount;
    }

    public synchronized long getAverageTimeoutConnectionCheckoutTime() {
        return totalTimeoutConnectionCount == 0 ? 0 : totalTimeoutConnectionCheckoutTime / totalTimeoutConnectionCount;
    }

    public synchronized long getTotalWaitCount() {
        return totalWaitCount;
    }

    public synchronized long getAverageTotalWaitTime() {
        return totalWaitCount == 0 ? 0 : totalWaitTime / totalWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

}
