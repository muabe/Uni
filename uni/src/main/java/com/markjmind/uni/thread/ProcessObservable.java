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
public class ProcessObservable implements ProcessObserver {

    private ArrayList<ProcessObserver> observers = new ArrayList<>();

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        for(ProcessObserver observer : observers){
            observer.onPreExecute(cancelAdapter);
        }
    }

    @Override
    public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception{
        for(ProcessObserver observer : observers){
            observer.doInBackground(event, cancelAdapter);
        }
    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        for(ProcessObserver observer : observers){
            observer.onProgressUpdate(value, cancelAdapter);
        }
    }

    @Override
    public void onPostExecute() {
        for(ProcessObserver observer : observers){
            observer.onPostExecute();
        }
    }

    @Override
    public void onFailedExecute(String message, Object arg) {
        for(ProcessObserver observer : observers){
            observer.onFailedExecute(message, arg);
        }
    }

    @Override
    public void onExceptionExecute(Exception e) {
        for(ProcessObserver observer : observers){
            observer.onExceptionExecute(e);
        }
    }

    @Override
    public void onCancelled(boolean attached) {
        for(ProcessObserver observer : observers){
            observer.onCancelled(attached);
        }
    }

    public void add(ProcessObserver processObserver){
        observers.add(processObserver);
    }
}
