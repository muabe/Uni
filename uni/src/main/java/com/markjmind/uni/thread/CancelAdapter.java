package com.markjmind.uni.thread;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-16
 */
public class CancelAdapter implements CancelObserver {
    private String taskId;
    private CancelObserver cancelObserver;

    public CancelAdapter(String taskId, CancelObserver cancelObserver){
        this.taskId = taskId;
        this.cancelObserver = cancelObserver;
    }

    public String getTaskId(){
        return this.taskId;
    }

    public void cancel(){
        cancelObserver.cancel(taskId);
    }

    @Override
    public void cancel(String taskId) {
        cancelObserver.cancel(taskId);
    }

    @Override
    public void cancelAll() {
        cancelObserver.cancelAll();
    }
}
