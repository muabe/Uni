/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.MethodInjectAdapter;
import com.markjmind.uni.mapper.annotiation.OnCheckedChange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-11
 */
public class OnCheckedChangeAdapter extends MethodInjectAdapter<OnCheckedChange> {

    @Override
    public Class<OnCheckedChange> getAnnotationType() {
        return OnCheckedChange.class;
    }

    @Override
    public void injectMethod(OnCheckedChange annotation, Method method, Object targetObject) {
        int[] list = annotation.ids();
        if(list.length==0){
            int id = annotation.value();
            if(id==-1) {
                id = getIdentifier(method.getName(), "id");
            }
            View view = setListener(getObject(), id, method);
            addViewCache(id, view);
        }else{
            for(int id:list){
                View view = setListener(getObject(), id, method);
                addViewCache(id, view);
            }
        }
    }

    private View setListener(final Object obj, Integer id, final Method method){
        final View view = findViewById(id);
        if(view==null){ // view가 없을경우
            throw new UinMapperException(ErrorMessage.Runtime.injectMethod(obj.getClass(), method.getName()),null);
        }

        try {
            String viewClass = view.getClass().toString();
            if(viewClass.equals(RadioGroup.class.toString())){
                Method listenerMethod = view.getClass().getMethod("setOnCheckedChangeListener", RadioGroup.OnCheckedChangeListener.class);
                listenerMethod.invoke(view, new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        try {
                            method.invoke(obj, group, checkedId);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else{
                Method listenerMethod = view.getClass().getMethod("setOnCheckedChangeListener", android.widget.CompoundButton.OnCheckedChangeListener.class);
                listenerMethod.invoke(view, new android.widget.CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            method.invoke(obj, buttonView, isChecked);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return view;
    }
}
