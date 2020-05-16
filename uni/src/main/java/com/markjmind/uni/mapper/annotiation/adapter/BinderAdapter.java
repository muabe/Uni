package com.markjmind.uni.mapper.annotiation.adapter;

import android.view.LayoutInflater;

import androidx.databinding.ViewDataBinding;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.mapper.FieldInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Binder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BinderAdapter extends FieldInjectAdapter<Binder> {
    private UniLayout uniLayout;
    private LayoutInflater inflater;

    public BinderAdapter(UniLayout uniLayout, LayoutInflater inflater){
        this.uniLayout = uniLayout;
        this.inflater = inflater;
    }

    @Override
    public Class<Binder> getAnnotationType() {
        return Binder.class;
    }

    @Override
    public void injectField(Binder annotation, Field field, Object targetObject) {
        try {
            Method method = field.getType().getMethod("inflate", LayoutInflater.class);//, ViewGroup.class, boolean.class);
            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, inflater);
            uniLayout.setView(vb.getRoot());
            uniLayout.setViewDataBinding(vb);
            field.setAccessible(true);
            field.set(targetObject, uniLayout.getViewDataBinding());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Binder Annotation Exception",e);
        }
    }
}