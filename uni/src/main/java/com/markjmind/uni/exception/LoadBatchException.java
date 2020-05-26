package com.markjmind.uni.exception;

import com.markjmind.uni.LoadBatch;

public class LoadBatchException extends Exception{
    private Exception originException;
    private Class<? extends LoadBatch> loadBatchClass;

    public LoadBatchException(Exception e, Class<? extends LoadBatch> loadBatchClass){
        super(loadBatchClass.getCanonicalName()+":"+e.getMessage(), e);
        this.originException = e;
        this.loadBatchClass = loadBatchClass;
    }

    public Exception getOriginException() {
        return originException;
    }

    public void setOriginException(Exception originException) {
        this.originException = originException;
    }

    public Class<? extends LoadBatch> getLoadBatchClass() {
        return loadBatchClass;
    }

    public void setLoadBatchClass(Class<? extends LoadBatch> loadBatchClass) {
        this.loadBatchClass = loadBatchClass;
    }


}
