package com.markjmind.fragmenttest;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.uni.UniTask;
import com.markjmind.uni.mapper.annotiation.GetView;
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
@Progress(mode = UniProgress.VIEW, res = R.layout.one_progress)
public class Content2 extends UniTask {
    @GetView
    Button button3;
    @GetView
    TextView text;

    @Override
    public void onLoad(LoadEvent loadEvent, CancelAdapter cancelAdapter) throws Exception {
        Thread.sleep(4000);
    }

    @Override
    public void onPost()   {
        Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
        button3.setText("안뇽");
        text.setText("ok");
    }
}
