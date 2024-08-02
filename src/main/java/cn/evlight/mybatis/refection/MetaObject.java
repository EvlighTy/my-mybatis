package cn.evlight.mybatis.refection;

import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;
import cn.evlight.mybatis.refection.wrapper.ObjectWrapper;
import cn.evlight.mybatis.refection.wrapper.factory.ObjectWrapperFactory;
import cn.evlight.mybatis.refection.wrapper.impl.BeanWrapper;
import cn.evlight.mybatis.refection.wrapper.impl.CollectionWrapper;
import cn.evlight.mybatis.refection.wrapper.impl.MapWrapper;

import java.util.Collection;
import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public class MetaObject {

    private Object originalObject;
    private ObjectWrapper objectWrapper;
    private ObjectFactory objectFactory;
    private ObjectWrapperFactory objectWrapperFactory;

    public MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;
        if (object instanceof ObjectWrapper) {
            this.objectWrapper = (ObjectWrapper) object;
        } else if (this.objectWrapperFactory.hasWrapperFor(object)) {
            this.objectWrapperFactory.getWrapperFor(object, this);
        } else if (object instanceof Map) {
            this.objectWrapper = new MapWrapper(this, (Map<String, Object>) object);
        } else if (object instanceof Collection) {
            this.objectWrapper = new CollectionWrapper(this, (Collection<Object>) object);
        } else {
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    public static MetaObject forObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        if (object == null) {
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object, objectFactory, objectWrapperFactory);
        }
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    public void setOriginalObject(Object originalObject) {
        this.originalObject = originalObject;
    }

    public ObjectWrapper getObjectWrapper() {
        return objectWrapper;
    }

    public void setObjectWrapper(ObjectWrapper objectWrapper) {
        this.objectWrapper = objectWrapper;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                return null;
            } else {
                return metaObject.getValue(prop.getChild());
            }
        } else {
            return objectWrapper.get(prop);
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaObject = metaObjectForProperty(prop.getNameAndIndex());
            if (metaObject == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null) {
                    return;
                } else {
                    metaObject = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
                }
            }
            metaObject.setValue(prop.getChild(), value);
        } else {
            objectWrapper.set(prop, value);
        }
    }

    public MetaObject metaObjectForProperty(String name) {
        Object object = getValue(name);
        return MetaObject.forObject(object, this.objectFactory, this.objectWrapperFactory);
    }

    public boolean hasGetter(String name) {
        return objectWrapper.hasGetter(name);
    }

    public boolean hasSetter(String name) {
        return objectWrapper.hasSetter(name);
    }

    public Class<?> getGetterType(String name) {
        return objectWrapper.getGetterType(name);
    }

    public Class<?> getSetterType(String name) {
        return objectWrapper.getSetterType(name);
    }

    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }

    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

}
