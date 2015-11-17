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
    protected LoadViewListener loadViewListener;
    private int loadCount = 0;

    public LoadViewController(){
        this.layoutId = -1;
        this.enable = true;
    }

    protected void setLoadView(int layoutId, LoadViewListener loadViewListener){
        this.layoutId = layoutId;
        this.loadViewListener = loadViewListener;
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

    protected boolean isShow(ViewGroup frame){
        if(loadView!=null){
            if(frame.indexOfChild(loadView)>0){
                return true;
            }
        }
        return false;
    }

    /**
     * frame에 있는 LoadView를 add하여 화면에 보이게 한다.
     * @param requestCode
     * @param frame
     */
    protected synchronized void show(int requestCode, ViewGroup frame){
        if(isEnable()) {
            loadCount++;
            if (!isShow(frame)) {
                Context context = frame.getContext();
                loadView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
                loadView.setClickable(true);
                frame.addView(loadView); //로딩뷰 띄우기
            }
            if (loadViewListener != null) {
                loadViewListener.loadCreate(requestCode, loadView);
            }
        }
    }

    protected synchronized void onDestroy(int requestCode, ViewGroup parents){
        if(--loadCount==0) {
            parents.removeView(loadView);
            loadView = null;
        }
        if(loadViewListener !=null){
            loadViewListener.loadDestroy(requestCode);
        }

    }
}
