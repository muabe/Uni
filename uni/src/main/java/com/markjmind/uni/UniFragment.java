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
import com.markjmind.uni.thread.LoadEvent;


/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniInterface{
    private UniTask uniTask;
    private UniLayout uniLayout;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;

    private boolean isPopStack;


    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        uniLayout = null;
        uniTask = new UniTask();
        mapper = uniTask.mapper;
        param = uniTask.param;
        progress = new ProgressBuilder();

        isPopStack = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(uniLayout == null || isPopStack) {
            uniLayout = new UniLayout(getActivity());
            uniTask.init(uniLayout, progress, this, this, container);
//            setRefreshBackStack(false);
            uniTask.excute(progress);
        }
        return uniLayout;
    }



    public View findViewById(int id){
        return uniLayout.findViewById(id);
    }

    public void setAsync(boolean isAsync){
        this.uniTask.setAsync(isAsync);
    }

    public void setRefreshBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    /*************************************************** 필수 항목 *********************************************/
    public UniLayout getUniLayout(){
        return uniLayout;
    }

    /*************************************************** 실행 관련 *********************************************/
    public void post(){
        uniTask.post();
    }

    public String excute(){
        return uniTask.excute(progress);
    }

//    protected String excute(UniAsyncTask uniAsyncTask, UniLoadFail uniLoadFail){
//        return uniTask.excute(uniAsyncTask, uniLoadFail);
//    }
//
//    protected String excute(UniAsyncTask uniAsyncTask){
//        return uniTask.excute(uniAsyncTask);
//    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/
    public void cancel(String id) {
        uniTask.cancel(id);
    }

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
