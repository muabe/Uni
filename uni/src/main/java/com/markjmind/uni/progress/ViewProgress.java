/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

import android.view.View;
import android.view.ViewGroup;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-24
 */
class ViewProgress implements UniProgress.ProgressInterface {
    private ViewGroup progressLayout;
    private boolean isShowing;
    private ViewGroup parents;

    public ViewProgress(ViewGroup layout, ViewGroup parents){
        this.progressLayout = layout;
        this.parents = parents;
    }

    @Override
    public boolean isShow() {
        return isShowing;
    }

    @Override
    public synchronized void show(View view) {
        if(!isShowing) {
            isShowing = true;
            if(view!=null) {
                progressLayout.addView(view);
            }
            parents.addView(progressLayout, parents.getChildCount());
        }
    }

    @Override
    public synchronized void dismiss() {
        if(isShowing) {
            if(progressLayout !=null) {
                progressLayout.removeAllViews();
                parents.removeView(progressLayout);
            }
            isShowing = false;
        }
    }
}
