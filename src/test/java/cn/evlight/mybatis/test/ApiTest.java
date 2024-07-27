package cn.evlight.mybatis.test;

import cn.evlight.mybatis.datasource.pooled.PooledDataSource;
import cn.evlight.mybatis.io.Resources;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.session.SqlSessionFactory;
import cn.evlight.mybatis.session.defaults.DefaultSqlSessionFactoryBuilder;
import cn.evlight.mybatis.test.dao.UserDao;
import cn.evlight.mybatis.test.po.User;
import com.alibaba.fastjson.JSON;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class ApiTest {
    public static void main(String[] args) throws DocumentException, InterruptedException {
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        while (true) {
            User result = userDao.getById(1L);
            System.err.println(JSON.toJSONString(result));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.cj.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/my_mybatis?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("123456");
        // 持续获得链接
        while (true) {
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
//            connection.close();
        }
    }
}
