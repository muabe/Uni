package com.markjmind.uni.thread;

import android.os.AsyncTask;
import android.os.Build;

import com.markjmind.uni.UniInterface;
import com.markjmind.uni.progress.ProgressBuilder;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-04-29
 */
public abstract class UniAsyncTask implements UniInterface{
    private CancelObservable cancelObservable;
    public ProgressBuilder progress;

    public UniAsyncTask(){
        cancelObservable = new CancelObservable();
        progress = new ProgressBuilder();
    }

    public UniAsyncTask(ProgressBuilder progress){
        cancelObservable = new CancelObservable();
        this.progress = progress;
    }

    public String excute(UniInterface uniInterface){
        UniMainThread task = new UniMainThread(cancelObservable);
        if(progress.isAble()) {
            task.addTaskObserver(progress);
        }
        task.addTaskObserver(new ProcessAdapter(uniInterface));
        cancelObservable.add(task);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task.getId();
    }

    public String excute(){
        return this.excute(this);
    }

    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {

    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {

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

    public void cancel(String id){
        cancelObservable.cancel(id);
    }

    public void cancelAll(){
        cancelObservable.cancelAll();
    }
}
