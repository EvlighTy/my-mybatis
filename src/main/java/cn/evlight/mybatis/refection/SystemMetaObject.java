package cn.evlight.mybatis.refection;

import cn.evlight.mybatis.refection.object.ObjectFactory;
import cn.evlight.mybatis.refection.object.impl.DefaultObjectFactory;
import cn.evlight.mybatis.refection.wrapper.factory.DefaultObjectWrapperFactory;
import cn.evlight.mybatis.refection.wrapper.factory.ObjectWrapperFactory;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public class SystemMetaObject {

    public static final ObjectFactory objectFactory = new DefaultObjectFactory();
    public static final ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, objectFactory, objectWrapperFactory);

    private static class NullObject {

    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }


}
