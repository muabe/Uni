/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObserver;
import com.markjmind.uni.thread.LoadEvent;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
public class UniDialog extends Dialog implements UniTask, CancelObserver{
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;

    private UniView uniView;
    private Class<? extends UniView> customUniView;

    private UniBuilder.UniInterface uniInterface = new UniBuilder.UniInterface() {
        @Override
        public UniTask getUniTask() {
            return UniDialog.this;
        }
        @Override
        public UniMapper getMapper() {
            return (UniMapper)mapper;
        }
        @Override
        public Class<? extends UniView> getCustomUniView() {
            return customUniView;
        }
        @Override
        public Store<?> getParam() {
            return param;
        }
        @Override
        public ProgressBuilder getProgress() {
            return progress;
        }
        @Override
        public void reset() {
            param.clear();
        }
    };

    public UniDialog(Context context) {
        super(context);
        uniView = null;
        mapper = new UniMapper();
        param = new Store<>();
        progress = new ProgressBuilder();
    }

    public UniDialog(Context context, int themeResId) {
        super(context, themeResId);
        uniView = null;
        mapper = new UniMapper();
        param = new Store<>();
        progress = new ProgressBuilder();
    }

    public <T extends UniView> UniDialog(Context context, Class<T> uniView){
        this(context);
        customUniView = uniView;
    }

    public <T extends UniView> UniDialog(Context context, int themeResId, Class<T> uniView){
        this(context, themeResId);
        customUniView = uniView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniView = UniBuilder.createUniView(getContext(), uniInterface, null);
        setContentView(uniView);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                uniView.excute();
            }
        });

    }

    public void excute(UniTask uniTask){
        uniView.excute(uniTask);
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/

    @Override
    public void cancel(String id) {
        uniView.cancel(id);
    }

    @Override
    public void cancelAll() {
        uniView.cancelAll();
    }


    /*************************************************** 인터페이스 관련 *********************************************/

    @Override
    public void onBind() {
        uniView.onBind();
    }

    @Override
    public void onPre() {
        uniView.onPre();
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        uniView.onLoad(event, cancelAdapter);
    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {
        uniView.onUpdate(value, cancelAdapter);

    }

    @Override
    public void onPost() {
        uniView.onPost();
    }

    @Override
    public void onPostFail(String message, Object arg) {
        uniView.onPostFail(message, arg);
    }

    @Override
    public void onException(Exception e) {
        uniView.onException(e);
    }

    @Override
    public void onCancelled(boolean attach) {
        uniView.onCancelled(attach);
    }

}
