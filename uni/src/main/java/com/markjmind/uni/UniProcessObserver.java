/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.ProcessObserver;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-24
 */
public class UniProcessObserver implements ProcessObserver {
    private UniTask uniTask;

    public UniProcessObserver(UniTask uniTask){
        this.uniTask = uniTask;
    }

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        this.uniTask.onPre();
    }

    @Override
    public void doInBackground(UpdateEvent event,CancelAdapter cancelAdapter) throws Exception{
        this.uniTask.onLoad(event, cancelAdapter);
    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        this.uniTask.onUpdate(value, cancelAdapter);
    }

    @Override
    public void onPostExecute() {
        this.uniTask.onPost();
    }

    @Override
    public void onFailExecute(boolean isException, String message, Exception e) {
        this.uniTask.onFail(isException, message, e);
    }

    @Override
    public void onCancelled(boolean attached) {
        this.uniTask.onCancelled(attached);
    }
}