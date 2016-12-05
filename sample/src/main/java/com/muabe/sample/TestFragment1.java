package com.muabe.sample;

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

@Layout(R.layout.layout1)
public class TestFragment1 extends UniFragment{
    int layout;

    public TestFragment1(int layout){
        super();
        this.layout = layout;

    }

    @OnClick
    public void text(View view){
        FragmentBuilder.getBuilder(this)
                .replace(layout, new TestFragment2(layout));
    }
}
