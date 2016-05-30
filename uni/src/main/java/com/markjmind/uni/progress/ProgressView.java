/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.progress;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-24
 */
class ProgressView implements ProgressBuilder.ProgressInterface {
    private ViewGroup progressLayout;
    private boolean isShowing;
    private ViewGroup parents;

    public ProgressView(ViewGroup layout, ViewGroup parents){
        reset(layout, parents);
    }

    void reset(ViewGroup layout, ViewGroup parents){
        this.progressLayout = layout;
        this.parents = parents;
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public synchronized void show(View view) {
        if(!isShowing) {
            isShowing = true;
            if(view!=null) {
                progressLayout.addView(view);
            }
            parents.addView(progressLayout);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(progressLayout, View.ALPHA, 0f, 1f);
            alpha.setDuration(300);
            AnimatorSet set = new AnimatorSet();
            set.play(alpha);
            alpha.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(progressLayout!=null){
                        progressLayout.setAlpha(1f);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    if(progressLayout!=null){
                        progressLayout.setAlpha(1f);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }
    }

    @Override
    public synchronized void dismiss() {
        if(isShowing) {
            if(progressLayout !=null) {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(progressLayout, View.ALPHA, 1f, 0f);
                alpha.setDuration(300);
                AnimatorSet set = new AnimatorSet();
                set.play(alpha);
                alpha.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressLayout.removeAllViews();
                        parents.removeView(progressLayout);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        progressLayout.removeAllViews();
                        parents.removeView(progressLayout);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                set.start();
            }
            isShowing = false;
        }
    }

    @Override
    public int getMode() {
        return UniProgress.VIEW;
    }
}
