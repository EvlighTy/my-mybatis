package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.mapping.Environment;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("方法: " + statement + " 被代理了");
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        try {
            Environment environment = configuration.getEnvironment();
            Connection connection = environment.getDataSource().getConnection();
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(mappedStatement.getBoundSql().getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> result = resultSet2Obj(resultSet, Class.forName(mappedStatement.getBoundSql().getResultType()));
            if (result.isEmpty()) {
                return (T) ("data can not find from database for sql:" + mappedStatement.getBoundSql().getSql());
            }
            return result.get(0);
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


    @Override
    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
