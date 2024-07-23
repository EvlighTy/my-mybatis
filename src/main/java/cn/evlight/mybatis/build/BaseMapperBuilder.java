package cn.evlight.mybatis.build;

import cn.evlight.mybatis.session.Configuration;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class BaseMapperBuilder {
    protected final Configuration configuration;

    public BaseMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
}
