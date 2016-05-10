package com.markjmind.uni.thread;

import com.markjmind.uni.UniInterface;
import com.markjmind.uni.UniLoadFail;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-11
 */
public class ProcessAdapter implements ProcessObserver {
    private UniInterface uniInterface;
    private UniLoadFail uniLoadFail;

    public ProcessAdapter(UniInterface uniInterface, UniLoadFail uniLoadFail) {
        this.uniInterface = uniInterface;
        this.uniLoadFail = uniLoadFail;
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
        if(uniLoadFail==null) {
            this.uniInterface.onException(e);
        }else{
            this.uniLoadFail.onException(e);
        }
    }

    @Override
    public void onCancelled(boolean attached) {
        if(uniLoadFail==null) {
            this.uniInterface.onCancelled(attached);
        }else{
            this.uniLoadFail.onCancelled(attached);
        }
    }
}
