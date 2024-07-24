package cn.evlight.mybatis.build;

import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.type.TypeAliasRegistry;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class BaseMapperBuilder {
    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }
}
