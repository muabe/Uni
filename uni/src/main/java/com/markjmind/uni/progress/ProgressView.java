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
import android.animation.ValueAnimator;
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
    private AnimatorSet inAnimation;
    private AnimatorSet outAnimation;

    public ProgressView(ViewGroup layout, ViewGroup parents){
        reset(layout, parents);
        AnimatorSet setIn = new AnimatorSet();
        setIn.play(defaultInAnimation());
        this.setInAnimation(setIn);
        AnimatorSet setOut = new AnimatorSet();
        setOut.play(defaultOutAnimation());
        this.setInAnimation(setIn);
        this.setOutAnimation(setOut);
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
            if(inAnimation!=null) {
                inAnimation.start();
            }else{
                if(progressLayout!=null){
                    progressLayout.setAlpha(1f);
                }
            }
        }
    }

    @Override
    public synchronized void dismiss() {
        if(isShowing) {
            if(progressLayout !=null) {
                if(outAnimation!=null){
                    outAnimation.start();
                }else{
                    progressLayout.removeAllViews();
                    parents.removeView(progressLayout);
                }

            }
            isShowing = false;
        }
    }

    @Override
    public int getMode() {
        return UniProgress.VIEW;
    }

    public void setInAnimation(AnimatorSet inAnimation){
        if(inAnimation==null){
            this.inAnimation = null;
        }else {
            this.inAnimation = inAnimation;
            this.inAnimation.addListener(new Animator.AnimatorListener() {
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
        }
    }

//    public void setInAnimation(ValueAnimator inAnimation){
//        if(inAnimation==null){
//            this.inAnimation = null;
//        }else {
//            AnimatorSet set = new AnimatorSet();
//            set.play(inAnimation);
//            this.setInAnimation(set);
//        }
//    }

    public void setOutAnimation(AnimatorSet outAnimation){
        if(outAnimation==null){
            this.outAnimation = null;
        }else {
            this.outAnimation = outAnimation;
            this.outAnimation.addListener(new Animator.AnimatorListener() {
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
        }
    }

//    public void setOutAnimation(ValueAnimator outAnimation){
//        if(outAnimation==null){
//            this.outAnimation = null;
//        }else {
//            AnimatorSet set = new AnimatorSet();
//            set.play(outAnimation);
//            this.setOutAnimation(set);
//        }
//    }

    private ValueAnimator defaultInAnimation(){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(progressLayout, View.ALPHA, 0f, 1f);
        alpha.setDuration(250);
        return alpha;
    }

    private ValueAnimator defaultOutAnimation(){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(progressLayout, View.ALPHA, 1f, 0f);
        alpha.setDuration(250);
        return alpha;
    }


}
