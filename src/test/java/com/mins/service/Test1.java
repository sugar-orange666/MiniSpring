package com.mins.service;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.AService;
import com.minis.test.BaseBaseService;

import javax.annotation.Resource;

/**
 * @Date：2024/3/16 09:46
 * @Description
 */
public class Test1 {


    @Autowired
    private static BaseBaseService bbs;


    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml", true);
//        AService aService = (AService) ctx.getBean("aservice");
//        aService.sayHello();

        bbs.sayHello();
    }

//    public static void main(String[] args) {
//        System.out.println("Base Service says Hello");
//        bbs.sayHello();
//    }




}
