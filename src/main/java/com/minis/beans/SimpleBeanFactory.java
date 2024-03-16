package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date：2024/3/16 10:13
 * @Description
 */
public class SimpleBeanFactory implements BeanFactory {


    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    //getBean容器的核心方法
    @Override
    public Object getBean(String beanName) {
        //先尝试获取bean
        Object singleton = singletons.get(beanName);
        //如果获取不到
        try {
            if (singleton == null) {
                //bean不存在
                int i = beanNames.indexOf(beanName);
                if (i == -1) {
                    return new BeansException();
                    //存在，第一次构建在map中，感觉这里有点延迟加载的意思
                } else {
                    BeanDefinition beanDefinition = beanDefinitions.get(i);
                    singleton = Class.forName(beanDefinition.getClassName()).newInstance();
                    //注册bean实例
                    singletons.put(beanDefinition.getId(), singleton);
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitions.add(beanDefinition);
        beanNames.add(beanDefinition.getId());

    }
}
