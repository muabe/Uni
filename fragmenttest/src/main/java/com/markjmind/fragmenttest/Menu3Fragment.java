package com.markjmind.fragmenttest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.UniTaskAdapter;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.progress.OnProgressListener;
import com.markjmind.uni.progress.ViewProgressInfo;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.UpdateEvent;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.item)
public class Menu3Fragment extends UniFragment {
    @GetView
    Button btn;


    @Override
    public void onBind() {
        progress.bind(new ViewProgressInfo(R.layout.progress, new OnProgressListener() {
            @Override
            public void onStart(View layout, CancelAdapter cancelAdapter) {
                ProgressBar bar = (ProgressBar) layout.findViewById(R.id.progressBar);
                bar.setMax(100);
                Button cancel = (Button) layout.findViewById(R.id.cancel);
                final CancelAdapter adapter = cancelAdapter;
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        }));
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception {
        for(int i=0;i<=100;i++){
            event.update(i);
            Log.i("d", "" + i);
            Thread.sleep(10);
        }

    }



    @Override
    public void onPost() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                excute(new UniTaskAdapter(Menu3Fragment.this) {

                    @Override
                    public void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception {
                        for(int i=0;i<=100;i++){
                            event.update(i);
                            Log.i("d", "" + i);
                            Thread.sleep(10);
                        }
                    }

                    @Override
                    public void onPost() {
                        btn.setText("완료");
                    }

                });
            }
        });
    }
}
