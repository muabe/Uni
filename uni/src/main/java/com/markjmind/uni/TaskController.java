package com.markjmind.uni;

import com.markjmind.uni.progress.ProgressBuilder;
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
    private ProgressBuilder progress;
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;
    private boolean skipOnPre = false;
    private UniAop uniAop;
    private boolean isAnnotationMapping = false;
    private UniUncaughtException uncaughtException;


    void init(UniTask uniTask, boolean isAnnotationMapping){
        this.uniTask = uniTask;
        this.setProgress(uniTask.progress);
        this.setUniInterface(uniTask.getUniInterface());
        this.isAnnotationMapping = isAnnotationMapping;
    }

    public TaskController setAsync(boolean async) {
        isAsync = async;
        return this;
    }

    public TaskController setProgress(ProgressBuilder progress) {
        this.progress = progress;
        return this;
    }

    public TaskController setUniInterface(UniInterface uniInterface) {
        this.uniInterface = uniInterface;
        return this;
    }

    public TaskController setUniLoadFail(UniLoadFail uniLoadFail) {
        this.uniLoadFail = uniLoadFail;
        return this;
    }

    public TaskController skipOnPre(boolean skipOnPre) {
        this.skipOnPre = skipOnPre;
        return this;
    }

    public TaskController setUniUncaughtException(UniUncaughtException uncaughtException){
        this.uncaughtException = uncaughtException;
        return this;
    }

    public UniUncaughtException getUniUncaughtException(){
        return uncaughtException;
    }

    public TaskController setUniAop(UniAop uniAop) {
        this.uniAop = uniAop;
        return this;
    }

    public void cancel(String taskId){
        uniTask.cancel(taskId);
    }

    public void cancelAll(){
        uniTask.cancelAll();
    }

    public String excute(){
        if(isAnnotationMapping){
            uniTask.memberMapping();
        }
        if(isAsync) {
            return uniTask.run(progress, uniInterface, uniLoadFail, skipOnPre, uniAop, uncaughtException);
        }else{
            if(!skipOnPre){
                uniInterface.onPre();
            }
            uniInterface.onPost();
            return null;
        }
    }

    public String refresh(){
        if(isAsync) {
            return uniTask.refresh(progress, uniLoadFail, uniAop, uncaughtException);
        }else{
            uniInterface.onPost();
            return null;
        }
    }
}
