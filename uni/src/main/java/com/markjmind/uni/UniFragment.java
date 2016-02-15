package com.markjmind.uni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.builder.BuildInterface;
import com.markjmind.uni.viewer.UpdateEvent;
import com.markjmind.uni.viewer.ViewerBuilder;


/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniInterface, BuildInterface {
    private UniView uniView;
    public BindConfig config;
    private boolean isPopStack;

    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        isPopStack = false;
        uniView = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(uniView == null || !isPopStack) {
            uniView = new UniView(getActivity(), this, container);
            uniView.setUniInterface(this);
            uniView.config = this.config;
            setBackStack(false);
            uniView.excute();
        }
        return uniView;
    }

    public void setBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    @Override
    public UniView getUniView() {
        return uniView;
    }

    @Override
    public UniInterface getUniInterface() {
        return this;
    }


    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        uniView.onBind(requestCode, build);
    }

    @Override
    public void onPre(int requestCode) {
        uniView.onPre(requestCode);
    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) throws Exception {
        uniView.onLoad(requestCode, event);
    }

    @Override
    public void onUpdate(int requestCode, Object value) {
        uniView.onUpdate(requestCode, value);

    }

    @Override
    public void onPost(int requestCode) {
        uniView.onPost(requestCode);
    }

    @Override
    public void onFail(int requestCode, boolean isException, String message, Exception e) {
        uniView.onFail(requestCode, isException, message, e);
    }

    @Override
    public void onCancelled(int requestCode) {
        uniView.onCancelled(requestCode);
    }
}
