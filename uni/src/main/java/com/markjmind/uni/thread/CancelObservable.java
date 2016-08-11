package com.markjmind.uni.thread;

import android.os.AsyncTask;

import com.markjmind.uni.common.StoreObservable;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class CancelObservable extends StoreObservable<UniMainThread> implements CancelObserver {
    private boolean taskAutoCanceled = true;
    private boolean isAttached;

    public CancelObservable(){
        isAttached = false;
    }

    @Override
    public synchronized void add(UniMainThread observer) {
        String className = observer.getId();
        super.add(observer);
//        Log.e("DetachedObservable", className+" add:"+size());
    }

    @Override
    public synchronized void remove(String id) {
        UniMainThread observer = get(id);
        if(observer != null) {
            String className = observer.getId();
            super.remove(id);
//            Log.e("DetachedObservable", className + " remove:" + size());
        }
    }

    @Override
    public synchronized void cancel(String id){
        UniMainThread observer = get(id);
        if(observer != null) {
            String className = observer.getId();
            get(id).cancel();
//            Log.e("DetachedObservable", className + " cancel:" + size());
            remove(id);
        }
    }

    public synchronized void cancelRecent() {
        this.cancel(getLastKey());
    }

    @Override
    public synchronized void cancelAll(){
        synchronized (this) {
            String[] keys = getStore().getKeys();
            for (String key: keys) {
                cancel(key);
            }
        }
    }

    public AsyncTask.Status getStatus(String id){
        if(id==null){
            return null;
        }
        UniMainThread observer = get(id);
        if(observer==null){
            return null;
        }
        return observer.getStatus();
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean isAttached) {
        this.isAttached = isAttached;
    }

    public CancelAdapter getCancelAdapter(String taskId){
        return new CancelAdapter(taskId, this);
    }

    public boolean isTaskAutoCanceled() {
        return taskAutoCanceled;
    }

    public void setTaskAutoCanceled(boolean taskAutoCanceled) {
        this.taskAutoCanceled = taskAutoCanceled;
    }
}
