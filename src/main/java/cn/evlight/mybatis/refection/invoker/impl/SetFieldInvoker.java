package cn.evlight.mybatis.refection.invoker.impl;

import cn.evlight.mybatis.refection.invoker.Invoker;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        this.field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return this.field.getType();
    }
}
