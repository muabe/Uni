package com.markjmind.uni.mapper.annotiation;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.mapper.ClassInjectAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-04
 */
public class LayoutInjector extends ClassInjectAdapter<Layout> {
    LayoutInflater inflater;
    UniLayout uniLayout;
    ViewGroup container;

    public LayoutInjector(LayoutInflater inflater, UniLayout uniLayout, ViewGroup container){
        this.inflater = inflater;
        this.uniLayout = uniLayout;
        this.container = container;
    }

    @Override
    public Class<Layout> getAnnotationType() {
        return Layout.class;
    }

    @Override
    public void injectClass(Layout annotation, Class targetClass, Object targetObject) {
        int layoutId = annotation.value();
        if (layoutId > 0) {
            uniLayout.setView(inflater.inflate(layoutId, container, false));
        }
    }
}
