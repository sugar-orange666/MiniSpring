package com.minis.beans.factory.annotation;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @Date：2024/4/1 17:11
 * @Description
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private AutowireCapableBeanFactory beanFactory;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        
        Object result=bean;
        Class<?> clazz = result.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields!=null)
        {
            for (Field field : fields) {
                //对注解做处理
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired)
                {
                    String fieldName = field.getName();
                    try {
                        Object autowiredObj = getBeanFactory().getBean(fieldName);
                        field.setAccessible(true);
                        field.set(bean,autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        


        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {

    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
