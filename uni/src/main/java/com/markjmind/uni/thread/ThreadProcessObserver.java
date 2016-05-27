/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

import com.markjmind.uni.thread.aop.CancelAop;
import com.markjmind.uni.thread.aop.UniAop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-17
 */
public abstract class ThreadProcessObserver {
    private UniAop uniAop;
    private CancelAop cancelAop;

    public abstract void onPreExecute(CancelAdapter cancelAdapter);

    public abstract void doInBackground(LoadEvent event,CancelAdapter cancelAdapter) throws Exception;

    public abstract void onProgressUpdate(Object value, CancelAdapter cancelAdapter);

    public abstract void onPostExecute();

    public abstract void onFailedExecute(String message, Object arg);

    public abstract void onExceptionExecute(Exception e);

    public abstract void onCancelled(boolean attached);

    public CancelAop getCancelAop() {
        if(uniAop!=null){
            return uniAop.getCancelAop();
        }else{
            return null;
        }

    }

    public ThreadProcessObserver setUniAop(UniAop uniAop){
        this.uniAop = uniAop;
        return this;
    }
}
