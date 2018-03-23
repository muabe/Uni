/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import android.view.View;

import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.FieldInjectAdapter;
import com.markjmind.uni.mapper.annotiation.GetView;

import java.lang.reflect.Field;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-11
 */
public class GetViewAdapter extends FieldInjectAdapter<GetView> {

    @Override
    public Class<GetView> getAnnotationType() {
        return GetView.class;
    }

    @Override
    public void injectField(GetView annotation, Field field, Object targetObject) {
        int id = annotation.value();
        if(id==-1){
            id = getIdentifier(field.getName(), "id");
        }
        setView(field, id, annotation.nullable());
    }

    private View setView(Field field, int id, boolean isNull){
        View v = findViewById(id);
        if(v==null){
            if(isNull){
                return null;
            }
            throw new UinMapperException(ErrorMessage.Runtime.injectField(getObject().getClass(), field.getName()));
        }
        setField(field, v);
        return v;
    }
}
