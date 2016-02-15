package com.markjmind.uni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.thread.DetachedObservable;
import com.markjmind.uni.thread.InnerUniTask;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-29
 */
public class Uni implements View.OnAttachStateChangeListener{
    private View view;
    private Context context;

    private ViewGroup layout;
    private UniInterface uniInterface;
    private Mapper mapper;
    private boolean isMapping;
    private DetachedObservable detachedObservable;

    private void init(View view){
        this.view = view;
        this.context = view.getContext();
        isMapping = false;
        detachedObservable = new DetachedObservable();
//        addOnAttachStateChangeListener(this);
    }

    protected Uni(View view) {
        init(view);
        onCreateView(null);
    }

    protected Uni(UniFragment frag) {
        view = frag.getUniView();
        onCreateView(null);
    }

    protected Uni(View view, Object mappingObject, ViewGroup container) {
        init(view);
        mappingLayout(new Mapper(view, mappingObject), container);
    }

    public static Uni option(View view, UniInterface uniInterface){
        Uni uni = new Uni(view);
        uni.setUniInterface(uniInterface);
        return uni;
    }

    public static Uni option(UniView view){
        Uni uni = new Uni(view, view, view);
        uni.setUniInterface(view);
        return uni;
    }





    private void onCreateView(ViewGroup layout){
//        if(layout==null) {
//            mappingLayout(new Mapper(this, this), this);
//        }else{
//            setView(layout);
//        }
    }

//    protected void setView(ViewGroup layout) {
//        this.layout = layout;
//        if(this.layout !=null) {
//            this.addView(this.layout);
//            this.setLayoutParams(this.layout.getLayoutParams());
//        }
//    }

    protected void mappingLayout(Mapper mapper, ViewGroup container){
//        if(Mapper.hasLayout(uniFragment.getClass())) {
        this.mapper = mapper;
        if(layout==null) {
            int layoutId = mapper.injectLayout();
            LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = (ViewGroup) inflater.inflate(layoutId, container, false);
        }
//        setView(layout);
    }

    protected void setUniInterface(UniInterface uniInterface){
        this.uniInterface = uniInterface;
    }

    public ViewGroup getLayout(){
        return this.layout;
    }

    public String excute(int requestCode, UniInterface uniInterface){
        uniInterface.onBind(requestCode, null);
        if(!isMapping) {
            mapper.injectAll();
            isMapping = true;
        }
        InnerUniTask task = new InnerUniTask(requestCode, detachedObservable, uniInterface);
        detachedObservable.add(task);
        task.execute();
        return task.getId();
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        detachedObservable.cancelAll();
    }

}
