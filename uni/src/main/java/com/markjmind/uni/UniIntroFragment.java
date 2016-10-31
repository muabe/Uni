package com.markjmind.uni;

import com.markjmind.uni.mapper.annotiation.TimeouttInjector;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

import java.util.Date;

/**
 * Created by MarkJ on 2016-10-31.
 */

public class UniIntroFragment extends UniFragment{
    private int timeOut = 0;
    public UniIntroFragment(){
        super();
        getMapper().addSubscriptionOnStart(new TimeouttInjector(this));
    }

    public UniIntroFragment setTimeout(int timeout){
        this.timeOut = timeout;
        return this;
    }

    @Override
    protected UniInterface getUniInterface() {

        return new UniInterface() {

            long startTime;
            UniInterface superUniInterface = UniIntroFragment.super.getUniInterface();
            @Override
            public void onBind() {
                startTime = new Date().getTime();
                superUniInterface.onBind();
            }

            @Override
            public void onPre() {
                superUniInterface.onPre();
            }

            @Override
            public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                superUniInterface.onLoad(event, cancelAdapter);
                if(timeOut>0) {
                    int delay = (int) (new Date().getTime() - startTime);
                    if (timeOut > delay) {
                        Thread.sleep(timeOut - delay);
                    }
                }
            }

            @Override
            public void onUpdate(Object value, CancelAdapter cancelAdapter) {
                superUniInterface.onUpdate(value, cancelAdapter);
            }

            @Override
            public void onPost() {
                superUniInterface.onPost();
            }

            @Override
            public void onPostFail(String message, Object arg) {
                superUniInterface.onPostFail(message, arg);
            }

            @Override
            public void onException(Exception e) {
                superUniInterface.onException(e);
            }

            @Override
            public void onCancelled(boolean attached) {
                superUniInterface.onCancelled(attached);
            }
        };
    }
}
