package com.minis.test;

/**
 * @Date：2024/3/16 09:44
 * @Description
 */
public class AServiceImpl implements AService {


    public AServiceImpl() {
    }

    private String property1;
    private String property2;

    private String name;
    private int level;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    @Override
    public void sayHello() {
        System.out.println(this.property1 + "," + this.property2);
    }
}
