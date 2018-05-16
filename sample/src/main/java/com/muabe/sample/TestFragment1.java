package com.muabe.sample;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.annotiation.GetView;
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
@Layout(R.layout.layout1)
public class TestFragment1 extends UniFragment{
    int layout;

    @GetView
    TextView text;
    public TestFragment1(int layout){
        super();
        this.layout = layout;

    }

    @OnClick
    public void area(View view){
        FragmentBuilder.getBuilder(this)
                .setOnFinishedListener(new OnFinishedListener() {
                    @Override
                    public void onFinished(Store<?> finishResult) {
                        String result = finishResult.getString("result");
                        text.setText(result);
                    }
                })
                .replace(layout, new TestFragment2(layout));
    }
}
