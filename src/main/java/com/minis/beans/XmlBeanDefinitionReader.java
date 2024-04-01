package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
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

            //处理属性
            List<Element> propertiesElements = element.elements("property");
            PropertyValues pvs = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertiesElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                pvs.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(pvs);
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);


            simpleBeanFactory.registerBeanDefinition(beanId,beanDefinition);
        }

    }
}
