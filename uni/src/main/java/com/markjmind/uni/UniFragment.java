package com.markjmind.uni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
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
    private UniTask uniTask;
    private UniView uniView;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;

    private boolean isPopStack;


    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        uniView = null;
        uniTask = new UniTask();
        mapper = uniTask.mapper;
        param = uniTask.param;
        progress = uniTask.progress;

        isPopStack = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(uniView == null || !isPopStack) {
            uniView = new UniView(getActivity());
            uniTask.init(uniView, this, this, true, container);
            setBackStack(false);
            uniTask.excute();
        }
        return uniView;
    }

    public void setAsync(boolean isAsync){
        this.uniTask.setAsync(isAsync);
    }

    public void setBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    public void excute(){
        uniTask.excute();
    }

    public void excute(UniInterface uniInterface){
        uniTask.excute(uniInterface);
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/

    @Override
    public void cancel(String id) {
        uniTask.cancel(id);
    }

    @Override
    public void cancelAll() {
        uniTask.cancelAll();
    }


    /*************************************************** 인터페이스 관련 *********************************************/

    @Override
    public void onBind() {
    }

    @Override
    public void onPre() {
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onPost() {
    }

    @Override
    public void onPostFail(String message, Object arg) {
    }

    @Override
    public void onException(Exception e) {
    }

    @Override
    public void onCancelled(boolean attach) {
    }




}
