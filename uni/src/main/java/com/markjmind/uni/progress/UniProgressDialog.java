package com.markjmind.uni.progress;

import android.content.Context;
import android.view.View;

import com.markjmind.uni.thread.CancelAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-25
 */
public class UniProgressDialog extends ProgressInfo implements OnProgressListener{

    protected UniProgressDialog(){
        setListener(this);
    }

    public UniProgressDialog(int layoutId){
        setLayoutId(layoutId);
        setListener(this);
    }

    @Override
    public UniProgress.Mode getMode() {
        return UniProgress.Mode.dialog;
    }

    @Override
    public void onStart(View layout, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onUpdate(View layout, Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onDestroy(View layout, boolean attach) {

    }

    public View findViewById(int id){
        return mapper.findViewById(id);
    }

    public Context getContext(){
        return layout.getContext();
    }
}
