package com.markjmind.uni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by codemasta on 2015-10-23.
 */
class LoadViewController {
    protected int layoutId;
    protected View loadView;
    protected boolean enable;
    protected UpdateListener updateListener;

    public LoadViewController(){
        this.layoutId = -1;
        this.enable = true;
    }

    protected void setLoadView(int layoutId, UpdateListener updateListener){
        this.layoutId = layoutId;
        this.updateListener = updateListener;
    }

    protected void setEnable(boolean enable){
        this.enable = enable;
    }

    protected boolean isEnable(){
        if(layoutId==-1){
            return false;
        }
        return this.enable;
    }

    /**
     * frame에 있는 LoadView를 add하여 화면에 보이게 한다.
     * @param requestCode
     * @param frame
     */
    protected void show(int requestCode, ViewGroup frame){
        Context context = frame.getContext();
        loadView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
        loadView.setClickable(true);
        frame.addView(loadView); //로딩뷰 띄우기
        if(updateListener!=null){
            updateListener.onCreate(requestCode, loadView);
        }
    }

    protected void onDestroy(int requestCode, ViewGroup parents){
        parents.removeView(loadView);
        loadView = null;
        if(updateListener!=null){
            updateListener.onDestroy(requestCode);
        }
    }
}
