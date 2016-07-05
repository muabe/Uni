/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.mapper.ClassInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Layout;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-12
 */
public class LayoutAdapter extends ClassInjectAdapter<Layout> {
    int layoutId = 0;

    public int getLayoutId(){
        return layoutId;
    }

    @Override
    public Class<Layout> getAnnotationType() {
        return Layout.class;
    }

    @Override
    public void injectClass(Layout annotation, Class targetClass, Object targetObject) {
        layoutId = annotation.value();
    }
}
