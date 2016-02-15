/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.mapper.ClassAdapter;
import com.markjmind.uni.mapper.annotiation.Layout;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-12
 */
public class LayoutAdapter extends ClassAdapter<Layout>{
    int layoutId = 0;

    @Override
    public Class<Layout> getAnnotationClass() {
        return Layout.class;
    }

    @Override
    public void inject(Layout annotation, Class clz, Object targetObject) {
        layoutId = annotation.value();
    }

    public int getLayoutId(){
        return layoutId;
    }
}
