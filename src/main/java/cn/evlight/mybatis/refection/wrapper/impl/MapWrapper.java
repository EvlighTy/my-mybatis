package cn.evlight.mybatis.refection.wrapper.impl;

import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.SystemMetaObject;
import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;
import cn.evlight.mybatis.refection.wrapper.BaseObjectWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public class MapWrapper extends BaseObjectWrapper {

    private Map<String, Object> raw;

    public MapWrapper(MetaObject metaObject, Map<String, Object> raw) {
        super(metaObject);
        this.raw = raw;
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            //属性是集合
            Object collection = resolveCollection(prop, raw);
            return getCollectionValue(prop, collection);
        } else {
            return raw.get(prop.getName());
        }
    }

    @Override
    public void set(PropertyTokenizer prop, Object object) {
        if (prop.getIndex() != null) {
            //属性是集合
            Object collection = resolveCollection(prop, raw);
            setCollectionValue(prop, collection, object);
        } else {
            raw.put(prop.getName(), object);
        }
    }

    @Override
    public String findProperty(String name, boolean useCamel) {
        return name;
    }

    @Override
    public String[] getGetterNames() {
        return raw.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getSetterNames() {
        return raw.keySet().toArray(new String[0]);
    }

    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaObject.getGetterType(prop.getChild());
            }
        } else {
            if (raw.get(name) != null) {
                return raw.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaObject.getSetterType(prop.getChild());
            }
        } else {
            if (raw.get(name) != null) {
                return raw.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (raw.containsKey(prop.getNameAndIndex())) {
                MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
                if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                    return false;
                } else {
                    return metaObject.hasGetter(prop.getChild());
                }
            } else {
                return false;
            }
        } else {
            return raw.containsKey(prop.getName());
        }
    }

    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        HashMap<String, Object> map = new HashMap<>();
        set(prop, map);
        return MetaObject.forObject(map, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory());
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public void add(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <O> void addAll(List<O> object) {
        throw new UnsupportedOperationException();
    }
}
