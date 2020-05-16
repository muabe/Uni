package com.markjmind.uni;

import android.util.Log;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

public abstract class LoadBatch<RetunValue>{
    private boolean isExcepted = false;
    private RetunValue retunValue;
    private UniInterface exception;
    private String batchLock = "load lock batch index : ";
    private String preUpdate = "load pre update index : ";
    private LoadBatch<?> next;
    private int index = 0;
    private CancelAdapter cancelAdapter;
    private boolean isPreUpdate = false;

    public UniInterface getBatch(){
        return uniInterface;
    }

    private UniInterface uniInterface = new UniInterface() {
        @Override
        public void onBind() {

        }

        @Override
        public void onPre() {

        }

        @Override
        public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
            load(0, event, cancelAdapter);
        }

        @Override
        public void onUpdate(Object value, CancelAdapter cancelAdapter) {
            preUpdate((String)value);
            update((String)value, cancelAdapter);
        }

        @Override
        public void onPost() {

        }

        @Override
        public void onPostFail(String message, Object arg) {

        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onCancelled(boolean attached) {

        }
    };

    abstract public void onPreUpdate();

    abstract public RetunValue onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception;

    abstract public void onUpdate(RetunValue value, CancelAdapter cancelAdapter);


    void preUpdate(String value){
        if((preUpdate+index).equals(value)){
            onPreUpdate();
        }else if(next != null){
            next.preUpdate(value);
        }
    }

    void load(int index, LoadEvent event, CancelAdapter cancelAdapter){
        this.index = index;
        try {
            Log.i("LoadUpdate","LoadUpdate : "+index);
            event.update(preUpdate+index);
            retunValue = LoadBatch.this.onLoad(event, cancelAdapter);
            event.lockedUpdate(batchLock+index);
            if(next != null){
                next.load(++index, event, cancelAdapter);
            }

        }catch (Exception e){
            isExcepted = true;
            LoadBatch.this.onException(e);
            cancelAdapter.cancelAll();
        }
    }

    void update(String value, CancelAdapter cancelAdapter){
        if((batchLock+index).equals(value)){
            this.cancelAdapter = cancelAdapter;
            onUpdate(retunValue, cancelAdapter);
        }else if(next != null){
            next.update(value, cancelAdapter);
        }

    }

    public void unlock(){
        if(cancelAdapter != null){
            cancelAdapter.unlock();
        }
    }

    public void unlockNext(LoadBatch next){
        if(cancelAdapter != null){
            cancelAdapter.unlock();
            this.next(next);
        }
    }

    public void onException(Exception e) {
        if(exception != null){
            exception.onException(e);
        }
    }

    public void next(LoadBatch<?> batch){
        this.next = batch;
    }
    public void setException(UniInterface exception){
        this.exception = exception;
    }


}
