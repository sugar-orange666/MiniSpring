package com.minis.beans.factory.xml;

import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2024/3/16 10:08
 * @Description
 */
public class XmlBeanDefinitionReader {


    AbstractBeanFactory beanFactory;
//    SimpleBeanFactory simpleBeanFactory;

//    BeanFactory simpleBeanFactory;


//    DefaultSingletonBeanRegistry simpleBeanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory  beanFactory) {
        this.beanFactory = beanFactory;
    }


//    public XmlBeanDefinitionReader(AutowireCapableBeanFactory autowireCapableBeanFactory)
//    {
//        this.simpleBeanFactory=autowireCapableBeanFactory;
//    }




    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethodName=element.attributeValue("init-method");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
//            beanFactory.registerBeanDefinition(beanId, beanDefinition);
            //处理构造方法
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues avs = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                avs.addArgumentValue(new ConstructorArgumentValue(aValue, aType, aName));
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

            beanDefinition.setInitMethodName(initMethodName);
            beanFactory.registerBeanDefinition(beanId,beanDefinition);
        }

    }
}
