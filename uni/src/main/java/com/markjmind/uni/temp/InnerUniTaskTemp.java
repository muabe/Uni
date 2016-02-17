/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.temp;

import android.os.AsyncTask;
import android.util.Log;

import com.markjmind.uni.UniInterface;
import com.markjmind.uni.UniProgress;
import com.markjmind.uni.common.StoreObserver;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.DetachedObservable;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class InnerUniTaskTemp extends AsyncTask<Void, Object, Boolean> implements StoreObserver<DetachedObservable>, UpdateEvent{
    private String taskId;
    private boolean isCancel;
    private DetachedObservable observable;
    private CancelAdapter cancelAdapter;
    private UniInterface uniInterface;
    private UniProgress progress;

    private Exception doInBackException;

    public InnerUniTaskTemp(DetachedObservable observable, UniInterface uniInterface, UniProgress progress){
        this.isCancel = false;
        this.observable = observable;
        this.uniInterface = uniInterface;
        this.taskId = ""+this.hashCode();
        this.progress = progress;
        this.doInBackException=null;
        this.cancelAdapter = new CancelAdapter(this.taskId, observable);
    }


    @Override
    protected void onPreExecute() {
        progress.show();
        uniInterface.onPre();
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        doInBackException = null;
        try{
            uniInterface.onLoad(this, cancelAdapter);
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
            //프로그래스바 업데이트
            progress.getOnProgressListener().onUpdate(progress.getLayout(), value, cancelAdapter);
            //업데이트실행
            uniInterface.onUpdate(value, cancelAdapter);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.e("DetachedObservable", getId()+" Post");
        if(!isCancel){
            if(result) { //성공
                progress.dismiss();
                uniInterface.onPost();
            }else{ // 실패
                progress.dismiss();
                uniInterface.onFail(false, "", doInBackException);
            }
//            observable.remove(this);
        }
    }


    @Override
    protected void onCancelled() {
        progress.dismiss();
        uniInterface.onCancelled(false);
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
    public void notifyChange(DetachedObservable observable, Object data) {
        cancel();
    }

    @Override
    public String getId() {
        return taskId;
    }


}