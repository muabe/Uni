/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.thread;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-17
 */
public interface TaskObserver{
    void onPreExecute(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter);

    void doInBackground(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) throws Exception;

    void onProgressUpdate(UniMainAsyncTask uniTask, Object value, CancelAdapter cancelAdapter);

    void onPostExecute(UniMainAsyncTask uniTask);

    void onFailExecute(UniMainAsyncTask uniTask, boolean isException, String message, Exception e);

    void onCancelled(UniMainAsyncTask uniTask, boolean attached);
}
