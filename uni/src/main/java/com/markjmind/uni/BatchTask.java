package com.markjmind.uni;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

public class BatchTask extends UniAsyncTask{
    protected Store batchParam;
    private UniLayout uniLayout;

    void init(UniLayout uniLayout){
        inheritFault(uniLayout.getUniTask());
        this.uniLayout = uniLayout;
    }

    void initBatchParam(Store batchParam){
        this.batchParam = batchParam;
    }

    public void addBatchParam(String key, Object value){
        batchParam.add(key, value);
    }

    public Object getBatchParam(String key){
       return batchParam.get(key);
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {

    }

    protected void next(Class<BatchTask> batchTaskClass){
        try {
            BatchTask batchTask = batchTaskClass.newInstance();
            batchTask.init(uniLayout);
            batchTask.initBatchParam(batchParam);
            batchTask.excute();
        } catch (Exception e) {
            throw new UinMapperException(ErrorMessage.Runtime.batchException(batchTaskClass));
        }
    }
}
