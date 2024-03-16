package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.List;

/**
 * @Date：2024/3/16 10:08
 * @Description
 */
public class XmlBeanDefinitionReader {

    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }


    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            simpleBeanFactory.registerBeanDefinition(beanId, beanDefinition);
            //处理属性
            List<Element> propertiesElements = element.elements("property");
            PropertyValues pvs = new PropertyValues();
            for (Element e : propertiesElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                pvs.addPropertyValue(pType, pName, pValue);
            }
            beanDefinition.setPropertyValues(pvs);
            //处理构造方法
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues avs = new ArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                avs.addArgumentValue(new ArgumentValue(aValue, aType, aName));
            }
            beanDefinition.setConstructorArgumentValues(avs);
        }

    }
}
