/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.fragmenttest;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.progress.UniProgressDialog;
import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
@Layout(R.layout.progress)
public class SimpleProgressBar extends UniProgressDialog {
    @Override
    public void onStart(View layout, CancelAdapter cancelAdapter) {
        ProgressBar bar = (ProgressBar) layout.findViewById(R.id.progressBar);
        bar.setMax(100);
        Button cancel = (Button) layout.findViewById(R.id.cancel);
        final CancelAdapter adapter = cancelAdapter;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText("취소중...");
                v.setEnabled(false);
                adapter.cancel();
            }
        });
    }

    @Override
    public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {
        ProgressBar bar = (ProgressBar) layout.findViewById(R.id.progressBar);
        bar.setProgress((int) value);
    }

    @Override
    public void onDestroy(View layout, boolean attach) {
    }
}
