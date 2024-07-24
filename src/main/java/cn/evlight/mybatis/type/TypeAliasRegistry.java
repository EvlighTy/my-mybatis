package cn.evlight.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/23
 */
public class TypeAliasRegistry {

    private final Map<String, Class<?>> typeAlias = new HashMap<>();

    {
        register("string", String.class);
        register("byte", Byte.class);
        register("long", Long.class);
        register("short", Short.class);
        register("int", Integer.class);
        register("integer", Integer.class);
        register("double", Double.class);
        register("float", Float.class);
        register("boolean", Boolean.class);
        register("char", CharSequence.class);
    }

    public void register(String alias, Class<?> clazz) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        typeAlias.put(key, clazz);
    }

    public Class<?> resolve(String alias) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        return typeAlias.get(key);
    }

}
