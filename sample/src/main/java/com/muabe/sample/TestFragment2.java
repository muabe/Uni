package com.muabe.sample;

import android.annotation.SuppressLint;
import android.view.View;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.OnClick;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-12-05
 */

@SuppressLint("ValidFragment")
@Layout(R.layout.layout2)
public class TestFragment2 extends UniFragment{
    int layout;

    public TestFragment2(int layout){
        super();
        this.layout = layout;

    }

    @Override
    public void onPre() {
        addFinishResult("result","result : 2 -> 1");
    }

    @OnClick
    public void area(View view){
        FragmentBuilder.getBuilder(this)
                .replace(layout, new TestFragment3(layout));
    }
}
