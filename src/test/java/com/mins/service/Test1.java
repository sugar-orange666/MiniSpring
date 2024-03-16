package com.mins.service;

import com.minis.ClassPathXmlApplicationContext;
import com.minis.test.AService;

/**
 * @Date：2024/3/16 09:46
 * @Description
 */
public class Test1 {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) ctx.getObjects("aservice");
        aService.sayHello();
    }
}
