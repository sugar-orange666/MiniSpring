package com.minis.beans;

/**
 * @Date：2024/3/16 10:08
 * @Description
 */
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    void registerBean(String beanName, Object obj);
}
