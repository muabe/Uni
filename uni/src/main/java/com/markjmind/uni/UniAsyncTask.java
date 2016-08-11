package com.markjmind.uni;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-04-29
 */
public abstract class UniAsyncTask implements UniInterface{
    private UniTask uniTask;
    public Store<?> param;
    public ProgressBuilder progress;
    private UniInterface uniInterface;
    private UniUncaughtException uncaughtException;
    private String taskId;
    private TaskController taskController;

    public UniAsyncTask(){
        param = new Store<>();
        progress = new ProgressBuilder();
        taskController = new TaskController();
    }

    public UniAsyncTask(UniLayout uniLayout){
        inheritFault(uniLayout.getUniTask().getUniInterface());
        progress = uniLayout.progress;
        param = uniLayout.param;
        this.uncaughtException = uniLayout.getTask().getUniUncaughtException();
        taskController = new TaskController();
    }

    public UniAsyncTask(UniFragment uniFragment){
        this(uniFragment.getUniLayout());
        this.uncaughtException = uniFragment.getTask().getUniUncaughtException();
        taskController = new TaskController();
    }

    public UniAsyncTask(UniDialog uniDialog){
        this(uniDialog.getUniLayout());
        this.uncaughtException = uniDialog.getTask().getUniUncaughtException();
        taskController = new TaskController();
    }

    void inheritFault(UniInterface uniInterface){
        this.uniInterface = uniInterface;
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
        if(uniInterface!=null){
            uniInterface.onException(e);
        }
    }

    @Override
    public void onCancelled(boolean attached) {
        if(uniInterface!=null){
            uniInterface.onCancelled(attached);
        }
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/
    public void cancel() {
        if(uniTask!=null) {
            uniTask.getTask().cancel(taskId);
        }
    }

    public void cancelAll() {
        if(uniTask!=null) {
            uniTask.getTask().cancelAll();
        }
    }

    public void setTaskAutoCanceled(boolean autoCanceled) {
        if(uniTask!=null) {
            uniTask.getTask().setTaskAutoCanceled(autoCanceled);
        }
    }

    public boolean isRunning(){
        if(uniTask==null) {
            return false;
        }
        return uniTask.getTask().isRunning(taskId);
    }


    /*************************************************** 실행 관련 *********************************************/

    public String excute(){
        return getTask().setProgress(progress)
                .setUniUncaughtException(uncaughtException)
                .execute();
    }

    public TaskController getTask(){
        if(taskController==null){
            taskController = new TaskController();
        }
        taskController.init(null, this);
        return taskController;
    }


}
