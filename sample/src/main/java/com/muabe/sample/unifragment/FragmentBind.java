package com.muabe.sample.unifragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.markjmind.uni.UniTask;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.OnClick;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.muabe.sample.R;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-13
 */
@Layout(R.layout.unilayout)
public class FragmentBind extends UniTask{
    @GetView
    Button button1;

    @Override
    public void onPre() {
        button1.setText("onPre");
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        Thread.sleep(2000);
    }

    @Override
    public void onPost() {
        button1.setText("onPost");
    }

    @OnClick
    public void button1(View view){
        Toast.makeText(getContext(),"hellow",Toast.LENGTH_SHORT).show();
    }
}
