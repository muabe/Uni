/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-19
 */
abstract  class BaseProgressInfo {
    private int layoutId;
    private OnProgressListener listener;


    public BaseProgressInfo(int layoutId){
        this(layoutId, null);
    }
    public BaseProgressInfo(int layoutId, OnProgressListener listener){
        this.layoutId = layoutId;
        this.listener = listener;
    }

    abstract UniProgress.Mode getMode();

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
}
