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
public class CancelAdapter implements CancelObserver {
    private String taskId;
    private CancelObserver cancelObserver;

    public CancelAdapter(String taskId, CancelObserver cancelObserver){
        this.taskId = taskId;
        this.cancelObserver = cancelObserver;
    }

    public String getTaskId(){
        return this.taskId;
    }

    public void cancel(){
        cancelObserver.cancel(taskId);
    }

    @Override
    public void cancel(String taskId) {
        cancelObserver.cancel(taskId);
    }

    @Override
    public void cancelAll() {
        cancelObserver.cancelAll();
    }
}
