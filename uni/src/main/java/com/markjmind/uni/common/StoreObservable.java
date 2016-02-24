/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.common;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-03
 */
public class StoreObservable<T extends StoreObserver> {

    private Store<T> pool = new Store<>();

    public StoreObservable() {
    }


    public void add(T observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!pool.containsKey(observer.getId()))
                pool.add(observer.getId(), observer);
        }
    }

    public T get(String key){
        return pool.get(key);
    }

    public Store<T> getStore(){
        return pool;
    }


    public synchronized void remove(String id) {
        pool.remove(id);
    }

    public synchronized void remove(T observer) {
        this.remove(observer.getId());
    }

    public synchronized void removeAll(){
        synchronized (this) {
            String[] keys = pool.getKeys();
            for (String id: keys) {
                remove(id);
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
            for (String id: keys) {
                StoreObserver observer = pool.get(id);
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
