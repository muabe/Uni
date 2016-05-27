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
public class ThreadProcessObservable extends ThreadProcessObserver {

    private ArrayList<ThreadProcessObserver> observers = new ArrayList<>();

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        for(ThreadProcessObserver observer : observers){
            observer.onPreExecute(cancelAdapter);
        }
    }

    @Override
    public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception{
        for(ThreadProcessObserver observer : observers){
            observer.doInBackground(event, cancelAdapter);
        }
    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        for(ThreadProcessObserver observer : observers){
            observer.onProgressUpdate(value, cancelAdapter);
        }
    }

    @Override
    public void onPostExecute() {
        for(ThreadProcessObserver observer : observers){
            observer.onPostExecute();
        }
    }

    @Override
    public void onFailedExecute(String message, Object arg) {
        for(ThreadProcessObserver observer : observers){
            observer.onFailedExecute(message, arg);
        }
    }

    @Override
    public void onExceptionExecute(Exception e) {
        for(ThreadProcessObserver observer : observers){
            observer.onExceptionExecute(e);
        }
    }

    @Override
    public void onCancelled(boolean attached) {
        for(ThreadProcessObserver observer : observers){
            if(observer.getCancelAop()!=null){
                observer.getCancelAop().beforeOnCancel(attached);
                observer.onCancelled(attached);
                observer.getCancelAop().afterOnCancel(attached);
            }else{
                observer.onCancelled(attached);
            }

        }
    }

    public void add(ThreadProcessObserver threadProcessObserver){
        observers.add(threadProcessObserver);
    }
}
