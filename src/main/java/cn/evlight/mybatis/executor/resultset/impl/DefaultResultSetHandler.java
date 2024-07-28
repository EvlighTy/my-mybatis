package cn.evlight.mybatis.executor.resultset.impl;

import cn.evlight.mybatis.executor.resultset.ResultSetHandler;
import cn.evlight.mybatis.mapping.BoundSql;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/28
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final BoundSql boundSql;

    public DefaultResultSetHandler(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    @Override
    public <E> List<E> handle(Statement statement) {
        try {
            return resultSet2Obj(statement.getResultSet(), Class.forName(boundSql.getResultType()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        try {
            if (!resultSet.isBeforeFirst()) {
                return Collections.emptyList();
            }
            ArrayList<T> result = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T instance = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(methodName, LocalDateTime.class);
                        method.invoke(instance, ((Timestamp) value).toLocalDateTime());
                    } else {
                        method = clazz.getMethod(methodName, value.getClass());
                        method.invoke(instance, value);
                    }
                }
                result.add(instance);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
