/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.fragmenttest;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.markjmind.uni.UniDialog;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.UniAsyncTask;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */

@Layout(R.layout.simple_dialog)
@Progress(type=SimpleProgressBar.class)
public class SimpleDialog extends UniDialog{
    @GetView
    Button button2;

    public SimpleDialog(Context context) {
        super(context);
    }

    @Override
    public void onPre() {


            progress.param.add("textName", "하이");
            button2.setText(param.getString("hi"));
            Toast.makeText(getContext(), "dfsfd", Toast.LENGTH_SHORT).show();
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!progress.isShowing()) {
                        progress.param.add("textName", "thread");
                        excute(new UniAsyncTask() {

                            @Override
                            public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                                for (int i = 0; i <= 1000; i++) {
                                    event.update(i / 10);
//                                Log.i("d", "" + i);
                                    Thread.sleep(10);
                                }
                            }

                            @Override
                            public void onPost() {
                                button2.setText("완료");
                            }

                        });
                    }
                }
            });

    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        for(int i=0;i<=100;i++){
            event.update(i);
//            Log.i("d", "" + i);
            Thread.sleep(10);
        }
        param.add("hi","hi");

    }

    @Override
    public void onPost() {

    }

}
