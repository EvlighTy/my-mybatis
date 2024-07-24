package cn.evlight.mybatis.test;

import cn.evlight.mybatis.io.Resources;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.session.SqlSessionFactory;
import cn.evlight.mybatis.session.defaults.DefaultSqlSessionFactoryBuilder;
import cn.evlight.mybatis.test.dao.UserDao;
import cn.evlight.mybatis.test.po.User;
import com.alibaba.fastjson.JSON;
import org.dom4j.DocumentException;

import java.io.Reader;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class ApiTest {
    public static void main(String[] args) throws DocumentException {
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User result = userDao.getById(1L);
        System.err.println(JSON.toJSONString(result));
    }
}
