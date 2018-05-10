package com.markjmind.uni;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.boot.FragmentStack;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.UniMapper;
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
    private UniTask uniTask;
    private UniLayout uniLayout;
    public Store<?> param;
    public ProgressBuilder progressBuilder;
    private UniAop aop;
    private Bundle savedInstanceState;

    private boolean isPopStack;
    private int parentsViewID = -1;

    private FragmentStack fragmentStack = new FragmentStack();

    private Store<?> finishResult = new Store<>();
    private OnFinishedListener onFinishedListener;


    public interface OnFinishedListener{
        void onFinished(Store<?> finishResult);
    }

    /**
     * 기본생성자
     */
    public UniFragment() {
        super();
        uniLayout = null;
        uniTask = new UniTask(true);
        uniTask.initAtrribute(this, getUniInterface());
        isPopStack = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        if(uniLayout == null) {
            setRefreshBackStack(false);
            uniLayout = new UniLayout(getActivity());
            uniTask.syncUniLayout(uniLayout);
            uniTask.setBindInfo(this,uniLayout,inflater,container);
            getTask().setAsync(isAsync()).execute();
        }else{
            if(isPopStack){
                getTask().refresh();
            }else {
                onPostCache();
            }
        }
        return uniLayout;
    }

    @Override
    public void onResume() {
        if(fragmentStack.clearPopStackOnResume) {
            fragmentStack.clearPopStackOnResume = false;
            if(fragmentStack.parentsID!=null){
                FragmentBuilder.getBuilder(this).popBackStackClear(fragmentStack.clearAll, fragmentStack.parentsID);
            }else{
                FragmentBuilder.getBuilder(this).popBackStackClear(fragmentStack.clearAll);
            }
        }
        if(fragmentStack.popStackOnResume){
            fragmentStack.popStackOnResume = false;
            onBackPressed();
        }
        super.onResume();
    }

    public FragmentStack getFragmentStack(){
        return this.fragmentStack;
    }

    /*************************************************** BootStrap Builder관련 *********************************************/

    public int getParentsViewID(){
        return this.parentsViewID;
    }

    public void setParentsViewID(int parentsViewID){
        this.parentsViewID = parentsViewID;
    }

    public FragmentBuilder getBuilder(){
        return FragmentBuilder.getBuilder(this);
    }

    /*************************************************** 지원함수 관련 *********************************************/

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

    public Application getApplication(){
        return getActivity().getApplication();
    }

    public <T extends Application>T app(Class<T> clz){
        return clz.cast(getActivity().getApplication());
    }

    public Bundle getSavedInstanceState(){
        return savedInstanceState;
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

    public synchronized void onBackPressed() {
        if(!isBacking) {
            if (getFragmentStack().parentsID != null) {
                isBacking = true;
                getBuilder().popBackStack(getFragmentStack().parentsID); //해당 부모에 대해서만 popback
                onBackPressed(getFragmentStack().parentsID);
            } else {
                getBuilder().popBackStack();
            }

        }
        isBacking = false;
    }

    private boolean isBacking = false;
    public synchronized void onBackPressed(int parentsViewID) {
        if(!isBacking) {
            isBacking = true;
            getBuilder().popBackStack(parentsViewID); //해당 부모에 대해서만 popback
            onBackPressed();
            if(this.onFinishedListener!=null){
                this.onFinishedListener.onFinished(finishResult);
            }
        }
        isBacking = false;
    }


    public void setOnFinishedListener(OnFinishedListener finishedListener){
        this.onFinishedListener = finishedListener;
    }

    public UniFragment addFinishResult(String key, Object value){
        finishResult.add(key, value);
        return this;
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

    void setUniTask(UniTask uniTask){
        this.uniTask = uniTask;
    }

    public UniMapper getMapper(){
        return this.uniTask.mapper;
    }

    public TaskController getTask(){
        return uniTask.getTask();
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

    protected UniInterface getUniInterface(){
        return new UniInterface() {
            @Override
            public void onBind() {
                UniFragment.this.onBind();
            }

            @Override
            public void onPre() {
                UniFragment.this.onPre();
            }

            @Override
            public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                UniFragment.this.onLoad(event, cancelAdapter);
            }

            @Override
            public void onUpdate(Object value, CancelAdapter cancelAdapter) {
                UniFragment.this.onUpdate(value, cancelAdapter);
            }

            @Override
            public void onPost() {
                UniFragment.this.onPost();
            }

            @Override
            public void onPostFail(String message, Object arg) {
                UniFragment.this.onPostFail(message, arg);
            }

            @Override
            public void onException(Exception e) {
                UniFragment.this.onException(e);
            }

            @Override
            public void onCancelled(boolean attached) {
                UniFragment.this.onCancelled(attached);
                isPopStack = true;
            }
        };
    }
}
