package com.study.eventbusdemo.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hcw on 2019/4/9.
 * Copyright©hcw.All rights reserved.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    //默认线程模式
    ThreadMode threadMode()  default ThreadMode.MAIN;
}
