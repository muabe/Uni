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
public class ExcuteBuilder {
    private UniTask uniTask;

    private boolean isAsync = true;
    private ProgressBuilder progress;
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;
    private boolean skipOnPre = false;
    private UniAop uniAop;
    private boolean isAnnotationMapping = true;

    public ExcuteBuilder(UniTask uniTask){
        this.uniTask = uniTask;
        this.setProgress(uniTask.progress);
        this.setUniInterface(uniTask.getUniInterface());
    }
    public ExcuteBuilder setAsync(boolean async) {
        isAsync = async;
        return this;
    }

    public ExcuteBuilder setProgress(ProgressBuilder progress) {
        this.progress = progress;
        return this;
    }

    public ExcuteBuilder setUniInterface(UniInterface uniInterface) {
        this.uniInterface = uniInterface;
        return this;
    }

    public ExcuteBuilder setUniLoadFail(UniLoadFail uniLoadFail) {
        this.uniLoadFail = uniLoadFail;
        return this;
    }

    public ExcuteBuilder skipOnPre(boolean skipOnPre) {
        this.skipOnPre = skipOnPre;
        return this;
    }

    public ExcuteBuilder setUniAop(UniAop uniAop) {
        this.uniAop = uniAop;
        return this;
    }

    public ExcuteBuilder setAnnotationMapping(boolean annotationMapping) {
        isAnnotationMapping = annotationMapping;
        return this;
    }

    public String excute(){
        if(isAnnotationMapping){
            uniTask.memberMapping();
        }
        if(isAsync) {
            return uniTask.run(progress, uniInterface, uniLoadFail, skipOnPre, uniAop);
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
            return uniTask.refresh(progress, uniLoadFail, uniAop);
        }else{
            uniInterface.onPost();
            return null;
        }
    }
}
