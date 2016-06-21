package com.markjmind.uni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
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
    public Mapper mapper;
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
        setUniTask(task);
        if(par!=null){
            this.param = par;
        }
        if(pro!=null) {
            this.progress = pro;
            pro.setParents(this);
        }

        uniTask.mapper.addAdapter(new ParamAdapter(param));
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

    void setUniTask(UniTask uniTask){
        this.uniTask = uniTask;
        mapper = uniTask.mapper;
    }

    UniTask getUniTask(){
        return uniTask;
    }

    protected void setFrameLayout(View view) {
        this.removeAllViews();
        this.view = view;
        if(this.view !=null) {
            super.addView(this.view);
            this.setLayoutParams(this.view.getLayoutParams());
        }
    }

    public void bind(UniTask uniTask){
        uniTask.syncUniLayout(this, uniTask.param, uniTask.progress, uniTask, uniTask.getUniInterface(), null);
    }

    /*************************************************** excute 관련 *********************************************/
    public void setCancelAop(CancelAop cancelAop){
        aop.setCancelAop(cancelAop);
    }

    public UniAop getAop(){
        return aop;
    }

    public void post(){
        if(uniTask==null){
            UniTask task = new UniTask();
            task.syncUniLayout(this, param, progress, this, this, null);
        }
        uniTask.post();
    }

    public String excute(){
        if(uniTask==null){
            UniTask task = new UniTask();
            task.syncUniLayout(this, param, progress, this, this, null);
        }
        return uniTask.excute(progress, getAop());
    }

    public String excute(boolean isAsync){
        if(isAsync){
            return excute();
        }else{
            post();
            return null;
        }
    }

    public String refresh(){
        return uniTask.refresh(isAsync(), getAop());
    }

    public String refresh(boolean isAsync){
        return uniTask.refresh(isAsync, getAop());
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/
    public void cancel(String id) {
        uniTask.cancel(id);
    }

    public void cancelAll() {
        uniTask.cancelAll();
    }

    public void setTaskAutoCanceled(boolean autoCanceled) {
        uniTask.setTaskAutoCanceled(autoCanceled);
    }

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
