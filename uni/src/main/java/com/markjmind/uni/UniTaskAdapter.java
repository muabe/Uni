/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-18
 */
public abstract class UniTaskAdapter implements UniTask {

    public UniTaskAdapter(){

    }

    public void onBind(){

    }

    public void onPre(){

    }

    public void onUpdate(Object value, CancelAdapter cancelAdapter){

    }

    public void onFail(boolean isException, String message, Exception e){

    }

    public void onCancelled(boolean attached){

    }
}
