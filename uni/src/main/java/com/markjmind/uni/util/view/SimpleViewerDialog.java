package com.markjmind.uni.util.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.markjmind.uni.Jwc;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;

import java.util.HashMap;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2015-12-15
 */

public class SimpleViewerDialog extends Dialog {

    private ViewerBuilder viewerBuilder;
    private OnDismissListener onDissmiss;
    private Viewer viewer;
    private boolean isFullSise;

    public interface OnDismissListener{
        public void onDismiss(int code, Viewer viewer, Object... obj);
    }

    public interface OnButtonClickListener{
        public void onClick(SimpleViewerDialog dialog, View view);
    }


    public SimpleViewerDialog(Context context, Class<? extends Viewer> viewerClass, int requestCode) {
        super(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        setFullSize(true);
        if(viewerClass!=null) {
            viewerBuilder = Viewer.build(viewerClass, this).setRequestCode(requestCode);
        }
        buttonMap = new HashMap<>();
    }

    public SimpleViewerDialog(Context context, Class<? extends Viewer> viewerClass){
        this(context, viewerClass, Viewer.REQUEST_CODE_NONE);
    }

    public SimpleViewerDialog(Context context){
        this(context, null, Viewer.REQUEST_CODE_NONE);

    }

    public void setViewer(Class<? extends Viewer> viewerClass, int requestCode){
        viewerBuilder = Viewer.build(viewerClass, this).setRequestCode(requestCode);
    }

    public void setViewer(Class<? extends Viewer> viewerClass){
        this.setViewer(viewerClass, Viewer.REQUEST_CODE_NONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout frame = new LinearLayout(getContext());
        frame.setGravity(Gravity.CENTER);
        setContentView(frame);
        viewer = viewerBuilder.setPreLayout(true).change(frame);
        if(buttonMap.size()>0) {
            Integer[] keys = (Integer[]) buttonMap.keySet().toArray(new Integer[0]);
            for (Integer key : keys) {
                final OnButtonClickListener ocl = buttonMap.get(key);
                findViewById(key).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ocl.onClick(SimpleViewerDialog.this, v);
                    }
                });
            }
        }
    }


    public ViewerBuilder getBuild(){
        return viewerBuilder;
    }

    /**
     * 다른 Viewer로 넘기는 파라미터를 설정한다.
     * @param key Parameter Key
     * @param value Parameter Value
     */
    public SimpleViewerDialog addParam(String key, Object value){
        viewerBuilder.addParam(key, value);
        return this;
    }

    public void setOnDismissListener(OnDismissListener onDissmiss){
        this.onDissmiss = onDissmiss;
    }

    public void dismiss(int code, Object... obj){
        if(onDissmiss != null){
            onDissmiss.onDismiss(code, viewer, obj);
        }
        super.dismiss();
    }

    /**
     * 테마를 설정한다.
     *
     * @param style
     */
    public void setTheme(int style) {
        getContext().setTheme(style);
    }

    /**
     * 다이얼로그 사이즈를 full사이즈로 한다.
     *
     * @param isFullSize
     */
    public void setFullSize(boolean isFullSize) {
        this.setFullSize(true, false);
    }

    public void setFullSize(boolean isFullSize, boolean isFullScrean){
        if (isFullSize) {
            if(isFullScrean){
                setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
            }else {
                setTheme(android.R.style.Theme_Holo_NoActionBar);
            }
        } else {
            if(isFullScrean){
                setTheme(android.R.style.Theme_Holo_Dialog_NoActionBar);
            }else {
                setTheme(android.R.style.Theme_Holo_Dialog_NoActionBar);
            }

        }
    }

    public Viewer getViewer(){
        return viewer;
    }

    public void setWidth(int width_dp){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)viewer.getLayout().getLayoutParams();
        int maxWidth = Jwc.getWindowWidth(getContext())-lp.leftMargin-lp.rightMargin;
        int width = Jwc.getDp(getContext(), width_dp);
        if(width > maxWidth){
            width = maxWidth;
        }
        lp.width = width;
        viewer.getView().setLayoutParams(lp);
    }

    public void setHeight(int height_dp){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)viewer.getLayout().getLayoutParams();
        int maxHeight = Jwc.getWindowWidth(getContext())-lp.leftMargin-lp.rightMargin;
        int height = Jwc.getDp(getContext(), height_dp);
        if(height > maxHeight){
            height = maxHeight;
        }
        lp.height = height;
        viewer.getView().setLayoutParams(lp);
    }


    HashMap<Integer, OnButtonClickListener> buttonMap;

    public void makeButton(int resourceId, OnButtonClickListener onButtonClickListener){
        buttonMap.put(resourceId, onButtonClickListener);
    }

}

