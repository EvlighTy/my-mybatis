package cn.evlight.mybatis.mapping;

import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.type.enums.SqlCommandType;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class MappedStatement {

    private Configuration configuration;
    private String id;
    private SqlCommandType sqlCommandType;
    private BoundSql boundSql;

    private MappedStatement(Builder builder) {
        this.configuration = builder.configuration;
        this.id = builder.id;
        this.sqlCommandType = builder.sqlCommandType;
        this.boundSql = builder.boundSql;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static class Builder {

        private Configuration configuration;
        private String id;
        private SqlCommandType sqlCommandType;
        private BoundSql boundSql;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder sqlCommandType(SqlCommandType sqlCommandType) {
            this.sqlCommandType = sqlCommandType;
            return this;
        }

        public Builder boundSql(BoundSql boundSql) {
            this.boundSql = boundSql;
            return this;
        }

        public Builder configuration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public MappedStatement build() {
            return new MappedStatement(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}
