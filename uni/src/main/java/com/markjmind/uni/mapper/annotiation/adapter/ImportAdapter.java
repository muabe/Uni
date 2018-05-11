package com.markjmind.uni.mapper.annotiation.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.mapper.FieldInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Import;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Muabe on 2018-05-11.
 */

public class ImportAdapter extends FieldInjectAdapter<Import> {
    ArrayList<UniLayout> importor;
    ViewGroup finder;

    public ImportAdapter(ViewGroup finder, ArrayList<UniLayout> importor){
        this.finder = finder;
        this.importor = importor;
    }

    @Override
    public Class<Import> getAnnotationType() {
        return Import.class;
    }

    @Override
    public void injectField(Import annotation, Field field, Object targetObject) {
        try {
            Constructor con = field.getType().getConstructor(Context.class);
            Object obj = con.newInstance(finder.getContext());
            if(obj instanceof UniLayout){
                UniLayout importLayout = ((UniLayout)obj);
                setField(field, importLayout);
                int parentsId = annotation.value();
                int mode = annotation.mode();
                ViewGroup parents = ((ViewGroup)finder.findViewById(parentsId));
                if(mode == Import.replace){
                    parents.removeAllViews();
                }
                parents.addView(importLayout);
                importor.add(importLayout);
            }else{
                throw new UinMapperException(ErrorMessage.Runtime.uniLayoutClassCast(targetObject.getClass(), field.getName()));
            }
        } catch (NoSuchMethodException e) {
            throw new UinMapperException(ErrorMessage.Runtime.uniLayoutNoSuchMethod(targetObject.getClass(), field.getName()), e);
        } catch (IllegalAccessException e) {
            throw new UinMapperException(ErrorMessage.Runtime.fieldIllegalAccess(targetObject.getClass(), field.getName()), e);
        } catch (InstantiationException e) {
            throw new UinMapperException(ErrorMessage.Runtime.fieldInstantiation(targetObject.getClass(), field.getName()), e);
        } catch (InvocationTargetException e) {
            throw new UinMapperException(ErrorMessage.Runtime.invocationTarget(targetObject.getClass(), field.getName()), e);
        }
    }


}