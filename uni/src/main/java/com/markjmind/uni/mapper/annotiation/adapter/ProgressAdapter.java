/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.mapper.ClassInjectAdapter;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.progress.UniProgress;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
public class ProgressAdapter extends ClassInjectAdapter<Progress> {

    private ProgressBuilder progress;

    public ProgressAdapter(ProgressBuilder progress){
        this.progress = progress;
    }

    @Override
    public void injectClass(Progress annotation, Class targetClass, Object targetObject) {
        if(progress!=null) {
            Class<UniProgress> type = (Class<UniProgress>) annotation.type();
            int mode = annotation.mode();

            if (!type.equals(Progress.None.class)) {
                try {
                    UniProgress info = type.newInstance();
                    progress.set(mode, info);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                int res = annotation.res();
                if (res != -1) {
                    progress.set(mode, res);
                }
            }

            if (progress.get() != null) {
                progress.get().onBind();
            }
        }
    }

    @Override
    public Class<Progress> getAnnotationType() {
        return Progress.class;
    }
}
