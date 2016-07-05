/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
public class UniMapper extends Mapper{
    protected HashMap<Class<?>, InjectAdapter<? extends Annotation>> onInitMap = new HashMap<>();
    protected HashMap<Class<?>, InjectAdapter<? extends Annotation>> onStartMap = new HashMap<>();

    public UniMapper(){

    }

    public UniMapper(View finder){
        super(finder);
    }

    public UniMapper(Activity finder){
        super(finder);
    }

    public UniMapper(Dialog finder){
        super(finder);
    }

    public UniMapper(View finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    public UniMapper(Activity finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    public UniMapper(Dialog finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    private void initAdapter(){
        addSubscriptionOnStart(new GetViewAdapter());
        addSubscriptionOnStart(new OnClickAdapter());
    }

    public Context getContext(){
        return context;
    }

    public Object getTarget(){
        return targetObject;
    }

    public void reset(View finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }

    public void reset(Activity finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }

    public void reset(Dialog finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }

    public void injectSubscriptionOnInit(){
        ArrayList<InjectListener<? extends Annotation, Class>> classList = new ArrayList<>();
        InjectAdapter<? extends Annotation>[] values = new InjectAdapter[onInitMap.size()];
        onInitMap.values().toArray(values);
        inject(values);
    }

    public void injectSubscriptionOnStart(){
        ArrayList<InjectListener<? extends Annotation, Class>> classList = new ArrayList<>();
        InjectAdapter<? extends Annotation>[] values = new InjectAdapter[onStartMap.size()];
        onStartMap.values().toArray(values);
        inject(values);
    }

    public void addSubscriptionOnInit(InjectAdapter<? extends Annotation> adapter){
        onInitMap.put(adapter.getClass(), adapter);
    }

    public void addSubscriptionOnStart(InjectAdapter<? extends Annotation> adapter){
        onStartMap.put(adapter.getClass(), adapter);
    }

    public <T extends Annotation>InjectAdapter<T> getSubscriptionOnInit(Class<T> adapterClass){
        return (InjectAdapter<T>)onInitMap.get(adapterClass);
    }

    public <T extends Annotation>InjectAdapter<T> getSubscriptionOnStart(Class<T> adapterClass){
        return (InjectAdapter<T>)onStartMap.get(adapterClass);
    }

    public void removeSubscriptionOnInit(Class<?> adapterClass){
        onInitMap.remove(adapterClass);
    }

    public void removeSubscriptionOnStart(Class<?> adapterClass){
        onStartMap.remove(adapterClass);
    }
}
