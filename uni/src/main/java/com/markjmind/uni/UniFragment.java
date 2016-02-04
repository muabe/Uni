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
 * start : 2013.11.17<br>
 * <br>
 * Base Viewer 클래스<br>
 * 일반 Viewer을 정의하는 클래스는<br>
 * BaseLayout 를 상속하여 view_init메소드에 화면을 정의한다.
 *
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniInterface, BuildInterface {
    private UniView uniView;

    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        uniView = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(uniView==null) {
            uniView = new UniView(getActivity(), this, container);
            uniView.setUniInterface(this);
            uniView.excute();
        }
        return uniView;
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
