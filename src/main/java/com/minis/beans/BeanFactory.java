package com.minis.beans;

/**
 * @Date：2024/3/16 10:08
 * @Description
 */
public interface BeanFactory {
    Object getBean(String beanName) throws Exception;

    void registerBean(String beanName, Object obj);


    boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class getType(String name);
}
