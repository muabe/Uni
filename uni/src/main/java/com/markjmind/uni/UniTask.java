package com.markjmind.uni;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ProgressAdapter;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.ThreadProcessAdapter;
import com.markjmind.uni.thread.UniMainThread;
import com.markjmind.uni.thread.aop.UniAop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-04
 */
public class UniTask implements UniInterface{
    private UniLayout uniLayout;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;


    private Context context;
    private boolean isMapping;
    private boolean isAsync;

    private UniInterface uniInterface;
    private CancelObservable cancelObservable;



    public UniTask(){
        uniLayout = null;
        mapper = new UniMapper();
        uniInterface = this;
        isAsync = true;
        param = new Store<>();
        progress = new ProgressBuilder();
        cancelObservable = new CancelObservable();
    }

    private void injectLayout(LayoutInflater inflater, ViewGroup container){
        uniInterface.onBind();
        mapper.inject(LayoutAdapter.class);
        int layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
        if(layoutId>0) {
            uniLayout.setLayout(inflater.inflate(layoutId, container, false));
        }
    }

    void syncUniLayout(LayoutInflater inflater, UniLayout uniLayout, Store<?> param, ProgressBuilder progress, Object mappingObj, UniInterface uniInterface, ViewGroup container){
        isMapping = false;
        this.uniLayout = uniLayout;
        this.context = uniLayout.getContext();
        mapper.reset(this.uniLayout, mappingObj);
        this.uniInterface = uniInterface;
        this.uniLayout.init(this, param, progress);
        this.progress = this.uniLayout.progress;
        this.param = this.uniLayout.param;
        if(inflater==null) {
            inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        }
        if (container == null) {
            injectLayout(inflater, uniLayout);
        } else {
            injectLayout(inflater, container);
        }
    }

    void syncUniLayout(UniLayout uniLayout, Store<?> param, ProgressBuilder progress, Object mappingObj, UniInterface uniInterface, ViewGroup container){
        this.syncUniLayout(null, uniLayout, param, progress, mappingObj, uniInterface, container);
    }

    CancelObservable getCancelObservable(){
        return cancelObservable;
    }

    public ExcuteBuilder getBuilder(){
        return new ExcuteBuilder(this);
    }

    public void setUniInterface(UniInterface uniInterface){
        this.uniInterface = uniInterface;
    }

    public UniInterface getUniInterface() {
        return uniInterface;
    }

    void setMapping(boolean isMapping){
        this.isMapping = isMapping;
    }

    boolean isMapping(){
        return this.isMapping;
    }

    /*************************************************** Uni 외부지원 함수 관련 *********************************************/

    public View findViewById(int id){
        return uniLayout.findViewById(id);
    }

    public View findViewWithTag(Object tag){
        return uniLayout.findViewWithTag(tag);
    }

    public UniLayout getUniLayout(){
        return uniLayout;
    }

    public void setAsync(boolean isAsync){
        this.isAsync = isAsync;
    }

    public boolean isAsync(){
        return this.isAsync;
    }


    /*************************************************** Context 함수 관련 *********************************************/
    public Context getContext(){
        return context;
    }

    public Resources getResource(){
        return context.getResources();
    }

    public Context getApplicationContext(){
        return context.getApplicationContext();
    }

    public PackageManager getPackageManager(){
        return context.getPackageManager();
    }


    /*************************************************** CancelObserver Interface 관련 *********************************************/
    public void cancel(String id){
        cancelObservable.cancel(id);
    }

    public void cancelAll(){
        cancelObservable.cancelAll();
    }

    public void setTaskAutoCanceled(boolean autoCanceled) {
        cancelObservable.setTaskAutoCanceled(autoCanceled);
    }

    public boolean isRunning(String task){
        if(cancelObservable.getStatus(task)!=null && cancelObservable.getStatus(task).equals(AsyncTask.Status.RUNNING)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFinished(String task){
        if(cancelObservable.getStatus(task)!=null && cancelObservable.getStatus(task).equals(AsyncTask.Status.FINISHED)){
            return true;
        }else{
            return false;
        }
    }


    /*************************************************** excute 관련 *********************************************/
    void memberMapping(){
        if(progress!=null) {
            if (progress.get() == null) {
                mapper.addAdapter(new ProgressAdapter(progress));
                mapper.inject(ProgressAdapter.class);
            }
            if (progress.get() != null) {
                progress.get().onBind();
            }
        }

        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class, ProgressAdapter.class);
            isMapping = true;
        }
    }

    String refresh(ProgressBuilder progress, UniLoadFail uniLoadFail, UniAop uniAop){
        cancelAll();
        return run(progress, uniInterface, uniLoadFail, true, uniAop);
    }

    String run(ProgressBuilder progress, UniInterface uniInterface, UniLoadFail uniLoadFail, boolean skipOnPre, UniAop uniAop){
        UniMainThread task = new UniMainThread(cancelObservable);
        if(progress.isAble()) {
            task.addTaskObserver(progress);
        }
        task.addTaskObserver(new ThreadProcessAdapter(uniInterface, uniLoadFail, skipOnPre).setUniAop(uniAop));
        cancelObservable.add(task);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task.getId();
    }

    /*************************************************** UniTask Interface 관련 *********************************************/
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
    public void onCancelled(boolean attached) {

    }

}

