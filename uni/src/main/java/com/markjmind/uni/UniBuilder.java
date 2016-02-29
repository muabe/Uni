/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

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
class UniBuilder {
    public static UniView createUniView(Context context, final UniInterface uniInterface, ViewGroup container){
        UniView uniView;
        if(uniInterface.getCustomUniView()==null) {
            uniView = new UniView(context, uniInterface.getMapper());
        }else{
            try {
                uniView = (UniView)(uniInterface.getCustomUniView().getConstructor(Context.class, Mapper.class)
                        .newInstance(context, uniInterface.getMapper()));
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
                uniInterface.reset();
            }
        });
        ((UniMapper)uniInterface.getMapper()).reset(uniView, uniInterface.getUniTask());
        uniView.init(uniInterface.getUniTask(), uniInterface.getParam(), uniInterface.getProgress());
        if(container==null){
            uniView.injectLayout(uniView);
        }else{
            uniView.injectLayout(container);
        }

        return uniView;
    }


    interface UniInterface{
        UniTask getUniTask();
        UniMapper getMapper();
        Class<? extends UniView> getCustomUniView();
        Store<?> getParam();
        ProgressBuilder getProgress();
        void reset();
    }


}
