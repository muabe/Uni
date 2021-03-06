package com.markjmind.uni.progress;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnCheckedChangeAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-19
 */
public class UniProgress implements OnProgressListener{
    public static final int VIEW = 0;
    public static final int DIALOG = 1;

    private int mode;
    public Store<?> param;
    public Mapper mapper;

    private int layoutId;
    protected View layout;
    boolean hasListener;
    private ProgressBuilder.ProgressInterface progressInterface;
    private ProgressBuilder builder;

    protected UniProgress(){
        this.layoutId = -1;
        param = new Store<>();
        hasListener = false;
    }

    public UniProgress(int layoutId){
        this.layoutId = layoutId;
    }

    void setProgressInterface(ProgressBuilder.ProgressInterface progressInterface){
        this.progressInterface = progressInterface;
    }

    public ProgressBuilder getBuilder(){
        return builder;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    View mapperInit(ViewGroup finder, Store<?> param) {
        this.param = param;
        if(hasListener) {
            mapper = new Mapper(finder, this);
            LayoutAdapter layoutAdapter = new LayoutAdapter();
            mapper.inject(layoutAdapter);
            if(layoutId == -1) {
                layoutId = layoutAdapter.getLayoutId();
            }

            LayoutInflater inflater = ((LayoutInflater) finder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = inflater.inflate(layoutId, finder, false);
            mapper.reset(layout, this);
            mapper.inject(new GetViewAdapter(), new OnClickAdapter(), new ParamAdapter(param), new OnCheckedChangeAdapter());
        }else{
            if(layoutId == -1) {

            }
            LayoutInflater inflater = ((LayoutInflater) finder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = inflater.inflate(layoutId, finder, false);
        }
        return layout;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public int getMode(){
        return mode;
    }

    public void onBind(){

    }

    public View getLayout(){
        return layout;
    }

    @Override
    public void onStart(View layout, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onDestroy(View layout, boolean attach) {

    }

    public void setInAnimation(AnimatorSet inAnimation){
        progressInterface.setInAnimation(inAnimation);
    }

    public void setInAnimation(ValueAnimator inAnimation){
        if(inAnimation==null){
            progressInterface.setInAnimation(null);
        }else {
            AnimatorSet set = new AnimatorSet();
            set.play(inAnimation);
            this.setInAnimation(set);
            progressInterface.setInAnimation(set);
        }
    }

    public void setOutAnimation(AnimatorSet outAnimation){
        progressInterface.setOutAnimation(outAnimation);
    }

    public void setOutAnimation(ValueAnimator outAnimation){
        if(outAnimation==null){
            progressInterface.setOutAnimation(null);
        }else {
            AnimatorSet set = new AnimatorSet();
            set.play(outAnimation);
            progressInterface.setOutAnimation(set);
        }
    }

    private ValueAnimator defaultInAnimation(){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(layout, View.ALPHA, 0f, 1f);
        alpha.setDuration(250);
        return alpha;
    }

    private ValueAnimator defaultOutAnimation(){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(layout, View.ALPHA, 1f, 0f);
        alpha.setDuration(250);
        return alpha;
    }

    public void show(){
        if(builder!=null){
            builder.show();
        }
    }

    public void dismiss(){
        if(builder!=null){
            builder.dismiss();
        }
    }
}
