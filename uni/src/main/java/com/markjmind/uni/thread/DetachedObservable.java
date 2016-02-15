/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

import android.util.Log;

import com.markjmind.uni.common.StoreObservable;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class DetachedObservable extends StoreObservable<InnerUniTask>{

    @Override
    public void add(InnerUniTask observer) {
        String className = observer.getId();
        super.add(observer);
        Log.e("DetachedObservable", className+" add:"+size());
    }

    @Override
    public synchronized void remove(String id) {
        String className = get(id).getId();
        super.remove(id);
        Log.e("DetachedObservable", className + " remove:" + size());
    }


    public void cancel(String id){
        String className = get(id).getId();
        get(id).cancel();
        Log.e("DetachedObservable", className + " cancel:" + size());
        remove(id);
    }

    public synchronized void cancelAll(){
        synchronized (this) {
            String[] keys = getStore().getKeys();
            for (String key: keys) {
                cancel(key);
            }
        }
    }
}