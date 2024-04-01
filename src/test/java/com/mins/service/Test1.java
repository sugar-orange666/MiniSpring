package com.mins.service;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.AService;

/**
 * @Date：2024/3/16 09:46
 * @Description
 */
public class Test1 {


    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml", true);
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();
    }
}
