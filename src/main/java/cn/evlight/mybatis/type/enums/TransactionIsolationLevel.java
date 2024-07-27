package cn.evlight.mybatis.type.enums;

import java.sql.Connection;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/24
 */
public enum TransactionIsolationLevel {

    NONE(Connection.TRANSACTION_NONE, "无"),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED, "读未提交"),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED, "读已提交"),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ, "可重复读"),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE, "串行化"),
    ;

    private final Integer code;
    private final String info;

    TransactionIsolationLevel(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static TransactionIsolationLevel fromString(String code) {
        for (TransactionIsolationLevel transactionIsolationLevel : TransactionIsolationLevel.values()) {
            if (transactionIsolationLevel.getCode().equals(Integer.parseInt(code))) {
                return transactionIsolationLevel;
            }
        }
        throw new IllegalArgumentException("No enum constant " + TransactionIsolationLevel.class.getCanonicalName() + "." + code);
    }

}
