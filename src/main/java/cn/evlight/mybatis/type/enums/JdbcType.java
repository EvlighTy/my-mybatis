package cn.evlight.mybatis.type.enums;

import java.sql.Types;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public enum JdbcType {
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    TIMESTAMP(Types.TIMESTAMP);

    private int code;

    JdbcType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
