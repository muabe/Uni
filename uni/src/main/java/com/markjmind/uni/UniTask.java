package com.markjmind.uni;

import android.content.Context;
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
import com.markjmind.uni.thread.CancelObserver;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.ProcessObserver;
import com.markjmind.uni.thread.UniMainAsyncTask;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-04
 */
public class UniTask implements UniInterface, CancelObserver {
    private UniView uniView;
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;


    private Context context;
    private boolean isMapping;
    private CancelObservable cancelObservable;
    private boolean isAsync;

    private UniInterface uniInterface;


    public UniTask(){
        uniView = null;
        mapper = new UniMapper();
        param = new Store<>();
        progress = new ProgressBuilder();
        uniInterface = this;
        cancelObservable = new CancelObservable();
        isAsync = true;
    }


    void injectLayout(ViewGroup container){
        mapper.inject(LayoutAdapter.class);
        int layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
        if(layoutId>0) {
            LayoutInflater inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            uniView.setView(inflater.inflate(layoutId, container, false));
        }

    }

    void init(UniView uniView, Object mappingObj, UniInterface uniInterface, boolean injectLayout, ViewGroup container){
        isMapping = false;
        this.uniView = uniView;
        this.context = uniView.getContext();
        this.uniView.setUniTask(this);
        mapper.reset(this.uniView, mappingObj);
        this.uniInterface = uniInterface;
        this.uniView.init(this);
        if(injectLayout) {
            if (container == null) {
                injectLayout(uniView);
            } else {
                injectLayout(container);
            }
        }else{
//            uniView.setView();
        }
    }

    CancelObservable getCancelObservable(){
        return cancelObservable;
    }

    /*************************************************** 외부지원 함수 관련 *********************************************/
    public void bind(UniView uniView){
        this.init(uniView, this, this, false, null);
    }

    public void create(Context context){
        this.init(new UniView(context), this, this, true, null);
    }

    public Context getContext(){
        return context;
    }

    public View findViewById(int id){
        return uniView.findViewById(id);
    }

    public UniView getUniView(){
        return uniView;
    }

    public void setAsync(boolean isAsync){
        this.isAsync = isAsync;
    }

    /*************************************************** CancelObserver Interface 관련 *********************************************/
    @Override
    public void cancel(String id){
        cancelObservable.cancel(id);
    }

    @Override
    public void cancelAll(){
        cancelObservable.cancelAll();
    }


    /*************************************************** excute 관련 *********************************************/
    public void post(){
        uniInterface.onBind();
        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class, ProgressAdapter.class);
            isMapping = true;
        }
        onPost();
    }



    public String excute(){
        return this.excute(uniInterface);
    }

    public String excute(UniInterface uniInterface){
        if(progress.get()==null){
            mapper.addAdapter(new ProgressAdapter(progress));
            mapper.inject(ProgressAdapter.class);
        }
        if(progress.get()!=null){
            progress.get().onBind();
        }
        uniInterface.onBind();

        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class, ProgressAdapter.class);
            isMapping = true;
        }

        UniMainAsyncTask task = new UniMainAsyncTask(cancelObservable);
        if(progress.isAble()) {
            task.addTaskObserver(progress);
        }
        task.addTaskObserver(new UniProcessObserver(uniInterface));
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

    /*************************************************** Task Process 관련 *********************************************/
    class UniProcessObserver implements ProcessObserver {
        private UniInterface uniInterface;

        public UniProcessObserver(UniInterface uniInterface) {
            this.uniInterface = uniInterface;
        }

        @Override
        public void onPreExecute(CancelAdapter cancelAdapter) {
            this.uniInterface.onPre();
        }

        @Override
        public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
            this.uniInterface.onLoad(event, cancelAdapter);
        }

        @Override
        public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
            this.uniInterface.onUpdate(value, cancelAdapter);
        }

        @Override
        public void onPostExecute() {
            this.uniInterface.onPost();
        }

        @Override
        public void onFailedExecute(String message, Object arg) {
            this.uniInterface.onPostFail(message, arg);
        }

        @Override
        public void onExceptionExecute(Exception e) {
            this.uniInterface.onException(e);
        }

        @Override
        public void onCancelled(boolean attached) {
            this.uniInterface.onCancelled(attached);
        }
    }

}
