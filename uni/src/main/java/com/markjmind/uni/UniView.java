package com.markjmind.uni;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.exception.UniLoadFailException;
import com.markjmind.uni.hub.Store;
import com.markjmind.uni.mapper.MapperAdapter;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.thread.DetachedObservable;
import com.markjmind.uni.thread.TaskObserver;
import com.markjmind.uni.thread.UniMainAsyncTask;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniView extends FrameLayout implements UniTask, CancelObservable{
    public Store<UniView> param;
    public UniProgress progress;

    private View layout;
    private UniTask uniTask;
    private UniMapper mapper;
    private boolean isMapping;
    private DetachedObservable detachedObservable;


    protected UniView(Context context, Object mappingObject, ViewGroup container) {
        super(context);
        this.mapper = new UniMapper(this, mappingObject);
        init();
        injectLayout(container);
    }

    public UniView(Context context) {
        super(context);
        this.mapper = new UniMapper(this, this);
        init();
        injectLayout(this);
    }

    public UniView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mapper = new UniMapper(this, this);
        init();
        //todo attrs에 layout이 있으면 onCreateView에 넣어주자
        injectLayout(this);
    }

    public UniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mapper = new UniMapper(this, this);
        init();
        injectLayout(this);
    }

    private void init(){
        param = new Store<>();
        progress = new UniProgress(this);

        isMapping = false;
        detachedObservable = new DetachedObservable();
        addMapperAdapter(new ParamAdapter(param));
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                detachedObservable.setAttached(true);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                detachedObservable.setAttached(false);
                detachedObservable.cancelAll();
            }
        });
    }

    protected void injectLayout(ViewGroup container){
        if(layout==null) {
            mapper.inject(LayoutAdapter.class);
            int layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
            if(layoutId>0) {
                LayoutInflater inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                layout = inflater.inflate(layoutId, container, false);
            }
        }
        setView(layout);
    }

    protected void setView(View layout) {
        this.removeAllViews();
        this.layout = layout;
        if(layout !=null) {
            this.addView(layout);
            this.setLayoutParams(this.layout.getLayoutParams());
        }
    }

    protected void setUniTask(UniTask uniTask){
        this.uniTask = uniTask;
    }

    protected void setUniProgress(UniProgress progress){
        this.progress = progress;
    }

    protected void addMapperAdapter(MapperAdapter adapter){
        mapper.addAdapter(adapter);
    }

    protected void fail(String message) throws UniLoadFailException {
        throw new UniLoadFailException(message);
    }


    public String excute(UniTask uniTask){
        uniTask.onBind();
        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class);
            isMapping = true;
        }

        UniMainAsyncTask task = new UniMainAsyncTask(detachedObservable);
        task.addTaskObserver(new InterfaceObserver(uniTask));
        if(progress.isAble()) {
            task.addTaskObserver(progress);
        }

        detachedObservable.add(task);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task.getId();
    }

    public String excute(){
        UniTask tempUniImp = uniTask;
        if(tempUniImp==null) {
            tempUniImp = this;
        }
        return this.excute(tempUniImp);
    }

    @Override
    public void cancel(String id){
        detachedObservable.cancel(id);
    }
    @Override
    public void cancelAll(){
        detachedObservable.cancelAll();
    }



    /*************************************************** 인터페이스 관련 *********************************************/

    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(UpdateEvent updateEvent, CancelAdapter cancelAdapter) throws Exception {

    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onPost() {

    }

    @Override
    public void onFail(boolean isException, String message, Exception e) {

    }

    @Override
    public void onCancelled(boolean attached) {

    }

    private class InterfaceObserver implements TaskObserver{
        private UniTask uniTask;

        public InterfaceObserver(UniTask uniTask){
            this.uniTask = uniTask;
        }

        @Override
        public void onPreExecute(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) {
            this.uniTask.onPre();
        }

        @Override
        public void doInBackground(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) throws Exception{
            this.uniTask.onLoad(uniTask, cancelAdapter);
        }

        @Override
        public void onProgressUpdate(UniMainAsyncTask uniTask, Object value, CancelAdapter cancelAdapter) {
            this.uniTask.onUpdate(value, cancelAdapter);
        }

        @Override
        public void onPostExecute(UniMainAsyncTask uniTask) {
            this.uniTask.onPost();
        }

        @Override
        public void onFailExecute(UniMainAsyncTask uniTask, boolean isException, String message, Exception e) {
            this.uniTask.onFail(isException, message, e);
        }

        @Override
        public void onCancelled(UniMainAsyncTask uniTask, boolean attached) {
            this.uniTask.onCancelled(attached);
        }
    }
}
