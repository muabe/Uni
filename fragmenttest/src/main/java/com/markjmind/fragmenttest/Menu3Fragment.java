package com.markjmind.fragmenttest;

import android.view.View;
import android.widget.Button;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.UniTaskAdapter;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.item)
@Progress(mode = UniProgress.VIEW, res=R.layout.one_progress)
public class Menu3Fragment extends UniFragment {
    @GetView
    Button btn;


    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {
        progress.param.add("textName", "하이");
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        for(int i=0;i<=100;i++){
            event.update(i);
            Thread.sleep(10);
        }
        param.add("hi","hi");

    }

    @Override
    public void onPost() {
        btn.setText(param.getString("hi"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.param.add("textName", "thread");
                progress.set(UniProgress.VIEW, new SimpleProgressBar());
//                progress.get().setMode(U```niProgress.DIALOG);
                excute(new UniTaskAdapter(Menu3Fragment.this) {

                    @Override
                    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            event.update(i);
                            Thread.sleep(10);
                        }
                    }

                    @Override
                    public void onPost() {
                        btn.setText("완료");
                    }

                });
//                SimpleDialog d = new SimpleDialog(getActivity());
//                d.show();
            }
        });
    }

    public String getMode(int mode){
        if(mode==0){
            return "VIEW";
        }else{
            return "DIALGO";
        }
    }

}