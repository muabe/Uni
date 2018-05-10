package com.muabe.sample.unilayout;

import android.content.Context;
import android.widget.Button;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.muabe.sample.R;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-20
 */

@Progress(mode= UniProgress.VIEW, res=R.layout.default_progress)
@Layout(R.layout.unilayout_standard)
public class UniLayout_Standard extends UniLayout{
    @GetView
    Button button;

    public UniLayout_Standard(Context context) {
        super(context);
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        Thread.sleep(5000);
    }

    @Override
    public void onPost() {
        button.setText("slkdjflskjdflsdkj");
    }
}
