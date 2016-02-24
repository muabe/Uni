package com.markjmind.uni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.hub.Store;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.viewer.UpdateEvent;


/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniTask, CancelObservable {
    public Store<?> param;
    public UniProgress progress;

    private UniView uniView;
    private boolean isPopStack;

    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        isPopStack = false;
        uniView = null;
        param = new Store<>();
        progress = new UniProgress();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(uniView == null || !isPopStack) {
            uniView = new UniView(getActivity(), this, container);
            uniView.setUniTask(this);
            progress.setParents(uniView);
            uniView.setUniProgress(progress);
            uniView.addMapperAdapter(new ParamAdapter(param));
            setBackStack(false);
            uniView.excute();
        }
        return uniView;
    }

    public void setBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    @Override
    public void cancel(String id) {
        uniView.cancel(id);
    }

    @Override
    public void cancelAll() {
        uniView.cancelAll();
    }

    public void excute(UniTask uniInterfacece){
        uniView.excute(uniInterfacece);
    }

    /*************************************************** 인터페이스 관련 *********************************************/

    @Override
    public void onBind() {
        uniView.onBind();
    }

    @Override
    public void onPre() {
        uniView.onPre();
    }

    @Override
    public void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception {
        uniView.onLoad(event, cancelAdapter);
    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {
        uniView.onUpdate(value, cancelAdapter);

    }

    @Override
    public void onPost() {
        uniView.onPost();
    }

    @Override
    public void onFail(boolean isException, String message, Exception e) {
        uniView.onFail(isException, message, e);
    }

    @Override
    public void onCancelled(boolean attach) {
        uniView.onCancelled(attach);
    }


}
