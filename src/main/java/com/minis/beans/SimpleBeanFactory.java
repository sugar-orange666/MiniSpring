package com.minis.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String> beanNames = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //getBean容器的核心方法
    @Override
    public Object getBean(String beanName) throws Exception {
        //先尝试获取bean

        Object singleton = getSingleton(beanName);
        if (singleton == null) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition == null) {
                throw new BeansException("No bean.");
            }
            try {
//                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
                singleton = createBean(beanDefinition);
                registerSingleton(beanName, singleton);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        return singleton;
    }

    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    @Override
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
        Object obj = null;
        Constructor<?> con = null;


        try {
            clz = Class.forName(beanDefinition.getClassName());
            ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    if (argumentValue.getType().equals("String") || argumentValue.getType().equals("java.lang.String")) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if (argumentValue.getType().equals("Integer") || argumentValue.getType().equals("java.lang.Integer")) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if (argumentValue.getType().equals("int")) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else {
                        //默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }

                }


                //按照特定的构造器创建实例
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);

            } else {
                //如果构造为空，默认使用空构造方法
                obj = clz.newInstance();
            }

            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            if (!propertyValues.isEmpty()) {
                for (int i = 0; i < propertyValues.size(); i++) {
                    PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);

                    String pType = propertyValue.getType();
                    String pName = propertyValue.getName();
                    Object pValue = propertyValue.getValue();
                    Class[] paramTypes = new Class[1];

                    if (pType.equals("String") || pType.equals("java.lang.String")) {
                        paramTypes[0] = String.class;
                    } else if (pType.equals("Integer") || pType.equals("java.lang.Integer")) {
                        paramTypes[0] = Integer.class;
                    } else if (pType.equals("int")) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }

                    Object[] paramValues = new Object[1];
                    paramValues[0] = pValue;

                    //按照setXxxx规范查找setter方法，调用setter方法设置属性
                    String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                    Method method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);

                }
            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException(e.getMessage());
        }
        return obj;

    }
}
