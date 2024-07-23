package cn.evlight.mybatis.session;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
