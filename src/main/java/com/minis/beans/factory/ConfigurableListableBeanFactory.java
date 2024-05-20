package com.minis.beans.factory;

import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.config.ConfigurableBeanFactory;

/**
 * ListableBeanFactory：提供按名称和类型查找 Bean 的功能。
 * AutowireCapableBeanFactory：提供自动装配 Bean 的能力。
 * ConfigurableBeanFactory：提供配置 Bean 工厂的能力。
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory
{

}