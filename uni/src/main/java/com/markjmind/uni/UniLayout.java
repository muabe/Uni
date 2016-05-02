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

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniLayout extends FrameLayout implements UniInterface{

    private View view;
    private UniTask uniTask;
    private ViewGroup layout;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;



    public UniLayout(Context context) {
        super(context);
        layout = new FrameLayout(context);
        this.addView(layout);
    }

    public UniLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        layout = new FrameLayout(context);
        this.addView(layout);
    }

    public UniLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layout = new FrameLayout(context);
        this.addView(layout);
    }

    void init(UniTask task){
        setUniTask(task);
        uniTask.mapper.addAdapter(new ParamAdapter(uniTask.param));
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                uniTask.getCancelObservable().setAttached(true);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                uniTask.param.clear();
                uniTask.progress.param.clear();
                uniTask.getCancelObservable().setAttached(false);
                uniTask.getCancelObservable().cancelAll();
            }
        });
    }

    void setUniTask(UniTask uniTask){
        this.uniTask = uniTask;
        mapper = uniTask.mapper;
        param = uniTask.param;
        progress = uniTask.progress;
        this.uniTask.progress.setParents(this);
    }


    void setView(View view) {
        this.removeAllViews();
        this.view = view;
        if(this.view !=null) {
            this.addView(this.view);
            this.setLayoutParams(this.view.getLayoutParams());
        }
    }

    /*************************************************** 공통 *********************************************/

    public void excute(){
        uniTask.excute();
    }

    public void excute(UniInterface uniInterface){
        uniTask.excute(uniInterface);
    }

    public void cancel(String id) {
        uniTask.cancel(id);
    }

    public void cancelAll() {
        uniTask.cancelAll();
    }

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
