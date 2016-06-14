package com.markjmind.uni;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.aop.UniAop;

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

    public UniAsyncTask(){
        param = new Store<>();
        progress = new ProgressBuilder();
    }

    public UniAsyncTask(UniLayout uniLayout){
        inheritFault(uniLayout.getUniTask().getUniInterface());
        progress = uniLayout.progress;
        param = uniLayout.param;
    }

    public UniAsyncTask(UniFragment uniFragment){
        this(uniFragment.getUniLayout());
    }

    public UniAsyncTask(UniDialog uniDialog){
        this(uniDialog.getUniLayout());
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
    public void cancel(String id) {
        if(uniTask!=null) {
            uniTask.cancel(id);
        }
    }

    public void cancelAll() {
        if(uniTask!=null) {
            uniTask.cancelAll();
        }
    }

    public void setTaskAutoCanceled(boolean autoCanceled) {
        if(uniTask!=null) {
            uniTask.setTaskAutoCanceled(autoCanceled);
        }
    }

    public boolean isFinished(String task){
        if(uniTask==null) {
            return false;
        }
        return uniTask.isFinished(task);
    }

    public boolean isRunning(String task){
        if(uniTask==null) {
            return false;
        }
        return uniTask.isRunning(task);
    }


    /*************************************************** 실행 관련 *********************************************/
    public void post(){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
        }
        uniTask.post();
    }

    public String excute(){
        return this.excute(null);
    }

    public String excute(UniAop uniAop){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
            return uniTask.run(progress, this, null, uniAop);
        }else{
            return uniTask.excute(progress);
        }
    }

    public String excute(boolean isAsync){
        return this.excute(isAsync, null);
    }

    public String excute(boolean isAsync, UniAop uniAop){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
            return uniTask.run(progress, this, null, uniAop);
        }else{
            return uniTask.excute(progress);
        }
    }
}
