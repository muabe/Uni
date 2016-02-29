package com.markjmind.uni;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
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
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniView extends FrameLayout implements UniTask, CancelObserver{
    public Mapper mapper;
    public Store<?> param;
    public ProgressBuilder progress;

    private View layout;
    private UniTask uniTask;
    private boolean isMapping;
    private CancelObservable cancelObservable;
    private boolean isAsync;

    protected UniView(Context context, Mapper mapper) {
        super(context);
        this.mapper = mapper;
    }

    public UniView(Context context) {
        super(context);
        this.mapper = new UniMapper(this, this);
        init(this, new Store<>(), new ProgressBuilder());
        injectLayout(this);
    }

    public UniView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mapper = new UniMapper(this, this);
        init(this, new Store<>(), new ProgressBuilder());
        //todo attrs에 layout이 있으면 onCreateView에 넣어주자
        injectLayout(this);
    }

    public UniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mapper = new UniMapper(this, this);
        init(this, new Store<>(), new ProgressBuilder());
        injectLayout(this);
    }

    void init(UniTask uniTask, Store<?> param, final ProgressBuilder progress){
        this.uniTask = uniTask;
        this.param = param;
        progress.setParents(this);
        this.progress = progress;

        isMapping = false;
        isAsync = true;
        cancelObservable = new CancelObservable();
        mapper.addAdapter(new ParamAdapter(param));
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                cancelObservable.setAttached(true);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                UniView.this.param.clear();
                progress.param.clear();
                cancelObservable.setAttached(false);
                cancelObservable.cancelAll();
            }
        });
    }

    void injectLayout(ViewGroup container){
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

    private void setView(View layout) {
        this.removeAllViews();
        this.layout = layout;
        if(layout !=null) {
            this.addView(layout);
            this.setLayoutParams(this.layout.getLayoutParams());
        }
    }

    /*************************************************** excute 관련 *********************************************/

    private void bind(UniTask uniTask){
        if(progress.get()==null){
            mapper.addAdapter(new ProgressAdapter(progress));
            mapper.inject(ProgressAdapter.class);
        }
        if(progress.get()!=null){
            progress.get().onBind();
        }
        uniTask.onBind();
        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class, ProgressAdapter.class);
            isMapping = true;
        }
    }

    public String excute(UniTask uniTask){
        bind(uniTask);
        UniMainAsyncTask task = new UniMainAsyncTask(cancelObservable);
        task.addTaskObserver(new UniProcessObserver(uniTask));

        if(progress.isAble()) {
            task.addTaskObserver(progress);
        }

        cancelObservable.add(task);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task.getId();
    }

    public String excute(boolean isAsync){
        if(isAsync) {
            return this.excute(uniTask);
        }else{
            bind(uniTask);
            onPost();
            return null;
        }
    }

    public String excute(){
        return this.excute(this.isAsync);
    }

    public String replace(ViewGroup parents){
        parents.removeAllViews();
        parents.addView(this);
        return excute();
    }

    public String add(ViewGroup parents, int index){
        parents.addView(this, index);
        return excute();
    }

    public String add(ViewGroup parents){
        parents.addView(this);
        return excute();
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


    /*************************************************** UniTask Interface 관련 *********************************************/

    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(LoadEvent loadEvent, CancelAdapter cancelAdapter) throws Exception {

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
        private UniTask uniTask;

        public UniProcessObserver(UniTask uniTask) {
            this.uniTask = uniTask;
        }

        @Override
        public void onPreExecute(CancelAdapter cancelAdapter) {
            this.uniTask.onPre();
        }

        @Override
        public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
            this.uniTask.onLoad(event, cancelAdapter);
        }

        @Override
        public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
            this.uniTask.onUpdate(value, cancelAdapter);
        }

        @Override
        public void onPostExecute() {
            this.uniTask.onPost();
        }

        @Override
        public void onFailedExecute(String message, Object arg) {
            this.uniTask.onPostFail(message, arg);
        }

        @Override
        public void onExceptionExecute(Exception e) {
            this.uniTask.onException(e);
        }

        @Override
        public void onCancelled(boolean attached) {
            this.uniTask.onCancelled(attached);
        }
    }

}
