/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-17
 */
public interface ProcessObserver {
    void onPreExecute(CancelAdapter cancelAdapter);

    void doInBackground(UpdateEvent event,CancelAdapter cancelAdapter) throws Exception;

    void onProgressUpdate(Object value, CancelAdapter cancelAdapter);

    void onPostExecute();

    void onFailExecute(boolean isException, String message, Exception e);

    void onCancelled(boolean attached);
}
