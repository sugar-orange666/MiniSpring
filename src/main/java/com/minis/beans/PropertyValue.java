package com.minis.beans;

/**
 * @Date：2024/3/16 13:10
 * @Description
 */
public class PropertyValue {
    private final String type;
    private final String name;

    private final Object value;


    public PropertyValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

}
