package com.minis.context;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.BeansException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * 1.加载xml文件
 * 2. 把bean读出来，放入beanFactory
 * 3. 从factory中获取bean
 *
 * @Date：2024/3/12 19:02
 * @Description
 */
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {

    SimpleBeanFactory simpleBeanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        simpleBeanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(simpleBeanFactory);
        reader.loadBeanDefinitions(resource);
    }


    public Object getBean(String beanName) throws Exception {
        return simpleBeanFactory.getBean(beanName);
    }


    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        simpleBeanFactory.registerBean(beanDefinition.getId(), beanDefinition);
    }

    public boolean containsBean(String name) {
        return simpleBeanFactory.containsBean(name);
    }

    public void registerBean(String beanName, Object obj) {
        simpleBeanFactory.registerBean(beanName, obj);
    }


    public void publishEvent(ApplicationEvent event) {
    }

    public boolean isSingleton(String name) {
        return false;
    }

    public boolean isPrototype(String name) {
        return false;
    }

    public Class getType(String name) {
        return null;
    }
}
