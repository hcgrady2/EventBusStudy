package com.study.eventbusdemo;

/**
 * Created by hcw on 2019/4/9.
 * Copyright©hcw.All rights reserved.
 */

public class EventBean {

    private String one;
    private String two;

    public EventBean(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return one + "two:" + two;
    }
}
