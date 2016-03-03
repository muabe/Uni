package com.markjmind.fragmenttest;

import android.content.Context;
import android.widget.Toast;

import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-03
 */
@Layout(R.layout.content2)
@Progress(mode = UniProgress.VIEW, res = R.layout.one_progress)
public class Content2 extends UniView{

    public Content2(Context context) {
        super(context);
    }

    @Override
    public void onLoad(LoadEvent loadEvent, CancelAdapter cancelAdapter) throws Exception {
        Thread.sleep(4000);
    }

    @Override
    public void onPost()   {
        Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
    }
}
