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

    public UniAsyncTask(){
        param = new Store<>();
        progress = new ProgressBuilder();
    }

    public UniAsyncTask(UniLayout uniLayout){
        setUniTask(uniLayout.getUniTask());
        progress = uniLayout.progress;
        param = uniLayout.param;
    }

    public UniAsyncTask(UniFragment uniFragment){
        this(uniFragment.getUniLayout());
    }

    public UniAsyncTask(UniDialog uniDialog){
        this(uniDialog.getUniLayout());
    }

    void setUniTask(UniTask uniTask){
        this.uniTask = uniTask;
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

    /*************************************************** 실행 관련 *********************************************/
    public void post(){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
        }
        uniTask.post();
    }

    public String excute(){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
            return uniTask.run(progress, this, null);
        }else{
            return uniTask.excute(progress);
        }
    }

    public String excute(boolean isAsync){
        if(uniTask==null){
            uniTask = new UniTask();
            uniTask.init(this, this);
            return uniTask.run(progress, this, null);
        }else{
            return uniTask.excute(progress);
        }
    }
}
