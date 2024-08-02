package cn.evlight.mybatis.refection.invoker.impl;

import cn.evlight.mybatis.refection.invoker.Invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public class MethodInvoker implements Invoker {

    private Class<?> type;
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
