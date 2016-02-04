/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.common;

import com.markjmind.uni.hub.Store;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-03
 */
public class StoreObservable {

    private Store<StoreObserver> pool = new Store<>();

    public StoreObservable() {
    }


    public void add(StoreObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!pool.containsKey(observer.getId()))
                pool.add(observer.getId(), observer);
        }
    }



    public synchronized void remove(String key) {
        pool.remove(key);
    }

    public synchronized void remove(StoreObserver observer) {
        this.remove(observer.getId());
    }

    public synchronized void removeAll(){
        synchronized (this) {
            String[] keys = pool.getKeys();
            for (String key: keys) {
                remove(key);
            }
        }
    }

    public synchronized void clear() {
        pool.clear();
    }

    public synchronized void notifyChanges() {
        notifyChanges(null);
    }


    public synchronized void notifyChanges(Object data) {
        synchronized (this) {
            String[] keys = pool.getKeys();
            for (String key: keys) {
                StoreObserver observer = pool.get(key);
                if(observer!=null) {
                    observer.notifyChange(this, data);
                }
            }
        }
    }

    public int size() {
        return pool.size();
    }

    public String[] getIds(){
        return pool.getKeys();
    }
}
