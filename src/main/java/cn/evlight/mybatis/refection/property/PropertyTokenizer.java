package cn.evlight.mybatis.refection.property;

import java.util.Iterator;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/29
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {

    private String name;
    private String nameAndIndex;
    private String index;
    private String child;

    public PropertyTokenizer(String fullName) {
        int split = fullName.indexOf(".");
        if (split != -1) {
            name = fullName.substring(0, split);
            child = fullName.substring(split + 1);
        } else {
            name = fullName;
        }
        nameAndIndex = name;
        split = name.indexOf("[");
        if (split != -1) {
            index = name.substring(split + 1, name.length() - 1);
            name = name.substring(0, split);
        }
    }

    public String getName() {
        return name;
    }

    public String getNameAndIndex() {
        return nameAndIndex;
    }

    public String getIndex() {
        return index;
    }

    public String getChild() {
        return child;
    }

    @Override
    public boolean hasNext() {
        return child != null;
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(child);
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new RuntimeException("unsupported method");
    }
}
