/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper;

import android.content.Context;
import android.view.View;

import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-11
 */
public abstract class MapperAdapter<T extends Annotation, E>{
    private Mapper mapper;
    private Class<E> type;

    public Class<E> getType(){
        return type;
    }

    public MapperAdapter(Class<E> type){
        this.type = type;
    }

    public abstract Class<T> getAnnotationClass();
    public abstract void inject(T annotation, E element, Object targetObject);

    void setMapper(Mapper mapper){
        this.mapper = mapper;
    }

    public Context getContext(){
        return mapper.context;
    }

    public Object getObject(){
        return mapper.targetObject;
    }

    public void addViewCache(int id, View view){
        mapper.viewHash.put(id, view);
    }

    public View findViewById(int id){
        if(mapper.viewHash.containsKey(id)){
            return (View)mapper.viewHash.get(id);
        }
        return mapper.finder.findViewById(id);
    }

    public int getIdentifier(String name, String defType){
        int resID = mapper.context.getResources().getIdentifier(name, defType, mapper.context.getPackageName());
        if(resID!=-1){
            return resID;
        }else{
            throw new UinMapperException(ErrorMessage.Runtime.fieldNoSuch(mapper.targetClass, name), null);
        }
    }

    public void setField(Field field, Object value){
        try {
            field.setAccessible(true);
            field.set(getObject(), value);
        }catch (IllegalAccessException e) {
            throw new UinMapperException(ErrorMessage.Runtime.injectFieldIllegalAccess(getObject().getClass(), field.getName()),e);
        }
    }
}
