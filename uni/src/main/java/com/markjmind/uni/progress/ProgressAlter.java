/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-24
 */
class ProgressAlter extends AlertDialog implements ProgressBuilder.ProgressInterface {
    private ViewGroup progressLayout;
    private boolean isShowing;

    public ProgressAlter(Context context, ViewGroup layout) {
        super(context);
        this.progressLayout = layout;
        isShowing = false;
    }

    public ProgressAlter(Context context, ViewGroup layout, int theme) {
        super(context, theme);
        this.progressLayout = layout;
        isShowing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(progressLayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        this.setCancelable(false);
    }

    public synchronized void show(View view) {
        isShowing = true;
        if(view!=null) {
            progressLayout.addView(view);
        }
        super.show();
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public synchronized void dismiss() {
        if(progressLayout !=null) {
            progressLayout.removeAllViews();
        }
        isShowing = false;
        super.dismiss();
    }

    @Override
    public int getMode() {
        return UniProgress.DIALOG;
    }

}
