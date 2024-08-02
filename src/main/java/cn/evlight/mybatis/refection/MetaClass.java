package cn.evlight.mybatis.refection;

import cn.evlight.mybatis.refection.invoker.Invoker;
import cn.evlight.mybatis.refection.property.PropertyTokenizer;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/8/1
 */
public class MetaClass {

    private Reflector reflector;

    public MetaClass(Class<?> clazz) {
        this.reflector = Reflector.forClass(clazz);
    }

    public static MetaClass forClass(Class<?> clazz) {
        return new MetaClass(clazz);
    }

    public MetaClass metaClassForProperty(String propName) {
        Class<?> propType = reflector.getGetterType(propName);
        return MetaClass.forClass(propType);
    }

    public String findPropertyName(String name, boolean useCamel) {
        if (useCamel) {
            name = name.replaceAll("_", "");
        }
        return findPropertyName(name);
    }

    public String findPropertyName(String name) {
        StringBuilder sb = buildPropertyName(name, new StringBuilder());
        return sb.length() == 0 ? null : sb.toString();
    }

    private StringBuilder buildPropertyName(String name, StringBuilder sb) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        String propertyName = reflector.findPropertyName(prop.getName());
        if (propertyName != null) {
            sb.append(propertyName).append(".");
            if (prop.hasNext()) {
                MetaClass metaClass = metaClassForProperty(propertyName);
                metaClass.buildPropertyName(prop.getChild(), sb);
            }
        }
        return sb;
    }

    public Invoker getGetInvoker(String name) {
        return reflector.getGetInvoker(name);
    }

    public Invoker getSetInvoker(String name) {
        return reflector.getSetInvoker(name);
    }

    public String[] getGetterNames() {
        return reflector.getGetterNames();
    }

    public String[] getSetterNames() {
        return reflector.getSetterNames();
    }

    public Class<?> getGetterType(String name) {
        return reflector.getGetterType(name);
    }

    public Class<?> getSetterType(String name) {
        return reflector.getSetterType(name);
    }

    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (reflector.hasGetter(prop.getName())) {
                MetaClass metaClass = metaClassForProperty(prop.getName());
                return metaClass.hasGetter(prop.getChild());
            } else {
                return false;
            }
        } else {
            return reflector.hasGetter(prop.getName());
        }
    }

    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (reflector.hasSetter(prop.getName())) {
                MetaClass metaClass = metaClassForProperty(prop.getName());
                return metaClass.hasSetter(prop.getChild());
            } else {
                return false;
            }
        } else {
            return reflector.hasSetter(prop.getName());
        }
    }
}
