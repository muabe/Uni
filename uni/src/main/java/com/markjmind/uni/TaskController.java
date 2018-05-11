package com.markjmind.uni;

import android.os.AsyncTask;
import android.os.Build;

import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.thread.ThreadProcessAdapter;
import com.markjmind.uni.thread.UniMainThread;
import com.markjmind.uni.thread.aop.AopListener;

import java.util.ArrayList;

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
    private ProgressBuilder progressBuilder;
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;
    private UniUncaughtException uncaughtException;
    private UniProgress uniProgress;
    private ArrayList<AopListener> aopListeners = new ArrayList<>();


    public TaskController(UniInterface uniInterface){
        this.uniInterface = uniInterface;
        aopListeners.clear();
    }


    void init(UniTask uniTask, CancelObservable cancelObservable) {
        this.uniTask = uniTask;
        this.cancelObservable = cancelObservable;
        if(this.cancelObservable == null){
            this.cancelObservable = new CancelObservable();
        }
        task = new UniMainThread(this.cancelObservable);
        if(uniTask!=null) {
            this.setProgressBuilder(uniTask.progressBuilder);
        }

    }



    /*********************************************************************************
     * Option 관련
     *********************************************************************************/
    public TaskController setAsync(boolean async) {
        isAsync = async;
        return this;
    }

    public TaskController setProgressBuilder(ProgressBuilder progressBuilder) {
        this.progressBuilder = progressBuilder;
        return this;
    }

    public ProgressBuilder getProgressBuilder(){
        return this.progressBuilder;
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

    public TaskController addAop(AopListener aopListener){
        aopListeners.add(aopListener);
        return this;
    }

    public TaskController removeAop(AopListener aopListener){
        aopListeners.remove(aopListener);
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

    private synchronized String run(ProgressBuilder progressBuilder, UniInterface uniInterface, UniLoadFail uniLoadFail, boolean skipOnPre, UniUncaughtException uncaughtException) {
        if (uniTask!=null) { //UniAsycTask 일 경우는 매핑을 안함
            uniTask.taskBinding();
        }
        if(getStatus().equals(AsyncTask.Status.RUNNING)) {
            return null;
        }else if(getStatus().equals(AsyncTask.Status.FINISHED)){
            task = new UniMainThread(cancelObservable);
        }

        //태스크 옵저버 등록을 위한 initThreadTask
        initThreadTask(progressBuilder, uniInterface, uniLoadFail, skipOnPre, uncaughtException);

        cancelObservable.add(task);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

        return task.getId();
    }

    private void initThreadTask(ProgressBuilder progressBuilder, UniInterface uniInterface, UniLoadFail uniLoadFail, boolean skipOnPre, UniUncaughtException uncaughtException){
        /**ProgressBuilder*/
        if (progressBuilder!=null && progressBuilder.isAble()) {
            task.addTaskObserver(progressBuilder);
        }

        /**UniProgress*/
        if(uniProgress!=null) {
            task.addTaskObserver(uniProgress.getBuilder());
        }

        /**UniLayout*/
        task.addTaskObserver(new ThreadProcessAdapter(uniInterface, uniLoadFail, skipOnPre, aopListeners));

        task.setUIuncaughtException(uncaughtException);
    }

    public void pre(){
        if (uniTask!=null) {
            uniTask.taskBinding();
        }
        uniInterface.onPre();
        if (uniTask!=null) {
            uniTask.bindImport();
        }
    }

    public void post() {
        if (uniTask!=null) {
            uniTask.taskBinding();
        }
        uniInterface.onPost();
        if (uniTask!=null) {
            uniTask.bindImport();
        }
    }

    public void notifyPre() {
        uniInterface.onPre();
    }

    public void notifyPost() {
        uniInterface.onPost();
    }

    private synchronized String refresh(ProgressBuilder progress, UniLoadFail uniLoadFail, UniUncaughtException uncaughtException) {
        cancelAll();
        return run(progress, uniInterface, uniLoadFail, true, uncaughtException);
    }


    public synchronized String execute() {
        if (isAsync) {
            return run(progressBuilder, uniInterface, uniLoadFail, false, uncaughtException);
        } else {
            pre();
            return null;
        }
    }





    public synchronized String reLoad() {
        return refresh(progressBuilder, uniLoadFail, uncaughtException);
    }

    public synchronized String refresh() {
        if (isAsync) {
            return refresh(progressBuilder, uniLoadFail, uncaughtException);
        } else {
            pre();
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
