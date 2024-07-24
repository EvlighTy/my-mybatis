package cn.evlight.mybatis.bind;

import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.type.enums.SqlCommandType;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class MapperMethod {

    private final SqlCommand sqlCommand;

    public <T> MapperMethod(Class<T> clazz, Method method, Configuration configuration) {
        this.sqlCommand = new SqlCommand(clazz, method, configuration);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (sqlCommand.getSqlCommandType()) {
            case SELECT:
                result = sqlSession.selectOne(sqlCommand.getSqlCommandId(), args);
                break;
            case INSERT:
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
        }
        return result;
    }

    private class SqlCommand {

        private String sqlCommandId;
        private SqlCommandType sqlCommandType;

        public <T> SqlCommand(Class<T> clazz, Method method, Configuration configuration) {
            String mapperStatementId = clazz.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(mapperStatementId);
            this.sqlCommandId = mappedStatement.getId();
            this.sqlCommandType = mappedStatement.getSqlCommandType();
        }

        public String getSqlCommandId() {
            return sqlCommandId;
        }

        public SqlCommandType getSqlCommandType() {
            return sqlCommandType;
        }
    }

}
