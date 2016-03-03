package com.markjmind.uni.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
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

    protected UniProgress(){
        this.layoutId = -1;
        param = new Store<>();
        hasListener = false;
    }

    public UniProgress(int layoutId){
        this.layoutId = layoutId;
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
            mapper.addAdapter(new LayoutAdapter());
            mapper.inject(LayoutAdapter.class);
            if(layoutId == -1) {
                layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
            }

            LayoutInflater inflater = ((LayoutInflater) finder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = inflater.inflate(layoutId, finder, false);
            mapper.reset(layout, this);
            mapper.addAdapter(new GetViewAdapter());
            mapper.addAdapter(new OnClickAdapter());
            mapper.addAdapter(new ParamAdapter(param));
            mapper.injectWithout(LayoutAdapter.class);
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

    @Override
    public void onStart(View layout, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onDestroy(View layout, boolean attach) {

    }
}
