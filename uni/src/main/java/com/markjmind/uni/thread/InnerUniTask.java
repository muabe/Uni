package com.markjmind.uni.thread;

import android.os.AsyncTask;

import com.markjmind.uni.UniInterface;
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
    private UniInterface uniInterface;
    private int requestCode;

    public InnerUniTask(int requestCode, DetachedObservable observable, UniInterface uniInterface){
        this.isCancel = false;
        this.requestCode = requestCode;
        this.observable = observable;
        this.uniInterface = uniInterface;
        this.taskId = ""+this.hashCode();
    }


    @Override
    protected void onPreExecute() {

    }

    private Exception doInBackException=null;
    @Override
    protected Boolean doInBackground(Void... params) {
        try{

            return true;
        }catch(Exception e){
            doInBackException = e;
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if(!isCancel){

        }else{
            cancel();
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(!isCancel){

        }else{
            cancel();
        }
    }

    public synchronized void cancel() {
        if (!isCancel){
            observable.remove(this);
            this.isCancel = true;
            uniInterface.onCancelled(requestCode);
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