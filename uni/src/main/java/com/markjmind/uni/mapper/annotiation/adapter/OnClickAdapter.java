/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import android.view.View;

import com.markjmind.uni.common.OnClickListenerReceiver;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.MethodInjectAdapter;
import com.markjmind.uni.mapper.annotiation.OnClick;

import java.lang.reflect.Method;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-11
 */
public class OnClickAdapter extends MethodInjectAdapter<OnClick> {

    @Override
    public Class<OnClick> getAnnotationType() {
        return OnClick.class;
    }

    @Override
    public void injectMethod(OnClick annotation, Method method, Object targetObject) {
        int[] list = annotation.ids();
        if(list.length==0){
            int id = annotation.value();
            if(id==-1) {
                id = getIdentifier(method.getName(), "id");
            }
            View view = setOnClickListener(getObject(), id, method);
            addViewCache(id, view);
        }else{
            for(int id:list){
                View view = setOnClickListener(getObject(), id, method);
                addViewCache(id, view);
            }
        }
    }

    private View setOnClickListener(Object obj, Integer id, Method method){
        View view = findViewById(id);
        if(view==null){ // view가 없을경우
            throw new UinMapperException(ErrorMessage.Runtime.injectMethod(obj.getClass(), method.getName()),null);
        }
        OnClickListenerReceiver oclReceiver = new OnClickListenerReceiver(obj);
        oclReceiver.setOnClickListener(view, method);

        //TODO 파이미터 설정
//		obj.onClickParams.put(view.hashCode(), oclReceiver);
        return view;
    }
}
