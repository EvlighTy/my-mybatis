package cn.evlight.mybatis.datasource.pooled;

import cn.evlight.mybatis.datasource.unpooled.UnPooledDataSource;
import cn.evlight.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/25
 */
public class PooledDataSourceFactory extends UnPooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new UnPooledDataSource();
    }

}
