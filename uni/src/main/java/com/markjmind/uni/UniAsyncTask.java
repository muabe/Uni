package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObservable;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-04-29
 */
public abstract class UniAsyncTask implements UniInterface{
    private UniInterface uniInterface;
    private TaskController taskController;
    private CancelObservable cancelObservable;

    public UniAsyncTask(){
        taskController = new TaskController(this);
        taskController.init(null, cancelObservable);
    }

    public UniAsyncTask(UniLayout uniLayout){
        inheritFault(uniLayout.getUniTask());

    }

    public UniAsyncTask(UniFragment uniFragment){
        this(uniFragment.getUniLayout());
    }

    public UniAsyncTask(UniDialog uniDialog){
        this(uniDialog.getUniLayout());
    }

    public UniAsyncTask(UniTask uniTask){
        inheritFault(uniTask);
    }

    void inheritFault(UniTask uniTask){
        taskController = new TaskController(this);
        this.uniInterface = uniTask.getUniInterface();
        cancelObservable = uniTask.getCancelObservable();

        taskController.init(null, cancelObservable);
        taskController.setUniUncaughtException(uniTask.getTask().getUniUncaughtException());
        taskController.setProgressBuilder(uniTask.progressBuilder);

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
//    public void cancel() {
//        if(taskController!=null) {
//            getTask().cancel();
//        }
//    }
//
//    public void cancelAll() {
//        if(taskController!=null) {
//            getTask().cancelAll();
//        }
//    }
//
//    public void setTaskAutoCanceled(boolean autoCanceled) {
//        if(taskController!=null) {
//            getTask().setTaskAutoCanceled(autoCanceled);
//        }
//    }
//
//    public boolean isRunning(){
//        if(taskController==null) {
//            return false;
//        }
//        return getTask().isRunning();
//    }


    /*************************************************** 실행 관련 *********************************************/

    public String excute(){
        return getTask().execute();
    }

    public TaskController getTask(){
        return taskController;
    }


}
