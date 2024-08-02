package cn.evlight.mybatis.refection.wrapper.factory;

import cn.evlight.mybatis.refection.MetaObject;
import cn.evlight.mybatis.refection.wrapper.ObjectWrapper;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public interface ObjectWrapperFactory {

    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(Object object, MetaObject metaObject);
}
