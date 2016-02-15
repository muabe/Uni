package com.markjmind.libox.test;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.libox.R;
import com.markjmind.uni.viewer.CancelListener;
import com.markjmind.uni.viewer.ProgressViewListener;
import com.markjmind.uni.viewer.UpdateEvent;
import com.markjmind.uni.viewer.Viewer;
import com.markjmind.uni.viewer.ViewerBuilder;
import com.markjmind.uni.mapper.annotiation.Layout;

@Layout(R.layout.test1)
public class Test1 extends Viewer {

    TextView textView1;

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        build.setAsync(true)
             .setPreLayout(true)
             .setProgressLayout(R.layout.progress, new ProgressViewListener() {
                 @Override
                 public void onStart(int requestCode, View loadView) {
                     ((TextView) loadView.findViewById(R.id.progress_text)).setText("");
                 }

                 @Override
                 public void onUpdate(int requestCode, View loadView, Object value) {
                     ((ProgressBar) loadView.findViewById(R.id.progress)).setProgress((int) value);
                     ((TextView) loadView.findViewById(R.id.progress_text)).setText((int) value + "/100%");
                 }

                 @Override
                 public void onDestroy(int requestCode, View loadView) {

                 }

             })
             .addParam("p2", "내부");
        setCancelListener(new CancelListener() {
            @Override
            public void onCancel(int requestCode, Viewer viewer) {
                Toast.makeText(getContext(),"캔슬했다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPre(int requestCode) {
        textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText("Loading..");
    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) {
        for(int i=0;i<=100;i++) {
            try {
                event.update(i);
                Thread.sleep(20);
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public void onCancelled(int requestCode) {

    }

    @Override
    public void onPost(int requestCode) {
        textView1.setText("p1:" + getParamString("p1") + "\np2:" + getParamString("p2"));
        Toast.makeText(getContext(), "다했다", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFail(int requestCode, boolean isException, String message, Exception e) {
        Toast.makeText(getContext(), "실패", Toast.LENGTH_LONG).show();
    }
}
