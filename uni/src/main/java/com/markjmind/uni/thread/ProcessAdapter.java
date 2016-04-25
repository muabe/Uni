package com.markjmind.uni.thread;

import com.markjmind.uni.UniInterface;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-11
 */
public class ProcessAdapter implements ProcessObserver {
    private UniInterface uniInterface;

    public ProcessAdapter(UniInterface uniInterface) {
        this.uniInterface = uniInterface;
    }

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        this.uniInterface.onPre();
    }

    @Override
    public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        this.uniInterface.onLoad(event, cancelAdapter);
    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        this.uniInterface.onUpdate(value, cancelAdapter);
    }

    @Override
    public void onPostExecute() {
        this.uniInterface.onPost();
    }

    @Override
    public void onFailedExecute(String message, Object arg) {
        this.uniInterface.onPostFail(message, arg);
    }

    @Override
    public void onExceptionExecute(Exception e) {
        this.uniInterface.onException(e);
    }

    @Override
    public void onCancelled(boolean attached) {
        this.uniInterface.onCancelled(attached);
    }
}