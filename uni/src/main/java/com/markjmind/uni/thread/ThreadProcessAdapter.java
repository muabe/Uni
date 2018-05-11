package com.markjmind.uni.thread;

import com.markjmind.uni.thread.aop.AopListener;
import com.markjmind.uni.UniInterface;
import com.markjmind.uni.UniLoadFail;

import java.util.ArrayList;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-11
 */
public class ThreadProcessAdapter extends ThreadProcessObserver {
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;
    private boolean skipOnPre;
    private ArrayList<AopListener> aopListeners;

    public ThreadProcessAdapter(UniInterface uniInterface, UniLoadFail uniLoadFail, boolean skipOnPre, ArrayList<AopListener> aopListeners) {
        this.uniInterface = uniInterface;
        this.uniLoadFail = uniLoadFail;
        this.skipOnPre = skipOnPre;
        this.aopListeners = aopListeners;
    }

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        if(!skipOnPre) {
            for(AopListener aopListener : aopListeners){
                aopListener.beforeOnPre();
            }
            this.uniInterface.onPre();
            for(AopListener aopListener : aopListeners){
                aopListener.afterOnPre();
            }
        }
    }

    @Override
    public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        for(AopListener aopListener : aopListeners){
            aopListener.beforeOnLoad();
        }
        this.uniInterface.onLoad(event, cancelAdapter);
        for(AopListener aopListener : aopListeners){
            aopListener.afterOnLoad();
        }
    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        this.uniInterface.onUpdate(value, cancelAdapter);
    }

    @Override
    public void onPostExecute() {
        for(AopListener aopListener : aopListeners){
            aopListener.beforeOnPost();
        }
        this.uniInterface.onPost();
        for(AopListener aopListener : aopListeners){
            aopListener.afterOnPost();
        }
    }

    @Override
    public void onFailedExecute(String message, Object arg) {
        this.uniInterface.onPostFail(message, arg);
    }

    @Override
    public void onExceptionExecute(Exception e) {
        for(AopListener aopListener : aopListeners){
            aopListener.afterOnException(e);
        }
        if(uniLoadFail==null) {
            this.uniInterface.onException(e);
        }else{
            this.uniLoadFail.onException(e);
        }
        for(AopListener aopListener : aopListeners){
            aopListener.afterOnException(e);
        }
    }

    @Override
    public void onCancelled(boolean attached) {
        for(AopListener aopListener : aopListeners){
            aopListener.beforeOnCancel();
        }
        if(uniLoadFail==null) {
            this.uniInterface.onCancelled(attached);
        }else{
            this.uniLoadFail.onCancelled(attached);
        }
        for(AopListener aopListener : aopListeners){
            aopListener.afterOnCancel();
        }
    }
}
