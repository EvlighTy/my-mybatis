package cn.evlight.mybatis.refection.wrapper.factory;

import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.wrapper.ObjectWrapper;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(Object object, MetaObject metaObject) {
        throw new RuntimeException("default object wrapper factory do not provide wrapper");
    }
}
