/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-18
 */
public abstract class UniTaskAdapter implements UniTask {
    UniTask uniTask;

    public UniTaskAdapter(UniTask uniTask){
        this.uniTask = uniTask;
    }

    @Override
    public void onBind(){

    }

    @Override
    public void onPre(){

    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter){

    }

    @Override
    public void onPostFail(String message, Object arg){
        uniTask.onPostFail(message, arg);
    }

    @Override
    public void onException(Exception e) {
        uniTask.onException(e);
    }

    @Override
    public void onCancelled(boolean attached){
        uniTask.onCancelled(attached);
    }

    public UniTask getParents(){
        return uniTask;
    }
}
