package com.minis.beans.factory.config;

import com.minis.beans.BeanFactory;
import com.minis.beans.BeansException;

public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}