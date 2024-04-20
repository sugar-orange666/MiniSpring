package com.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2024/3/16 13:12
 * @Description
 */
public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        constructorArgumentValueList.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return constructorArgumentValueList.get(index);
    }

    public int getArgumentCount() {
        return constructorArgumentValueList.size();
    }

    public boolean isEmpty() {
        return constructorArgumentValueList.isEmpty();
    }
}
