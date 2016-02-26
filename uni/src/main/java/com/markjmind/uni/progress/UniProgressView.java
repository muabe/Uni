/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

import android.content.Context;
import android.view.View;

import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-25
 */
public class UniProgressView extends ProgressInfo implements OnProgressListener{

    protected UniProgressView(){
        setListener(this);
    }

    public UniProgressView(int layoutId){
        setLayoutId(layoutId);
        setListener(this);
    }

    @Override
    public UniProgress.Mode getMode() {
        return UniProgress.Mode.view;
    }

    @Override
    public void onStart(View layout, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onDestroy(View layout, boolean attach) {

    }

    public View findViewById(int id){
        return mapper.findViewById(id);
    }

    public Context getContext(){
        return layout.getContext();
    }
}
