package com.muabe.sample;

import android.annotation.SuppressLint;
import android.view.View;

import com.markjmind.uni.UniAsyncTask;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.OnClick;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-12-05
 */

@SuppressLint("ValidFragment")
@Layout(R.layout.layout4)
@Progress(mode= UniProgress.VIEW, res=R.layout.default_progress)
public class TestFragment4 extends UniFragment{
    int layout;

    public TestFragment4(int layout){
        super();
        this.layout = layout;

    }


    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        Thread.sleep(1000);
    }

    @OnClick
    public void area(View view){
        new UniAsyncTask(this) {
            @Override
            public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                Thread.sleep(2000);
            }

            @Override
            public void onPost() {
//                onBackPressed();
                FragmentBuilder.getBuilder(TestFragment4.this).popBackStackClear(false);
            }
        }.excute();

    }
}
