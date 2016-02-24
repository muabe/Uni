/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.FieldAdapter;
import com.markjmind.uni.mapper.annotiation.Param;

import java.lang.reflect.Field;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
public class ParamAdapter extends FieldAdapter<Param> {
    private Store<?> param;

    public ParamAdapter(Store param){
        this.param = param;
    }

    @Override
    public Class<Param> getAnnotationClass() {
        return Param.class;
    }

    @Override
    public void inject(Param annotation, Field field, Object targetObject) {
        String key = annotation.value();
        if(key.trim().length()==0){
            key = field.getName();
        }
        Object value = param.get(key);
        setField(field, value);
    }


}
