package cn.evlight.mybatis.refection.wrapper;

import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;

import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public interface ObjectWrapper {

    Object get(PropertyTokenizer prop);

    void set(PropertyTokenizer prop, Object object);

    String findProperty(String name, boolean useCamel);

    String[] getGetterNames();

    String[] getSetterNames();

    Class<?> getGetterType(String name);

    Class<?> getSetterType(String name);

    boolean hasGetter(String name);

    boolean hasSetter(String name);

    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

    boolean isCollection();

    void add(Object object);

    <O> void addAll(List<O> object);
}
