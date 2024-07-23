package cn.evlight.mybatis.build.impl;

import cn.evlight.mybatis.build.BaseMapperBuilder;
import cn.evlight.mybatis.io.Resources;
import cn.evlight.mybatis.mapping.MappedStatement;
import cn.evlight.mybatis.session.Configuration;
import cn.evlight.mybatis.type.SqlCommandType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class XmlMapperBuilder extends BaseMapperBuilder {

    private Element root;

    public XmlMapperBuilder(Reader reader) throws DocumentException {
        super(new Configuration());
        Document doc = new SAXReader().read(new InputSource(reader));
        root = doc.getRootElement();
    }

    public Configuration parseToConfiguration() {
        try {
            Element mappers = root.element("mappers");
            List<Element> mapperList = mappers.elements("mapper");
            for (Element element : mapperList) {
                String resource = element.attributeValue("resource");
                Reader resourceReader = Resources.getResourceAsReader(resource);
                Document doc = new SAXReader().read(new InputSource(resourceReader));
                Element root = doc.getRootElement();
                String namespace = root.attributeValue("namespace");
                List<Element> selectNodes = root.elements("select");
                for (Element node : selectNodes) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    String sql = node.getText();
                    HashMap<Integer, String> parameters = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameters.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }
                    String mappedStatementId = namespace + "." + id;
                    configuration.addMappedStatement(MappedStatement.builder()
                            .id(mappedStatementId)
                            .parameters(parameters)
                            .parameterType(parameterType)
                            .sql(sql)
                            .sqlCommandType(SqlCommandType.valueOf(node.getName().toUpperCase(Locale.ENGLISH)))
                            .resultType(resultType)
                            .build());
                }
                configuration.addMapper(Class.forName(namespace));
            }
        } catch (Exception e) {
            throw new RuntimeException("error parsing xml mapper:" + e, e);
        }
        return configuration;
    }

}
