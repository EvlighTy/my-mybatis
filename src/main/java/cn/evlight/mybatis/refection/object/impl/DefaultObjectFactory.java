package cn.evlight.mybatis.refection.object.impl;

import cn.evlight.mybatis.refection.object.ObjectFactory;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public class DefaultObjectFactory implements ObjectFactory {
    @Override
    public void setProperties(Properties properties) {
        //N/A
    }

    @Override
    public <T> T create(Class<T> clazz) {
        return create(clazz, null, null);
    }

    @Override
    public <T> T create(Class<T> clazz, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        Class<?> resolveClazz = resolve(clazz);
        return (T) instantiate(resolveClazz, constructorArgTypes, constructorArgs);
    }

    private <T> T instantiate(Class<T> clazz, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {
            Constructor<T> constructor;
            if (null == constructorArgs || constructorArgs.isEmpty()) {
                //无参
                constructor = clazz.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            } else {
                //有参
                constructor = clazz.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
            }
        } catch (Exception e) {
            throw new RuntimeException("occur a error during instantiate class cause:" + e, e);
        }
    }

    private Class<?> resolve(Class<?> clazz) {
        if (clazz == List.class || clazz == Collection.class || clazz == Iterable.class) {
            return ArrayList.class;
        } else if (clazz == Map.class) {
            return HashMap.class;
        } else if (clazz == SortedSet.class) {
            return TreeSet.class;
        } else if (clazz == Set.class) {
            return HashSet.class;
        } else {
            return clazz;
        }
    }
}
