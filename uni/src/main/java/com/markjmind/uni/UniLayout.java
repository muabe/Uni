package com.markjmind.uni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniLayout extends FrameLayout{

    private View view;
    private UniTask uniTask;
    private ViewGroup layout;


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
        this.uniTask.progress.setParents(this);
    }


    void setView(View layout) {
        this.removeAllViews();
        this.view = layout;
        if(layout !=null) {
            this.addView(layout);
            this.setLayoutParams(this.view.getLayoutParams());
        }
    }

}
