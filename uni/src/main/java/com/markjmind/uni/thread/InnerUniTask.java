package com.markjmind.uni.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.markjmind.uni.UniInterface;
import com.markjmind.uni.UniProgress;
import com.markjmind.uni.common.StoreObserver;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class InnerUniTask extends AsyncTask<Void, Object, Boolean> implements StoreObserver<DetachedObservable>, UpdateEvent{
    private String taskId;
    private boolean isCancel;
    private DetachedObservable observable;
    private CancelAdapter cancelAdapter;
    private UniInterface uniInterface;
    private UniProgress progress;

    private Exception doInBackException;

    public InnerUniTask(DetachedObservable observable, UniInterface uniInterface, UniProgress progress){
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
            progress.dismiss();
            if(result) {
                uniInterface.onPost();
            }else{
                uniInterface.onFail(false, "", doInBackException);
            }
            observable.remove(this);
        }
    }

    public synchronized void cancel() {
        if (!isCancel){
            this.isCancel = true;
            super.cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        progress.dismiss();
        uniInterface.onCancelled();
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