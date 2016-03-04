/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.progress.ProgressBuilder;

import java.lang.reflect.InvocationTargetException;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
public class Uni {
    static UniView createUniView(Context context, final UniBuildInterface uniBuildInterface, ViewGroup container){
        UniView uniView;
        if(uniBuildInterface.getCustomUniView()==null) {
            uniView = new UniView(context);
        }else{
            try {
                uniView = (UniView)(uniBuildInterface.getCustomUniView().getConstructor(Context.class, Mapper.class)
                        .newInstance(context, uniBuildInterface.getMapper()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        uniView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                uniBuildInterface.reset();
            }
        });
        ((UniMapper) uniBuildInterface.getMapper()).reset(uniView, uniBuildInterface.getUniInterface());
        uniView.init(uniBuildInterface.getUniInterface(), uniBuildInterface.getMapper(), uniBuildInterface.getParam(), uniBuildInterface.getProgress());
        if(container==null){
            uniView.injectLayout(uniView);
        }else{
            uniView.injectLayout(container);
        }
        return uniView;
    }

    public static UniView create(Context context, UniTask task, ViewGroup container){
        UniView uniView = new UniView(context);
        task.init(uniView);
        if(container==null){
            uniView.injectLayout(uniView);
        }else{
            uniView.injectLayout(container);
        }
        return uniView;
    }
    public static UniView create(Context context, UniTask task){
        return Uni.create(context, task, null);
    }

    public static void add(ViewGroup parent, UniTask task){
        UniView uniView = Uni.create(parent.getContext(), task, null);
        parent.addView(uniView);
        uniView.excute();
    }

    public static void replace(ViewGroup parent, UniTask task){
        parent.removeAllViews();
        Uni.add(parent, task);
    }

    public static UniView bind(Activity activity, int id, UniTask task){
        UniView uniView = (UniView)activity.findViewById(id);
        task.init(uniView);
        return uniView;
    }
    public static UniView bind(UniView uniView, UniTask task){
        task.init(uniView);
        return uniView;
    }

    interface UniBuildInterface {
        UniInterface getUniInterface();
        UniMapper getMapper();
        Class<? extends UniView> getCustomUniView();
        Store<?> getParam();
        ProgressBuilder getProgress();
        void reset();
    }


}
