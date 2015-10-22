package com.markjmind.libox.test;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.libox.R;
import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.UpdateListener;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;
import com.markjmind.uni.annotiation.Layout;

@Layout(R.layout.test1)
public class Test1 extends Viewer {

    TextView textView1;

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        build.setAsync(true)
             .setPreLayout(true)
             .setLoadView(R.layout.progress, new UpdateListener() {
                 @Override
                 public void init(int requestCode, View loadView) {
                 }
                 @Override
                 public void onUpdate(int requestCode, View loadView, Object value) {
                     ((ProgressBar)loadView.findViewById(R.id.progress)).setProgress((int) value);
                 }
             })
             .addParam("p2", "내부");
    }

    @Override
    public void onPre(int requestCode) {
        textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText("로딩중 입니다.");
    }

    @Override
    public boolean onLoad(int requestCode, UpdateEvent event) {
        for(int i=0;i<=100;i++) {
            try {
                event.update((i+11));
                Thread.sleep(50);
            } catch (InterruptedException e) {}
        }
        return true;
    }

    @Override
    public void onUpdate(int requestCode, Object value) {

    }

    @Override
    public void onCancelled(Integer requestCode) {

    }

    @Override
    public void onPost(int requestCode) {
        textView1.setText("p1:" + getParamString("p1") + "\np2:" + getParamString("p2"));
    }

    @Override
    public void onFail(Integer requestCode) {
        Toast.makeText(getContext(), "실패", Toast.LENGTH_LONG).show();
    }
}
