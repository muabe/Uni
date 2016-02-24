package com.markjmind.uni.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.markjmind.uni.common.StoreObserver;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class UniMainAsyncTask extends AsyncTask<Void, Object, Boolean> implements StoreObserver<CancelObservable>, UpdateEvent{
    private String taskId;
    private boolean isCancel;
    private CancelObservable observable;
    private CancelAdapter cancelAdapter;
    private ProcessObservable taskObservable = new ProcessObservable();

    private Exception doInBackException;

    public UniMainAsyncTask(CancelObservable observable){
        this.isCancel = false;
        this.observable = observable;
        this.taskId = ""+this.hashCode();
        this.doInBackException=null;
        this.cancelAdapter = observable.getCancelAdapter(this.taskId);
    }


    @Override
    protected void onPreExecute() {
        taskObservable.onPreExecute(cancelAdapter);
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        doInBackException = null;
        try{
            taskObservable.doInBackground(this, cancelAdapter);
            return true;
        }catch(Exception e){
            doInBackException = e;
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if(!isCancel){
            Object value = null;
            if(values != null) {
                value = values[0];
            }
            taskObservable.onProgressUpdate(value, cancelAdapter);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.e("DetachedObservable", getId()+" Post");
        if(!isCancel){
            if(result) { //성공
                taskObservable.onPostExecute();
            }else{ // 실패
                taskObservable.onFailExecute(false, "", doInBackException);
            }
            observable.remove(this);
        }
    }


    @Override
    protected void onCancelled() {
        taskObservable.onCancelled(observable.isAttached());
    }

    public synchronized void cancel() {
        if (!isCancel){
            this.isCancel = true;
            super.cancel(true);
        }
    }
    public boolean isCancel(){
        return isCancel;
    }


    @Override
    public void update(Object value) {
        this.publishProgress(value);
     }

    @Override
    public void notifyChange(CancelObservable observable, Object data) {
        cancel();
    }

    @Override
    public String getId() {
        return taskId;
    }

    public UniMainAsyncTask addTaskObserver(ProcessObserver observer){
        taskObservable.add(observer);
        return this;
    }

}