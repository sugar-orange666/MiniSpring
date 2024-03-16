package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

/**
 * @Date：2024/3/16 10:08
 * @Description
 */
public class XmlBeanDefinitionReader {

    BeanFactory beanFactory;

    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            beanFactory.registerBean(beanId, beanDefinition);
        }

    }
}
