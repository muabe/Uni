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
    public void onPreExecute(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) {
        for(TaskObserver observer : observers){
            observer.onPreExecute(uniTask, cancelAdapter);
        }
    }

    @Override
    public void doInBackground(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) throws Exception{
        for(TaskObserver observer : observers){
            observer.doInBackground(uniTask, cancelAdapter);
        }
    }

    @Override
    public void onProgressUpdate(UniMainAsyncTask uniTask, Object value, CancelAdapter cancelAdapter) {
        for(TaskObserver observer : observers){
            observer.onProgressUpdate(uniTask, value, cancelAdapter);
        }
    }

    @Override
    public void onPostExecute(UniMainAsyncTask uniTask) {
        for(TaskObserver observer : observers){
            observer.onPostExecute(uniTask);
        }
    }

    @Override
    public void onFailExecute(UniMainAsyncTask uniTask, boolean isException, String message, Exception e) {
        for(TaskObserver observer : observers){
            observer.onFailExecute(uniTask, isException, message, e);
        }
    }

    @Override
    public void onCancelled(UniMainAsyncTask uniTask, boolean attached) {
        for(TaskObserver observer : observers){
            observer.onCancelled(uniTask, attached);
        }
    }

    public void add(TaskObserver taskObserver){
        observers.add(taskObserver);
    }
}
