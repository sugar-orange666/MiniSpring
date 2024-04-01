package com.minis.beans;

/**
 * @Date：2024/3/16 13:10
 * @Description
 */
public class PropertyValue {
    private final String type;
    private final String name;

    private final Object value;

    private final boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public boolean getIsRef() {
        return isRef;
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
