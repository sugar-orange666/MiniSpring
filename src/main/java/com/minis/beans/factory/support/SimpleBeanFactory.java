package com.minis.beans.factory.support;

import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date：2024/3/16 10:13
 * @Description
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {


    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //二级缓存，存储半成品对象
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    //所有的bean都创建一遍

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //getBean容器的核心方法
    @Override
    public Object getBean(String beanName) throws Exception {
        //先尝试获取bean

        Object singleton = getSingleton(beanName);
        if (singleton == null) {
            singleton = earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                singleton = createBean(beanDefinition);
                registerSingleton(beanName, singleton);
            }
        }

        if (singleton == null) {
            throw new BeansException("bean is null.");
        }
        return singleton;
    }

    public boolean containsBean(String name) {
        return containsSingleton(name);
    }


    public void registerBean(String name, Object obj) {
        registerSingleton(name, obj);
    }


    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
        beanNames.add(name);
        try {
            if (beanDefinition.isLazyInit()) {
                getBean(name);
            }
        } catch (BeansException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanNames.remove(name);
        this.removeBeanDefinition(name);
    }

    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return beanDefinitionMap.get(name).getClass();
    }


    private Object createBean(BeanDefinition beanDefinition) throws BeansException {
        Class<?> clz = null;

        Object obj = doCreateBean(beanDefinition);
        earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        handleProperties(beanDefinition, clz, obj);
        return obj;

    }


    private Object doCreateBean(BeanDefinition beanDefinition) throws BeansException {
        Object obj = null;
        Class<?> clz = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if (constructorArgumentValue.getType().equals("String") || constructorArgumentValue.getType().equals("java.lang.String")) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if (constructorArgumentValue.getType().equals("Integer") || constructorArgumentValue.getType().equals("java.lang.Integer")) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if (constructorArgumentValue.getType().equals("int")) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else {
                        //默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }

                }


                //按照特定的构造器创建实例
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);

            } else {
                //如果构造为空，默认使用空构造方法
                obj = clz.newInstance();
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException(e.getMessage());
        }
        return obj;

    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {

        try {
            PropertyValues propertyValues = bd.getPropertyValues();
            if (!propertyValues.isEmpty()) {
                for (int i = 0; i < propertyValues.size(); i++) {
                    PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                    String pType = propertyValue.getType();
                    String pName = propertyValue.getName();
                    Object pValue = propertyValue.getValue();
                    boolean isRef = propertyValue.getIsRef();
                    Class[] paramTypes = new Class[1];
                    Object[] paramValues = new Object[1];

                    if (!isRef) {
                        if (pType.equals("String") || pType.equals("java.lang.String")) {
                            paramTypes[0] = String.class;
                        } else if (pType.equals("Integer") || pType.equals("java.lang.Integer")) {
                            paramTypes[0] = Integer.class;
                        } else if (pType.equals("int")) {
                            paramTypes[0] = int.class;
                        } else {
                            paramTypes[0] = String.class;
                        }

                        paramValues[0] = pValue;


                    } else {
                        paramTypes[0] = Class.forName(pType);
                        paramValues[0] = getBean((String) pValue);
                    }

                    //按照setXxxx规范查找setter方法，调用setter方法设置属性
                    String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                    Method method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);

                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
