package cn.evlight.mybatis.refection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException;

    Class<?> getType();
}
