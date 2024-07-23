package cn.evlight.mybatis.mapping;

import cn.evlight.mybatis.type.SqlCommandType;

import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class MappedStatement {

    private String id;
    private String sql;
    private SqlCommandType sqlCommandType;
    private String parameterType;
    private Map<Integer, String> parameters;
    private String resultType;

    private MappedStatement(Builder builder) {
        this.id = builder.id;
        this.sqlCommandType = builder.sqlCommandType;
        this.parameterType = builder.parameterType;
        this.parameters = builder.parameters;
        this.resultType = builder.resultType;
        this.sql = builder.sql;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public Map<Integer, String> getParameters() {
        return parameters;
    }

    public String getResultType() {
        return resultType;
    }

    public String getSql() {
        return sql;
    }

    public static class Builder {
        private String id;
        private SqlCommandType sqlCommandType;
        private String parameterType;
        private Map<Integer, String> parameters;
        private String resultType;
        private String sql;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder sqlCommandType(SqlCommandType sqlCommandType) {
            this.sqlCommandType = sqlCommandType;
            return this;
        }

        public Builder parameterType(String parameterType) {
            this.parameterType = parameterType;
            return this;
        }

        public Builder parameters(Map<Integer, String> parameter) {
            this.parameters = parameter;
            return this;
        }

        public Builder resultType(String resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder sql(String sql) {
            this.sql = sql;
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
