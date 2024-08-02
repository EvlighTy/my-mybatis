package cn.evlight.mybatis.refection;

import cn.evlight.mybatis.refection.invoker.Invoker;
import cn.evlight.mybatis.refection.invoker.impl.GetFieldInvoker;
import cn.evlight.mybatis.refection.invoker.impl.MethodInvoker;
import cn.evlight.mybatis.refection.invoker.impl.SetFieldInvoker;
import cn.evlight.mybatis.refection.property.PropertyNamer;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/31
 */
public class Reflector {

    private static boolean cacheEnabled = true;
    private static final Map<Class<?>, Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();
    private Class<?> clazz;
    private String[] readablePropertyNames = new String[0];
    private String[] writeablePropertyNames = new String[0];
    private Map<String, Invoker> getMethods = new HashMap<>();
    private Map<String, Invoker> setMethods = new HashMap<>();
    private Map<String, Class<?>> getTypes = new HashMap<>();
    private Map<String, Class<?>> setTypes = new HashMap<>();
    private Constructor<?> defaultConstructor;
    private Map<String, String> caseInsensitivePropertyNameMap = new HashMap<>();

    public Reflector(Class<?> clazz) {
        this.clazz = clazz;
        addDefaultConstructor(); //添加默认构造函数
        addGetMethods(); //添加字段的get方法
        addSetMethods(); //添加字段的set方法
        addFields(this.clazz); //添加非final/static修饰属性的get和set方法
        readablePropertyNames = getMethods.keySet().toArray(new String[0]); //可读属性
        writeablePropertyNames = setMethods.keySet().toArray(new String[0]); //可写属性
        for (String propName : readablePropertyNames) {
            caseInsensitivePropertyNameMap.put(propName.toLowerCase(Locale.ENGLISH), propName);
        }
        for (String propName : writeablePropertyNames) {
            caseInsensitivePropertyNameMap.put(propName.toLowerCase(Locale.ENGLISH), propName);
        }
    }

    public static Reflector forClass(Class<?> clazz) {
        if (cacheEnabled) {
            Reflector reflector = REFLECTOR_MAP.get(clazz);
            if (reflector != null) {
                return reflector;
            } else {
                reflector = new Reflector(clazz);
                REFLECTOR_MAP.put(clazz, reflector);
                return reflector;
            }
        } else {
            return new Reflector(clazz);
        }
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (privateMethodAccessible()) {
                try {
                    field.setAccessible(true);
                } catch (Exception ignore) {

                }
            }
            if (field.isAccessible()) {
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
                if (!setMethods.containsKey(field.getName())) {
                    int modifiers = field.getModifiers();
                    if (!(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers))) {
                        addSetField(field);
                    }
                }
            }
        }
    }

    private void addSetField(Field field) {
        if (isValidProperty(field.getName())) {
            setMethods.put(field.getName(), new SetFieldInvoker(field));
            setTypes.put(field.getName(), field.getType());
        }
    }

    private void addGetField(Field field) {
        if (isValidProperty(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }

    private void addSetMethods() {
        HashMap<String, List<Method>> conflictMethods = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String methodName = method.getName();
            if ((methodName.startsWith("set") && methodName.length() > 3)) {
                String propertyName = PropertyNamer.method2Property(methodName);
                List<Method> list = conflictMethods.computeIfAbsent(propertyName, k -> new ArrayList<>());
                list.add(method);
            }
        }
        for (String propertyName : conflictMethods.keySet()) {
            List<Method> list = conflictMethods.get(propertyName);
            Method firstMethod = list.get(0);
            if (list.size() == 1) {
                addSetMethod(propertyName, firstMethod);
            } else {
                Class<?> getterType = getTypes.get(propertyName);
                if (getterType == null) {
                    throw new RuntimeException("the corresponding getter for setter dose not exist");
                }
                Iterator<Method> iterator = list.iterator();
                Method setter = null;
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    if (method.getParameterCount() == 1 && getterType.equals(method.getParameterTypes()[0])) {
                        setter = method;
                        break;
                    }
                }
                if (setter == null) {
                    throw new RuntimeException("can not find a setter");
                }
                addSetMethod(propertyName, setter);
            }
        }
    }

    private Method[] getClassMethods(Class<?> clazz) {
        HashMap<String, Method> methodMap = new HashMap<>();
        Class<?> current = clazz;
        while (current != null) {
            addUniqueMethods(methodMap, clazz.getDeclaredMethods());
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(methodMap, anInterface.getDeclaredMethods());
            }
            current = current.getSuperclass();
        }
        return methodMap.values().toArray(new Method[0]);
    }

    private void addUniqueMethods(HashMap<String, Method> methodMap, Method[] methods) {
        for (Method method : methods) {
            if (!method.isBridge()) {
                String methodSignature = getMethodSignature(method);
                if (!methodMap.containsKey(methodSignature)) {
                    if (privateMethodAccessible()) {
                        try {
                            method.setAccessible(true);
                        } catch (Exception ignore) {

                        }
                        if (method.isAccessible()) {
                            methodMap.put(methodSignature, method);
                        }
                    }
                }
            }
        }
    }

    private String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (!Void.TYPE.equals(returnType)) {
            sb.append(returnType.getName()).append("#");
        }
        sb.append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i == 0) {
                sb.append(":");
            } else {
                sb.append(",");
            }
            sb.append(parameterTypes[i].getName());
        }
        return sb.toString();
    }

    private void addSetMethod(String propertyName, Method method) {
        if (isValidProperty(propertyName)) {
            setMethods.put(propertyName, new MethodInvoker(method));
            setTypes.put(propertyName, method.getParameterTypes()[0]);
        }
    }

    private void addGetMethods() {
        HashMap<String, List<Method>> conflictMethods = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        //找出所有getter方法
        for (Method method : methods) {
            String methodName = method.getName();
            if ((methodName.startsWith("get") && methodName.length() > 3) || (methodName.startsWith("is") && methodName.length() > 2)) {
                if (method.getParameterCount() == 0) {
                    String propertyName = PropertyNamer.method2Property(methodName);
                    List<Method> list = conflictMethods.computeIfAbsent(propertyName, k -> new ArrayList<>());
                    list.add(method);
                }
            }
        }
        //处理方法冲突
        for (String propertyName : conflictMethods.keySet()) {
            List<Method> list = conflictMethods.get(propertyName);
            Iterator<Method> iterator = list.iterator();
            Method firstMethod = iterator.next();
            if (list.size() == 1) {
                addGetMethod(propertyName, firstMethod);
            } else {
                Method getter = firstMethod;
                Class<?> getterType = firstMethod.getReturnType();
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    Class<?> returnType = method.getReturnType();
                    if (returnType.equals(getterType)) {
                        throw new RuntimeException("repeat returnType getter:" + method.getName());
                    } else if (returnType.isAssignableFrom(getterType)) {
                        continue;
                    } else if (getterType.isAssignableFrom(returnType)) {
                        getter = method;
                        getterType = returnType;
                    } else {
                        throw new RuntimeException("repeat getter:" + method.getName());
                    }
                }
                addGetMethod(propertyName, getter);
            }
        }
    }

    private void addGetMethod(String propertyName, Method method) {
        if (isValidProperty(propertyName)) {
            getMethods.put(propertyName, new MethodInvoker(method));
            getTypes.put(propertyName, method.getReturnType());
        }
    }

    private static boolean isValidProperty(String propertyName) {
        return !(propertyName.startsWith("$") || "serialVersionUID".equals(propertyName) || "class".equals(propertyName));
    }

    private void addDefaultConstructor() {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                if (privateMethodAccessible()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception ignore) {

                    }
                    if (constructor.isAccessible()) {
                        this.defaultConstructor = constructor;
                    }
                }
            }
        }
    }

    private boolean privateMethodAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String findPropertyName(String name) {
        return caseInsensitivePropertyNameMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    public Invoker getGetInvoker(String name) {
        Invoker invoker = getMethods.get(name);
        if (invoker == null) {
            throw new RuntimeException("can not find a get invoker for:" + name);
        }
        return invoker;
    }

    public Invoker getSetInvoker(String name) {
        Invoker invoker = setMethods.get(name);
        if (invoker == null) {
            throw new RuntimeException("can not find a set invoker for:" + name);
        }
        return invoker;
    }

    public String[] getGetterNames() {
        return readablePropertyNames;
    }

    public String[] getSetterNames() {
        return writeablePropertyNames;
    }

    public Class<?> getGetterType(String name) {
        Class<?> type = getTypes.get(name);
        if (type == null) {
            throw new RuntimeException("there is no getter for property named:" + name);
        }
        return type;
    }

    public Class<?> getSetterType(String name) {
        Class<?> type = setTypes.get(name);
        if (type == null) {
            throw new RuntimeException("there is no setter for property named:" + name);
        }
        return type;
    }

    public boolean hasGetter(String name) {
        return getMethods.containsKey(name);
    }

    public boolean hasSetter(String name) {
        return setMethods.containsKey(name);
    }
}
