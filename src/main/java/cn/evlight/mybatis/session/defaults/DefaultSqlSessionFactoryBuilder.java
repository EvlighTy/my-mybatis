package cn.evlight.mybatis.session.defaults;

import cn.evlight.mybatis.build.impl.XmlMapperBuilder;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.session.SqlSessionFactory;
import org.dom4j.DocumentException;

import java.io.Reader;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class DefaultSqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) throws DocumentException {
        Configuration configuration = new XmlMapperBuilder(reader).parseToConfiguration();
        return new DefaultSqlSessionFactory(configuration);
    }
}
