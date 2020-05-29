package com.markjmind.uni;

import android.util.Log;

import com.markjmind.uni.exception.LoadBatchException;
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
    private LoadEvent event;
    private CancelAdapter cancelAdapter;
    private boolean isPreUpdate = true;
    private Object param;
    private LoadBatch root = null;

    public LoadBatch(){
        root = this;
    }

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
            isPreUpdate = true;
            try {
                load(0, event, cancelAdapter);
            }catch (Exception e){
                throw new LoadBatchException(e, getLoadClass());
            }
        }

        @Override
        public void onUpdate(Object value, CancelAdapter cancelAdapter) {
            if(isPreUpdate) {
                isPreUpdate = false;
                preUpdate((String) value);
            }else {
                lockUpdate((String) value, cancelAdapter);
            }
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

    public void onEnd(){

    }

    private Class<? extends LoadBatch> getLoadClass(){
        return getClass();
    }

    void preUpdate(String value){
        if((preUpdate+index).equals(value)){
            onPreUpdate();
        }else if(next != null){
            next.preUpdate(value);
        }
    }

    void load(int index, LoadEvent event, CancelAdapter cancelAdapter) throws Exception{

        this.index = index;
        this.event = event;

            Log.i("LoadUpdate","LoadUpdate : "+index);
            event.update(preUpdate+index);
            retunValue = LoadBatch.this.onLoad(event, cancelAdapter);
            event.lockedUpdate(batchLock+index);
            if(next != null){
                next.load(++index, event, cancelAdapter);
            }

    }

    void lockUpdate(String value, CancelAdapter cancelAdapter){
        if(isExcepted){
            unlock();
            return;
        }
        if((batchLock+index).equals(value)){
            this.cancelAdapter = cancelAdapter;
            onUpdate(retunValue, cancelAdapter);
        }else if(next != null){
            next.lockUpdate(value, cancelAdapter);
        }
    }

    public Object getParam(){
        return param;
    }

    public void update(Object param){
        this.param = param;
        event.update(batchLock+index);
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
        batch.root = this.root;
    }
    public void setException(UniInterface exception){
        this.exception = exception;
    }

    LoadBatch endPost;

    public void end(){
        root.endPost = this;
        unlock();
    }
}
