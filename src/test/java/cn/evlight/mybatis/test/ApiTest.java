package cn.evlight.mybatis.test;

import cn.evlight.mybatis.datasource.pooled.PooledDataSource;
import cn.evlight.mybatis.io.Resources;
import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.SystemMetaObject;
import cn.evlight.mybatis.session.SqlSession;
import cn.evlight.mybatis.session.SqlSessionFactory;
import cn.evlight.mybatis.session.defaults.DefaultSqlSessionFactoryBuilder;
import cn.evlight.mybatis.test.dao.UserDao;
import cn.evlight.mybatis.test.po.User;
import com.alibaba.fastjson.JSON;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

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
            connection.close();
        }
    }

    @Test
    public void test_reflection() {
        Teacher teacher = new Teacher();
        List<Teacher.Student> list = new ArrayList<>();
        list.add(new Teacher.Student());
        teacher.setName("小傅哥");
        teacher.setStudents(list);
        MetaObject metaObject = SystemMetaObject.forObject(teacher);
        logger.info("getGetterNames：{}", JSON.toJSONString(metaObject.getGetterNames()));
        logger.info("getSetterNames：{}", JSON.toJSONString(metaObject.getSetterNames()));
        logger.info("name的get方法返回值：{}", JSON.toJSONString(metaObject.getGetterType("name")));
        logger.info("students的set方法参数值：{}", JSON.toJSONString(metaObject.getGetterType("students")));
        logger.info("name的hasGetter：{}", metaObject.hasGetter("name"));
        logger.info("student.id（属性为对象）的hasGetter：{}", metaObject.hasGetter("student.id"));
        logger.info("获取name的属性值：{}", metaObject.getValue("name"));
        // 重新设置属性值
        metaObject.setValue("name", "小白");
        logger.info("设置name的属性值：{}", metaObject.getValue("name"));
        // 设置属性（集合）的元素值
        metaObject.setValue("students[0].id", "001");
        logger.info("获取students集合的第一个元素的属性值：{}", JSON.toJSONString(metaObject.getValue("students[0].id")));
        System.err.println(metaObject.hasGetter("student[0]"));
        logger.info("对象的序列化：{}", JSON.toJSONString(teacher));
    }

    static class Teacher {

        private String name;

        private double price;

        private List<Student> students;

        private Student student;

        public static class Student {

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }
    }

}
