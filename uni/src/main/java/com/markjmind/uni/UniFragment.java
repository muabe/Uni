package com.markjmind.uni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObserver;
import com.markjmind.uni.thread.LoadEvent;


/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniInterface, CancelObserver{
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;

    private UniView uniView;
    private Class<? extends UniView> customUniView;

    private Uni.UniBuildInterface uniBuildInterface = new Uni.UniBuildInterface() {
        @Override
        public UniInterface getUniInterface() {
            return UniFragment.this;
        }
        @Override
        public UniMapper getMapper() {
            return (UniMapper)mapper;
        }
        @Override
        public Class<? extends UniView> getCustomUniView() {
            return customUniView;
        }
        @Override
        public Store<?> getParam() {
            return param;
        }
        @Override
        public ProgressBuilder getProgress() {
            return progress;
        }
        @Override
        public void reset() {
            param.clear();
        }
    };

    private boolean isPopStack;


    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        isPopStack = false;
        uniView = null;
        mapper = new UniMapper();
        param = new Store<>();
        progress = new ProgressBuilder();
    }

    public <T extends UniView> UniFragment(Class<T> uniView){
        customUniView = uniView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(uniView == null || !isPopStack) {
            uniView = Uni.createUniView(getActivity(), uniBuildInterface, container);
            setBackStack(false);
            uniView.excute();
        }
        return uniView;
    }

    public void setBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    public void excute(UniInterface uniInterface){
        uniView.excute(uniInterface);
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/

    @Override
    public void cancel(String id) {
        uniView.cancel(id);
    }

    @Override
    public void cancelAll() {
        uniView.cancelAll();
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
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
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
    public void onPostFail(String message, Object arg) {
        uniView.onPostFail(message, arg);
    }

    @Override
    public void onException(Exception e) {
        uniView.onException(e);
    }

    @Override
    public void onCancelled(boolean attach) {
        uniView.onCancelled(attach);
    }




}
