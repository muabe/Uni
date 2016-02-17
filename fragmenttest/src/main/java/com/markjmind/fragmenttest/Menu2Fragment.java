package com.markjmind.fragmenttest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.markjmind.uni.UniProgress;
import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Param;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.viewer.Jwc;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Menu2Fragment extends Fragment{
    TestUni uniView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        uniView = new TestUni(getActivity());
        uniView.param.add("1", "1");
        uniView.param.add("2","2");
        uniView.param.add("a","aaa");
        uniView.param.add("b","bbb");
        uniView.progress.setOnProgressListener(new UniProgress.OnProgressListener() {
            ObjectAnimator obj;
            @Override
            public void onStart(View layout, CancelAdapter cancelAdapter) {
                ProgressBar bar = (ProgressBar) layout.findViewById(R.id.progressBar);
                bar.setMax(500);
                Button cancel = (Button)layout.findViewById(R.id.cancel);
                final CancelAdapter adapter = cancelAdapter;
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.cancel();
                    }
                });

                obj = ObjectAnimator.ofFloat(cancel, View.TRANSLATION_X,
                        Jwc.getPix(layout.getContext(), 50)*-1
                        ,Jwc.getPix(layout.getContext(),50));
                obj.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    int count = 0;
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Log.e("d", ""+count++);
                    }
                });
                obj.setDuration(1000);
                obj.setRepeatCount(5000);
                obj.setRepeatMode(ObjectAnimator.REVERSE);
                obj.start();
            }

            @Override
            public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {
                ProgressBar bar = (ProgressBar) layout.findViewById(R.id.progressBar);
                bar.setProgress((int) value);

            }

            @Override
            public void onDestroy(View layout, boolean attach) {
                obj.cancel();
            }

        }).bind(R.layout.progress);
        uniView.excute();
        return uniView;
    }

    @Layout(R.layout.uniview)
    class TestUni extends UniView{
        @Param
        String a,b;

        @GetView
        Button button;

        public TestUni(Context context) {
            super(context);
        }

        @Override
        public void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception {
            for(int i=0;i<500;i++){
                event.update(i);
                Thread.sleep(50);
            }

        }

        @Override
        public void onUpdate(Object value, CancelAdapter cancelAdapter) {
            button.setText("" + value);
        }

        @Override
        public void onPost() {
            button.setText("유니뷰 버튼");
            Log.e("d", a);
            Log.e("d",b);
          }

        public int getId(String idName, Context context) throws Exception{
            int resID = context.getResources().getIdentifier(idName, "id", context.getPackageName());
            return resID;
        }

        @Override
        public void onCancelled(boolean attach) {
            Log.e("sd","캔슬");
        }
    }
}
