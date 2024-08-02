package cn.evlight.mybatis.refection.property;

import java.util.Locale;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public class PropertyNamer {

    public static String method2Property(String methodName) {
        String propertyName;
        if (methodName.startsWith("is")) {
            propertyName = methodName.substring(2);
        } else if (methodName.startsWith("get") || methodName.startsWith("set")) {
            propertyName = methodName.substring(3);
        } else {
            throw new RuntimeException("unable to parse method name to property name");
        }
        if (propertyName.length() == 1 || !Character.isUpperCase(propertyName.charAt(1))) {
            propertyName = propertyName.substring(0, 1).toLowerCase(Locale.ENGLISH) + propertyName.substring(1);
        }
        return propertyName;
    }

    public static boolean isProperty(String methodName) {
        return isGetter(methodName) || isSetter(methodName);
    }

    private static boolean isGetter(String methodName) {
        return methodName.startsWith("get") || methodName.startsWith("is");
    }

    private static boolean isSetter(String methodName) {
        return methodName.startsWith("set");
    }
}
