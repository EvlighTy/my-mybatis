package cn.evlight.mybatis.bind;

import cn.evlight.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/21
 */
public class MapperProxy<T> implements InvocationHandler {

    private final Class<T> clazz;
    private final SqlSession sqlSession;
    private final Map<Method, MapperMethod> mapperMethodMap = new HashMap<>();

    public MapperProxy(Class<T> clazz, SqlSession sqlSession) {
        this.clazz = clazz;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        MapperMethod mapperMethod = mapperMethodMap.get(method);
        if (null == mapperMethod) {
            mapperMethod = new MapperMethod(clazz, method, sqlSession.getConfiguration());
            mapperMethodMap.put(method, mapperMethod);
        }
        return mapperMethod.execute(sqlSession, args);
    }
}
