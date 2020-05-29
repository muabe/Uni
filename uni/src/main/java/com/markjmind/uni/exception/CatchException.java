package com.markjmind.uni.exception;

import android.util.Log;

import com.markjmind.uni.common.Store;

public class CatchException {
    private Store exceptionStore = new Store();
    private OnExceptionListener defaultException;

    public boolean onException(Exception exception){
        boolean result = false;
        if(exceptionStore.containsKey(exception.getClass().getName())){
            result = ((OnExceptionListener)exceptionStore.get(exception.getClass().getName())).onException(exception);
        }else{
            if(defaultException != null){
                    defaultException.onException(exception);
            }
        }
        return result;
    }


    public void addException(Class<? extends Exception> e, OnExceptionListener listener){
        Log.e("dd","여기:"+e.getName());
        exceptionStore.add(e.getName(), listener);
    }

    public void removeException(Class<? extends Exception> e){
        exceptionStore.remove(e.getName());
    }

    public void setDefaultException(OnExceptionListener listener){
        this.defaultException = listener;
    }
}
