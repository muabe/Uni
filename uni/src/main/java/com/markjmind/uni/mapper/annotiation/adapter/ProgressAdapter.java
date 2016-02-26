/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper.annotiation.adapter;

import com.markjmind.uni.mapper.ClassAdapter;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.ProgressInfo;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.progress.UniProgressDialog;
import com.markjmind.uni.progress.UniProgressView;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
public class ProgressAdapter extends ClassAdapter<Progress> {
    private UniProgress progress;

    public ProgressAdapter(UniProgress progress){
        this.progress = progress;
    }

    @Override
    public Class<Progress> getAnnotationClass() {
        return Progress.class;
    }

    @Override
    public void inject(Progress annotation, Class clz, Object targetObject) {
        Class<ProgressInfo> value = (Class<ProgressInfo>) annotation.value();
        if(!value.equals(Progress.None.class)){
            try {
                ProgressInfo info = (ProgressInfo)value.newInstance();
                if(info.getMode().equals(UniProgress.Mode.dialog)){
                    progress.set((UniProgressDialog)info);
                }else if(info.getMode().equals(UniProgress.Mode.view)){
                    progress.set((UniProgressView)info);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            int res =  annotation.res();
            if(res != -1){
                UniProgress.Mode mode = annotation.mode();
                if(mode.equals(UniProgress.Mode.none)){
                    mode = UniProgress.Mode.dialog;
                }
                progress.set(res, mode);
            }
        }
    }
}
