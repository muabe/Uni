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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
public class UniMapper extends Mapper{
    protected LinkedHashMap<Class<?>, InjectAdapter<? extends Annotation>> onInitMap = new LinkedHashMap<>();
    protected LinkedHashMap<Class<?>, InjectAdapter<? extends Annotation>> onStartMap = new LinkedHashMap<>();

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

    public UniMapper(BottomSheetDialog finder){
        super(finder);
    }

    public UniMapper(View finder, Object targetObject){
        super(finder, targetObject);
    }

    public UniMapper(Activity finder, Object targetObject){
        super(finder, targetObject);
    }

    public UniMapper(Dialog finder, Object targetObject){
        super(finder, targetObject);
    }

    public UniMapper(BottomSheetDialog finder, Object targetObject){
        super(finder, targetObject);
    }

    public Context getContext(){
        return context;
    }

    public Object getTarget(){
        return targetObject;
    }

    public void reset(View finder, Object targetObject){
        super.reset(finder, targetObject);
    }

    public void reset(Activity finder, Object targetObject){
        super.reset(finder, targetObject);
    }

    public void reset(Dialog finder, Object targetObject){
        super.reset(finder, targetObject);
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

    public <T extends InjectAdapter>T getSubscriptionOnStart(Class<T> adapterClass){
        return (T)onStartMap.get(adapterClass);
    }

    public void removeSubscriptionOnInit(Class<?> adapterClass){
        onInitMap.remove(adapterClass);
    }

    public void removeSubscriptionOnStart(Class<?> adapterClass){
        onStartMap.remove(adapterClass);
    }
}
