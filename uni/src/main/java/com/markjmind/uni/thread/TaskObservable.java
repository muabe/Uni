/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

import java.util.ArrayList;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-17
 */
public class TaskObservable implements TaskObserver{

    private ArrayList<TaskObserver> observers = new ArrayList<>();

    @Override
    public void onPreExecute(InnerUniTask uniTask) {
        for(TaskObserver observer : observers){
            observer.onPreExecute(uniTask);
        }
    }

    @Override
    public void doInBackground(InnerUniTask uniTask, CancelAdapter cancelAdapter) throws Exception{
        for(TaskObserver observer : observers){
            observer.doInBackground(uniTask, cancelAdapter);
        }
    }

    @Override
    public void onProgressUpdate(InnerUniTask uniTask, Object value, CancelAdapter cancelAdapter) {
        for(TaskObserver observer : observers){
            observer.onProgressUpdate(uniTask, value, cancelAdapter);
        }
    }

    @Override
    public void onPostExecute(InnerUniTask uniTask) {
        for(TaskObserver observer : observers){
            observer.onPostExecute(uniTask);
        }
    }

    @Override
    public void onFailExecute(InnerUniTask uniTask, boolean isException, String message, Exception e) {
        for(TaskObserver observer : observers){
            observer.onFailExecute(uniTask, isException, message, e);
        }
    }

    @Override
    public void onCancelled(InnerUniTask uniTask, boolean detach) {
        for(TaskObserver observer : observers){
            observer.onCancelled(uniTask, detach);
        }
    }

    public void add(TaskObserver taskObserver){
        observers.add(taskObserver);
    }
}
