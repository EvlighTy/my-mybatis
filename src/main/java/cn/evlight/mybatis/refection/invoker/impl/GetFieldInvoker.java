package cn.evlight.mybatis.refection.invoker.impl;

import cn.evlight.mybatis.refection.invoker.Invoker;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/30
 */
public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        return this.field.get(target);
    }

    @Override
    public Class<?> getType() {
        return this.field.getType();
    }
}
