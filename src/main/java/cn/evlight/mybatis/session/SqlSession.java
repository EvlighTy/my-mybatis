package cn.evlight.mybatis.session;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> T getMapper(Class<T> clazz);

    Configuration getConfiguration();
}
