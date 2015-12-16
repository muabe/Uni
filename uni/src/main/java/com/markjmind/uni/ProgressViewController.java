package com.markjmind.uni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by codemasta on 2015-10-23.
 */
class ProgressViewController {
    protected int layoutId;
    protected View progressView;
    protected boolean enable;
    protected ProgressViewListener progressViewListener;
    private int loadCount = 0;

    public ProgressViewController(){
        this.layoutId = -1;
        this.enable = true;
    }

    protected void setProgressView(int layoutId, ProgressViewListener progressViewListener){
        this.layoutId = layoutId;
        this.progressViewListener = progressViewListener;
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
        if(progressView !=null){
            if(frame.indexOfChild(progressView)>0){
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
                progressView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
                progressView.setClickable(true);
                frame.addView(progressView); //로딩뷰 띄우기
            }
            if (progressViewListener != null) {
                progressViewListener.onStart(requestCode, progressView);
            }
        }
    }

    protected synchronized void onDestroy(int requestCode, ViewGroup parents){
        if(progressViewListener !=null){
            progressViewListener.onDestroy(requestCode, progressView);
        }
        if(--loadCount==0) {
            parents.removeView(progressView);
            progressView = null;
        }
    }
}
