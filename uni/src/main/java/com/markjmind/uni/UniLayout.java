package com.markjmind.uni;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.aop.CancelAop;
import com.markjmind.uni.thread.aop.UniAop;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniLayout extends FrameLayout implements UniInterface{

    private View view;
    private UniTask uniTask;
    private ViewGroup frameLayout;
    public Store<?> param;
    public ProgressBuilder progressBuilder = new ProgressBuilder();
    private UniAop aop = new UniAop();

    public UniLayout(Context context) {
        super(context);
        init(context);
    }

    public UniLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UniLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        frameLayout = new FrameLayout(context);
        isInEditMode();
        super.addView(frameLayout);
        this.progressBuilder.setParents(this);
        param = new Store<>();

        uniTask = new UniTask(true);
        uniTask.initAtrribute(this, this);
        uniTask.setBindInfo(this, this, null, null);

        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                uniTask.getCancelObservable().setAttached(true);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                param.clear();
                progressBuilder.param.clear();
                uniTask.getCancelObservable().setAttached(false);
                uniTask.getCancelObservable().cancelAll();
            }
        });
    }

//    void setUniTask(UniTask task, Store<?> par, ProgressBuilder pro){
//        this.uniTask = task;
//        this.param = par;
//        this.progressBuilder = pro;
//        pro.setParents(this);
//        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//                uniTask.getCancelObservable().setAttached(true);
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//                param.clear();
//                progressBuilder.param.clear();
//                uniTask.getCancelObservable().setAttached(false);
//                uniTask.getCancelObservable().cancelAll();
//            }
//        });
//    }

    void setUniTask(UniTask task){
        this.uniTask = task;
    }

    void syncAttribute(Store<?> par, ProgressBuilder pro){
        this.param = par;
        this.progressBuilder = pro;
    }

    public void setAsync(boolean isAsync){
        this.uniTask.setAsync(isAsync);
    }

    public boolean isAsync(){
        return this.uniTask.isAsync();
    }

    UniTask getUniTask(){
        return uniTask;
    }

    public void setView(View view) {
        this.removeAllViews();
        this.view = view;
        if(view!=null) {
            super.addView(this.view);
            if(this.getLayoutParams()==null) {
                this.setLayoutParams(this.view.getLayoutParams());
            }
        }
    }

    public View getView(){
        return this.view;
    }
//    public void bind(UniTask uniTask){
//        uniTask.mapper.setInjectParents(UniTask.class);
//        uniTask.setEnableMapping(true);
//        uniTask.syncUniLayout(this, uniTask.param, uniTask.progressBuilder, uniTask, uniTask.getUniInterface(), null);
//    }

    /*************************************************** BootStrap Builder관련 *********************************************/
    public FragmentBuilder getBuilder(Activity activity){
        return FragmentBuilder.getBuilder(activity);
    }

    public FragmentBuilder getBuilder(UniFragment uniFragment){
        return FragmentBuilder.getBuilder(uniFragment);
    }

    /*************************************************** execute 관련 *********************************************/
    public void setCancelAop(CancelAop cancelAop){
        aop.setCancelAop(cancelAop);
    }

    public UniAop getAop(){
        return aop;
    }

    public TaskController getTask(){
        return uniTask.getTask();
    }

    /*************************************************** UniTask Interface 관련 *********************************************/
    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {

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

    public UniProgress getProgeress(){
        return progressBuilder.get();
    }

    public void setProgress(int mode, UniProgress pro){
        progressBuilder.set(mode, pro);
    }
}
