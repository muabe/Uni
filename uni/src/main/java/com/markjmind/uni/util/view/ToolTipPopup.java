package com.markjmind.uni.util.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-04
 */
public class ToolTipPopup extends Dialog{
    private FrameLayout frame;
    private FrameLayout contentLayout;
    private View contentView;
    private Animation animIn;
    private Animation animOut;
    private View onClickView;

     public ToolTipPopup(Context context, int layoutId, View onClickView) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.onClickView = onClickView;
        this.contentView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
    }

    protected void setMainContentView(View contentView){
        this.contentView = contentView;
    }

    public void setAnimationIn(Animation animIn){
        this.animIn = animIn;
    }
    public void setAnimationOut(Animation animOut){
        this.animOut = animOut;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frame=new FrameLayout(getContext());
        frame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        super.setContentView(frame);


        FrameLayout closeLayout = new FrameLayout(getContext());
        closeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        contentLayout = new FrameLayout(getContext());
        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.addView(contentView);
        closeLayout.setClickable(true);
        frame.setClickable(true);
        frame.addView(closeLayout);
        contentView.setVisibility(View.INVISIBLE);
        contentView.setClickable(true);
        frame.addView(contentLayout);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setVisibility(View.VISIBLE);
                if(animIn!=null){
                    contentView.setAnimation(animIn);
                }
                int windowWidth = frame.getWidth();
                int windowHeight = frame.getHeight();
                int width = onClickView.getWidth();
                int height = onClickView.getHeight();
                int[] location = new int[2];
                onClickView.getLocationInWindow(location);
                location[1] -= (getStatusBarHeight()-height);

                if (windowWidth - location[0] - contentLayout.getWidth() < 0) {
                    location[0] = windowWidth - contentLayout.getWidth();
                }

                if (windowHeight - location[1] - contentLayout.getHeight()< 0) {
                    location[1] -= (height+contentLayout.getHeight());
                }
                contentLayout.setX(location[0]);
                contentLayout.setY(location[1]);
            }
        });
    }

    public void setPadding(int left, int top, int right, int bottom){
        frame.setPadding(left, top, right, bottom);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result =  getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void dismiss() {
        if(animOut!=null){
            animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    ToolTipPopup.super.dismiss();
                }
            });
            contentView.startAnimation(animOut);
        }else{
            super.dismiss();
        }

    }

    public void setTheme(int style){
        getContext().setTheme(style);
    }
}