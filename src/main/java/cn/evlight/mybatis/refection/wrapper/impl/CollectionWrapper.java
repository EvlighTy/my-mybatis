package cn.evlight.mybatis.refection.wrapper.impl;

import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;
import cn.evlight.mybatis.refection.wrapper.BaseObjectWrapper;

import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/31
 */
public class CollectionWrapper extends BaseObjectWrapper {

    private Collection<Object> raw;

    public CollectionWrapper(MetaObject metaObject, Collection<Object> raw) {
        super(metaObject);
        this.raw = raw;
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(PropertyTokenizer prop, Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String findProperty(String name, boolean useCamel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public void add(Object object) {
        raw.add(object);
    }

    @Override
    public <O> void addAll(List<O> objects) {
        raw.addAll(objects);
    }
}
