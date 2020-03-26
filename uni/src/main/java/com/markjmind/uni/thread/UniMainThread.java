package com.markjmind.uni.thread;

import android.os.AsyncTask;

import com.markjmind.uni.UniUncaughtException;
import com.markjmind.uni.common.StoreObserver;
import com.markjmind.uni.exception.UniLoadFailException;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class UniMainThread extends AsyncTask<Void, Object, Boolean> implements StoreObserver<CancelObservable>, LoadEvent {
    private String taskId;
    private boolean isCancel;
    private CancelObservable observable;
    private CancelAdapter cancelAdapter;
    private ThreadProcessObservable taskObservable = new ThreadProcessObservable();
    private Exception doInBackException;
    private UniUncaughtException uncaughtException;
    private UpdateInfo info;


    public UniMainThread(CancelObservable observable){
        this.isCancel = false;
        this.observable = observable;
        this.taskId = ""+this.hashCode();
        this.doInBackException=null;
        this.cancelAdapter = observable.getCancelAdapter(this.taskId);
    }


    @Override
    protected void onPreExecute() {
        try {
            taskObservable.onPreExecute(cancelAdapter);
        }catch (Exception e){
            this.isCancel = true;
            super.cancel(true);
            observable.remove(this);
            if(uncaughtException!=null){
                uncaughtException.uncaughtException(e);
            }else {
                throw e;
            }
        }
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
//        if(!isCancel){
//            Object value = null;
//            if(values != null) {
//                value = values[0];
//            }
//            taskObservable.onProgressUpdate(value, cancelAdapter);
//        }

        if(!isCancel){
            UpdateInfo info = (UpdateInfo)values[0];
            taskObservable.onProgressUpdate(info.paramValue, cancelAdapter);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if((observable.isTaskAutoCanceled() && !isCancel()) || (!observable.isTaskAutoCanceled() && observable.isAttached())){
            try {
                if(result) { //성공
                    taskObservable.onPostExecute();
                }else{ // 실패
                    if(doInBackException instanceof UniLoadFailException){
                        UniLoadFailException ulfe = (UniLoadFailException)doInBackException;
                        taskObservable.onFailedExecute(ulfe.getMessage(), ulfe.getArg());
                    }else{
                        taskObservable.onExceptionExecute(doInBackException);
                    }
                }
                observable.remove(this);
            }catch (Exception e){
                observable.remove(this);
                if(uncaughtException!=null){
                    uncaughtException.uncaughtException(e);
                }else {
                    throw e;
                }
            }
        }else{
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
            if(observable.isTaskAutoCanceled()) {
                super.cancel(true);
            }
        }
    }
    public boolean isCancel(){
        return isCancel;
    }

    class UpdateInfo{
        Object paramValue;
        boolean isLock;
        boolean noty;

        UpdateInfo(Object paramValue){
            this(paramValue, false);
        }

        UpdateInfo(Object paramValue, boolean isLock){
            this.paramValue = paramValue;
            this.isLock = isLock;
            this.noty = false;
        }
    }

    @Override
    public void update(Object value) {
        this.publishProgress(new UpdateInfo(value));
    }


    @Override
    public void lockedUpdate(Object value) throws InterruptedException {
        info = new UpdateInfo(value, true);
        cancelAdapter.setLoadEvent(this);
        this.publishProgress(info);
        synchronized(this){
            while(!info.noty){
                this.wait();

            }
        }
    }

    @Override
    public void unlock() {
        synchronized(this) {
            if (info != null && info.isLock && !info.noty) {
                info.noty = true;
                this.notify();
            }
        }
    }


    @Override
    public void fail(String message) throws UniLoadFailException {
        throw new UniLoadFailException(message, null);
    }

    @Override
    public void fail(String message, Object arg) throws UniLoadFailException {
        throw new UniLoadFailException(message, arg);
    }

    @Override
    public void notifyChange(CancelObservable observable, Object data) {
        cancel();
    }

    @Override
    public String getId() {
        return taskId;
    }

    public UniMainThread addTaskObserver(ThreadProcessObserver observer){
        taskObservable.add(observer);
        return this;
    }

    public void setUIuncaughtException(UniUncaughtException uncaughtException){
        this.uncaughtException = uncaughtException;
    }

}