/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import android.util.Log;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.FieldInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Param;

import java.lang.reflect.Field;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
public class ParamAdapter extends FieldInjectAdapter<Param> {
    private Store<?> param;

    public ParamAdapter(Store param){
        this.param = param;
    }

    @Override
    public Class<Param> getAnnotationType() {
        return Param.class;
    }

    @Override
    public void injectField(Param annotation, Field field, Object targetObject) {
        String key = annotation.value();
        if(key.trim().length()==0){
            key = field.getName();
        }
        if(param.containsKey(key)) {
            Object value = param.get(key);
            try {
                if(value!=null) {
                    setField(field, value);
                }
            } catch (Exception e) {
                Log.i("dsd", field.getType().getName());
                if (value == null && ("int".equals(field.getType().getName())
                        || "long".equals(field.getType().getName())
                        || "float".equals(field.getType().getName())
                        || "double".equals(field.getType().getName())
                )) {
                    throw new UinMapperException(ErrorMessage.Runtime.injectParamNull(getObject().getClass(), field), e);
                } else {
                    throw new UinMapperException(ErrorMessage.Runtime.injectParam(getObject().getClass(), field.getName()), e);
                }
            }
        }

    }


}
