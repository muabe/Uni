package com.markjmind.uni;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.aop.CancelAop;
import com.markjmind.uni.thread.aop.UniAop;


/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 *
 */

public class UniFragment extends Fragment implements UniInterface{
    private String taskId;
    private UniTask uniTask;
    private UniLayout uniLayout;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;
    private UniAop aop;

    private boolean isPopStack;


    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        uniLayout = null;
        uniTask = new UniTask(true);
        mapper = uniTask.mapper;
        mapper.setInjectParents(UniFragment.class);
        param = new Store<>();
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
            uniTask.syncUniLayout(inflater, uniLayout, param, progress, this, this, container);
            setRefreshBackStack(false);
            getTask().setAsync(isAsync()).excute();
        }else{
            onPostCache();
        }
        return uniLayout;
    }

    public Context getContext(boolean isFragment) {
        if(!isFragment){
            return uniLayout.getContext();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return super.getContext();
            }else{
                return null;
            }
        }
    }

    public Context getContext() {
        if(uniLayout==null){
                return null;
        }else{
            return uniLayout.getContext();
        }
    }

    public View findViewById(int id){
        return uniLayout.findViewById(id);
    }

    boolean isAsync = true;

    public void setAsync(boolean isAsync){
        this.isAsync = isAsync;
    }

    public boolean isAsync(){
        return this.isAsync;
    }

    public void setRefreshBackStack(boolean isPopStack) {
        this.isPopStack = isPopStack;
    }

    /*************************************************** 필수 항목 *********************************************/
    public UniLayout getUniLayout(){
        return uniLayout;
    }

    /*************************************************** 실행 관련 *********************************************/
    public void setCancelAop(CancelAop cancelAop){
        aop.setCancelAop(cancelAop);
    }

    public UniAop getAop(){
        return aop;
    }

    public String refresh(){
        taskId = uniTask.getTask()
                .setAsync(isAsync)
                .setUniAop(getAop())
                .refresh();
        return taskId;
    }

    public String getTaskId(){
        return taskId;
    }

    public TaskController getTask(){
        return uniTask.getTask();
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/

    public boolean isFinished(String task){
        return uniTask.isFinished(task);
    }

    public boolean isRunning(String task){
        return uniTask.isRunning(task);
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

    public void onPostCache(){

    }
}
