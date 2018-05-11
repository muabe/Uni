package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.FieldInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Aop;

import java.lang.reflect.Field;

/**
 * Created by Muabe on 2018-05-11.
 */

public class AopAdapter extends FieldInjectAdapter<Aop> {

    @Override
    public Class<Aop> getAnnotationType() {
        return Aop.class;
    }

    @Override
    public void injectField(Aop annotation, Field field, Object targetObject) {
        try {
            setField(field, annotation.value().newInstance());
        } catch (InstantiationException e) {
            new UinMapperException(ErrorMessage.Runtime.defalutConstructInstantiation(targetObject.getClass(), field.getName()), e);
        } catch (IllegalAccessException e) {
            new UinMapperException(ErrorMessage.Runtime.injectFieldIllegalAccess(targetObject.getClass(), field.getName()), e);
        }
    }
}