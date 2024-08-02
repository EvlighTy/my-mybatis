package cn.evlight.mybatis.refection.wrapper.impl;

import cn.evlight.mybatis.refection.MetaClass;
import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.SystemMetaObject;
import cn.evlight.mybatis.refection.invoker.Invoker;
import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;
import cn.evlight.mybatis.refection.wrapper.BaseObjectWrapper;

import java.util.List;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/31
 */
public class BeanWrapper extends BaseObjectWrapper {

    private Object raw;
    private MetaClass metaClass;

    public BeanWrapper(MetaObject metaObject, Object raw) {
        super(metaObject);
        this.raw = raw;
        this.metaClass = MetaClass.forClass(raw.getClass());
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, raw);
            return getCollectionValue(prop, collection);
        } else {
            return getBeanProperty(prop, raw);
        }
    }

    private Object getBeanProperty(PropertyTokenizer prop, Object target) {
        try {
            Invoker getInvoker = metaClass.getGetInvoker(prop.getName());
            return getInvoker.invoke(target, new Object[0]);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("can not invoke getter cause:" + e, e);
        }
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, raw);
            setCollectionValue(prop, collection, value);
        } else {
            setBeanProperty(prop, raw, value);
        }
    }

    private void setBeanProperty(PropertyTokenizer prop, Object target, Object value) {
        try {
            Invoker setInvoker = metaClass.getSetInvoker(prop.getName());
            setInvoker.invoke(target, new Object[]{value});
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("can not invoke setter cause:" + e, e);
        }
    }


    @Override
    public String findProperty(String name, boolean useCamel) {
        return metaClass.findPropertyName(name, useCamel);
    }

    @Override
    public String[] getGetterNames() {
        return metaClass.getGetterNames();
    }

    @Override
    public String[] getSetterNames() {
        return metaClass.getSetterNames();
    }

    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                return metaClass.getGetterType(name);
            } else {
                return metaObject.getGetterType(prop.getChild());
            }
        } else {
            return metaClass.getGetterType(name);
        }
    }

    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                return metaClass.getSetterType(name);
            } else {
                return metaObject.getSetterType(prop.getChild());
            }
        } else {
            return metaClass.getSetterType(name);
        }
    }

    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (metaClass.hasGetter(prop.getNameAndIndex())) {
                MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
                if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                    return metaClass.hasGetter(name);
                } else {
                    return metaObject.hasGetter(prop.getChild());
                }
            } else {
                return false;
            }
        } else {
            return metaClass.hasGetter(name);
        }
    }

    @Override
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (metaClass.hasSetter(prop.getNameAndIndex())) {
                MetaObject metaObject = this.metaObject.metaObjectForProperty(prop.getNameAndIndex());
                if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                    return metaClass.hasSetter(name);
                } else {
                    return metaObject.hasSetter(prop.getChild());
                }
            } else {
                return false;
            }
        } else {
            return metaClass.hasSetter(name);
        }
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        Class<?> type = getSetterType(prop.getName());
        Object value = objectFactory.create(type);
        MetaObject metaObject = MetaObject.forObject(value, this.metaObject.getObjectFactory(), this.metaObject.getObjectWrapperFactory());
        set(prop, value);
        return metaObject;
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
