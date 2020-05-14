package com.markjmind.uni.mapper.annotiation.adapter;

import android.view.LayoutInflater;

import androidx.databinding.ViewDataBinding;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.mapper.ClassInjectAdapter;
import com.markjmind.uni.mapper.annotiation.AutoBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class AutoBinderAdapter extends ClassInjectAdapter<AutoBinder> {
    private UniLayout uniLayout;
    private LayoutInflater inflater;

    public AutoBinderAdapter(UniLayout uniLayout, LayoutInflater inflater){
        this.uniLayout = uniLayout;
        this.inflater = inflater;
    }


    @Override
    public Class<AutoBinder> getAnnotationType() {
        return AutoBinder.class;
    }

    @Override
    public void injectClass(AutoBinder annotation, Class targetClass, Object targetObject) {
        try {
            Class<?> genericClass = (Class<?>)((ParameterizedType)targetObject.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Method method = genericClass.getMethod("inflate", LayoutInflater.class);
            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, inflater);
            uniLayout.setView(vb.getRoot());
            uniLayout.setViewDataBinding(vb);
            targetClass.getField("binder").set(targetObject, uniLayout.getViewDataBinding());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException("Binder Annotation Exception",e);
        }
    }

}