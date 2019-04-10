package com.study.eventbusdemo.core;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hcw on 2019/4/9.
 * Copyright©hcw.All rights reserved.
 */

public class EventBus {

    private static volatile  EventBus instance;

    //定义一个容器，保存所有的方法
    private Map<Object,List<SubscribeMethod>> cacheMap;


    private Handler mHandler;

    private ExecutorService executorService;


    private EventBus(){
        cacheMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
    }


    public static EventBus getDefault(){
        if (null == instance){
            synchronized (EventBus.class){
                if (instance == null){
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }


    public void register(Object object){
        List<SubscribeMethod> list = cacheMap.get(object);

        if (list == null){
            list = findSubscribleMethod(object);
            cacheMap.put(object,list);
        }

    }

    //不存在就去找，并且添加到缓存,注意找父类
    private List<SubscribeMethod> findSubscribleMethod(Object object) {
        List<SubscribeMethod> list = new ArrayList<>();

        Class<?> clazz = object.getClass();


        //循环查找父类
        while (clazz != null){

            //判断是否是系统类
            String name = clazz.getName();

            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")){
                break;
            }

            //反射，得到 注册的 Activity(等) 的方法
            Method[] methods = clazz.getMethods();


            //通过注解得到目标方法
            for (Method method:methods){


                Subscribe subscribe = method.getAnnotation(Subscribe.class);

                if (subscribe == null){
                    continue;
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1){
                    throw  new RuntimeException("EventBus must receive one paramaters" );
                }

                //线程模式
                ThreadMode threadMode = subscribe.threadMode();

                SubscribeMethod subscribeMethod = new SubscribeMethod(method,threadMode,types[0]);

                list.add(subscribeMethod);

            }

            clazz  = clazz.getSuperclass();


        }


        return list;


    }



    public void post(final Object type){
        Set<Object> set = cacheMap.keySet();

        Iterator<Object> iterator = set.iterator();

        while (iterator.hasNext()){
            final Object object = iterator.next();

            List<SubscribeMethod> list = cacheMap.get(object);

            for (final SubscribeMethod subscribeMethod:list){
                //两个类对比一下,也就是参数对比
                if (subscribeMethod.getType().isAssignableFrom(type.getClass())) {

                    switch (subscribeMethod.getThreadMode()){
                        case MAIN:

                            //主线程向主线程传递
                            if (Looper.myLooper() == Looper.getMainLooper()){
                                invoke(subscribeMethod,object,type);
                            }else {
                                //子线程向主线程传递
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribeMethod,object,true);
                                    }
                                });
                            }
                            break;
                        case BACKROUND:
                            if (Looper.myLooper() == Looper.getMainLooper()){
                                //主 -> 子
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribeMethod,object,type);
                                    }
                                });

                            }else {
                                //子 —> 子 不需要线程切换
                                invoke(subscribeMethod,object,type);

                            }
                            break;
                            default:break;
                    }

                     }
                }

            }
        }

    private void invoke(SubscribeMethod subscribeMethod, Object object, Object type) {
        Method method = subscribeMethod.getMethod();
        try {
            method.invoke(object,type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}



