/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-16
 */
public class CancelAdapter implements CancelObservable{
    private String taskId;
    private CancelObservable cancelObservable;

    public CancelAdapter(String taskId, CancelObservable cancelObservable){
        this.taskId = taskId;
        this.cancelObservable = cancelObservable;
    }

    public String getTaskId(){
        return this.taskId;
    }

    public void cancel(){
        cancelObservable.cancel(taskId);
    }

    @Override
    public void cancel(String taskId) {
        cancelObservable.cancel(taskId);
    }

    @Override
    public void cancelAll() {
        cancelObservable.cancelAll();
    }
}
