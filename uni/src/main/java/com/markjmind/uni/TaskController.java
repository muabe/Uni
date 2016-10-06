package com.markjmind.uni;

import android.os.AsyncTask;
import android.os.Build;

import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.thread.ThreadProcessAdapter;
import com.markjmind.uni.thread.UniMainThread;
import com.markjmind.uni.thread.aop.UniAop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-23
 */
public class TaskController {
    private UniTask uniTask;
    private boolean isAsync = true;
    private UniMainThread task;
    private CancelObservable cancelObservable;

    /** Option **/
    private ProgressBuilder progress;
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;
    private UniAop uniAop;
    private UniUncaughtException uncaughtException;

    public TaskController(UniInterface uniInterface){
        this.uniInterface = uniInterface;
    }


    void init(UniTask uniTask, CancelObservable cancelObservable) {
        this.uniTask = uniTask;
        this.cancelObservable = cancelObservable;
        if(this.cancelObservable == null){
            this.cancelObservable = new CancelObservable();
        }
        task = new UniMainThread(this.cancelObservable);
        if(uniTask!=null) {
            this.setProgress(uniTask.progress);
        }

    }



    /*********************************************************************************
     * Option 관련
     *********************************************************************************/
    public TaskController setAsync(boolean async) {
        isAsync = async;
        return this;
    }

    public TaskController setProgress(ProgressBuilder progress) {
        this.progress = progress;
        return this;
    }

    public ProgressBuilder getProgress(){
        return this.progress;
    }

    public TaskController setUniInterface(UniInterface uniInterface) {
        this.uniInterface = uniInterface;
        return this;
    }

    public UniInterface getUniInterface(){
        return this.uniInterface;
    }

    public TaskController setUniLoadFail(UniLoadFail uniLoadFail) {
        this.uniLoadFail = uniLoadFail;
        return this;
    }

    public TaskController setUniUncaughtException(UniUncaughtException uncaughtException) {
        this.uncaughtException = uncaughtException;
        return this;
    }

    public UniUncaughtException getUniUncaughtException() {
        return uncaughtException;
    }

    public TaskController setUniAop(UniAop uniAop) {
        this.uniAop = uniAop;
        return this;
    }

    /*********************************************************************************
     * Task상태 관련
     *********************************************************************************/

    public String getId(){
        return task.getId();
    }

    public AsyncTask.Status getStatus(){
        return task.getStatus();
    }

    public boolean isRunning(String taskId) {
        if (cancelObservable.getStatus(taskId) != null){
            return (cancelObservable.getStatus(taskId).equals(AsyncTask.Status.RUNNING));
        } else {
            return false;
        }
    }

    public boolean isRunning() {
        return this.isRunning(task.getId());
    }

    public boolean isFinished(){
        return getStatus().equals(AsyncTask.Status.FINISHED);
    }

    /*********************************************************************************
     * execute 관련
     *********************************************************************************/

    private synchronized String run(ProgressBuilder progress, UniInterface uniInterface, UniLoadFail uniLoadFail, boolean skipOnPre, UniAop uniAop, UniUncaughtException uncaughtException) {
        if(getStatus().equals(AsyncTask.Status.RUNNING)) {
            return null;
        }else if(getStatus().equals(AsyncTask.Status.FINISHED)){
            task = new UniMainThread(cancelObservable);
        }

        if (progress!=null && progress.isAble()) {
            task.addTaskObserver(progress);
        }
        task.addTaskObserver(new ThreadProcessAdapter(uniInterface, uniLoadFail, skipOnPre).setUniAop(uniAop));
        task.setUIuncaughtException(uncaughtException);
        cancelObservable.add(task);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

        return task.getId();
    }

    private synchronized String refresh(ProgressBuilder progress, UniLoadFail uniLoadFail, UniAop uniAop, UniUncaughtException uncaughtException) {
        cancelAll();
        return run(progress, uniInterface, uniLoadFail, true, uniAop, uncaughtException);
    }


    public synchronized String execute() {
        if (uniTask!=null) {
            uniTask.beforeExecute();
        }
        if (isAsync) {
            return run(progress, uniInterface, uniLoadFail, false, uniAop, uncaughtException);
        } else {
            uniInterface.onPre();
            return null;
        }
    }

    public void notifyPre() {
        uniInterface.onPre();
    }

    public void notifyPost() {
        uniInterface.onPost();
    }

    public void post() {
        if (uniTask!=null) {
            uniTask.beforeExecute();
        }
        uniInterface.onPost();
    }

    public synchronized String reLoad() {
        return refresh(progress, uniLoadFail, uniAop, uncaughtException);
    }

    public synchronized String refresh() {
        if (isAsync) {
            return refresh(progress, uniLoadFail, uniAop, uncaughtException);
        } else {
            uniInterface.onPre();
            return null;
        }
    }


    /*********************************************************************************
     * CancelObserver Interface 관련
     *********************************************************************************/
    public synchronized void cancel(String taskId) {
        cancelObservable.cancel(taskId);
    }

    public synchronized void cancel(){
        this.cancel(getId());
    }

    public synchronized void cancelAll() {
        cancelObservable.cancelAll();
    }

    public void setTaskAutoCanceled(boolean autoCanceled) {
        cancelObservable.setTaskAutoCanceled(autoCanceled);
    }

}
