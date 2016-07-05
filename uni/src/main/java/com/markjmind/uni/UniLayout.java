package com.markjmind.uni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
import com.markjmind.uni.progress.ProgressBuilder;
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
    public UniMapper mapper;
    public Store<?> param;
    public ProgressBuilder progress = new ProgressBuilder();
    private UniAop aop = new UniAop();

    public UniLayout(Context context) {
        super(context);
        frameLayout = new FrameLayout(context);
        super.addView(frameLayout);
        this.progress.setParents(this);
        param = new Store<>();
    }

    public UniLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        frameLayout = new FrameLayout(context);
        super.addView(frameLayout);
        this.progress.setParents(this);
        param = new Store<>();
    }

    public UniLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        frameLayout = new FrameLayout(context);
        super.addView(frameLayout);
        this.progress.setParents(this);
        param = new Store<>();
    }

    void init(UniTask task, Store<?> par, ProgressBuilder pro){
        this.uniTask = task;
        mapper = uniTask.mapper;
        if(par!=null){
            this.param = par;
        }
        if(pro!=null) {
            this.progress = pro;
            pro.setParents(this);
        }

        uniTask.mapper.addSubscriptionOnInit(new ParamAdapter(param));
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                uniTask.getCancelObservable().setAttached(true);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                param.clear();
                progress.param.clear();
                uniTask.getCancelObservable().setAttached(false);
                uniTask.getCancelObservable().cancelAll();
            }
        });
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

    protected void setLayout(View view) {
        this.removeAllViews();
        this.view = view;
        if(this.view !=null) {
            super.addView(this.view);
            this.setLayoutParams(this.view.getLayoutParams());
        }
    }

    public void bind(UniTask uniTask){
        uniTask.mapper.setInjectParents(UniTask.class);
        uniTask.setAnnotationMapping(true);
        uniTask.syncUniLayout(this, uniTask.param, uniTask.progress, uniTask, uniTask.getUniInterface(), null);
    }

    /*************************************************** excute 관련 *********************************************/
    public void setCancelAop(CancelAop cancelAop){
        aop.setCancelAop(cancelAop);
    }

    public UniAop getAop(){
        return aop;
    }

    public TaskController getTask(){
        if(uniTask==null){
            uniTask = new UniTask(true);
            uniTask.mapper.setInjectParents(UniLayout.class);
            uniTask.syncUniLayout(this, param, progress, this, this, null);
        }
        return uniTask.getTask();
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/

    public boolean isFinished(String task){
        return uniTask.isFinished(task);
    }

    public boolean isRunning(String task){
        return uniTask.isRunning(task);
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
}
