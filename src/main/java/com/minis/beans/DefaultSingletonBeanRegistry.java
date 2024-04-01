package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date：2024/3/16 12:29
 * @Description
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected List<String> beanNames = new ArrayList<>();


    //ConcurrentHashMap 线程安全
    protected Map<String, Object> singletons = new ConcurrentHashMap<>();

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletons) {
            singletons.put(beanName, singletonObject);
            beanNames.add(beanName);
        }

    }

    @Override
    public Object getSingleton(String beanName) {
        return singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return  this.beanNames.toArray(new String[0]);
    }
}
