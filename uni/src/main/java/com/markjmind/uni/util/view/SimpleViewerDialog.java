package com.markjmind.uni.util.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.markjmind.uni.Jwc;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2015-12-15
 */

public class SimpleViewerDialog extends Dialog {

    private ViewerBuilder viewerBuilder;
    private OnDismissListener onDissmiss;
    private Viewer viewer;

    public SimpleViewerDialog(Context context, Class<? extends Viewer> viewerClass, int requestCode) {
        super(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        setFullSize(true);
        viewerBuilder = Viewer.build(viewerClass, this).setRequestCode(requestCode);
    }

    public SimpleViewerDialog(Context context, Class<? extends Viewer> viewerClass){
        this(context, viewerClass, Viewer.REQUEST_CODE_NONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout frame = new LinearLayout(getContext());
        frame.setGravity(Gravity.CENTER);
        setContentView(frame);
        viewer = viewerBuilder.setPreLayout(true).change(frame);
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

    public interface OnDismissListener{
        public void onDismiss(int code, Viewer viewer, Object... obj);
    }

    /**
     * 다이얼로그 사이즈를 full사이즈로 한다.
     *
     * @param isFullSize
     */
    public void setFullSize(boolean isFullSize) {
        if (isFullSize) {
            getContext().setTheme(android.R.style.Theme_Holo_NoActionBar);
        } else {
            getContext().setTheme(android.R.style.Theme_Holo_Dialog_NoActionBar);
        }
    }

    /**
     * 테마를 설정한다.
     *
     * @param style
     */
    public void setTheme(int style) {
        getContext().setTheme(style);
    }


}

