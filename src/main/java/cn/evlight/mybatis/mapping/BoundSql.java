package cn.evlight.mybatis.mapping;

import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public class BoundSql {

    private String sql;
    private String parameterType;
    private Map<Integer, String> parameters;
    private String resultType;

    public BoundSql(String sql, String parameterType, Map<Integer, String> parameters, String resultType) {
        this.sql = sql;
        this.parameterType = parameterType;
        this.parameters = parameters;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public Map<Integer, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, String> parameters) {
        this.parameters = parameters;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
