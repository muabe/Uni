/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-19
 */
public abstract class ProgressInfo {
    public Store<?> param;
    public Mapper mapper;

    private int layoutId;
    protected View layout;
    private OnProgressListener listener;

    protected ProgressInfo(){
        this.layoutId = -1;
        param = new Store<>();
    }

    public ProgressInfo(int layoutId){
        this.layoutId = layoutId;
        param = new Store<>();
    }

    public abstract UniProgress.Mode getMode();

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public OnProgressListener getListener() {
        return listener;
    }

    public void setListener(OnProgressListener listener) {
        this.listener = listener;
    }

    View mapperInit(ViewGroup finder) {
        if(listener == null) {
            LayoutInflater inflater = ((LayoutInflater) finder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = inflater.inflate(layoutId, finder, false);
        }else{
            mapper = new UniMapper(finder, this);
            mapper.inject(LayoutAdapter.class);
            if(layoutId == -1) {
                layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
            }

            LayoutInflater inflater = ((LayoutInflater) finder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = inflater.inflate(layoutId, finder, false);
            mapper.reset(layout, this);
            mapper.addAdapter(new ParamAdapter(param));
            mapper.injectWithout(LayoutAdapter.class);
        }
        return layout;
    }


}
