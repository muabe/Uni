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
import com.markjmind.uni.thread.DetachedObservable;
import com.markjmind.uni.thread.InnerUniTask;
import com.markjmind.uni.viewer.UpdateEvent;
import com.markjmind.uni.viewer.ViewerBuilder;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
  * @since 2016-01-28
 */
public class UniView extends FrameLayout implements UniInterface, View.OnAttachStateChangeListener {
    private View layout;
    private UniInterface uniInterface;
    private UniMapper mapper;
    private boolean isMapping;
    private DetachedObservable detachedObservable;

    public BindConfig config;
    public Store<UniView> param;

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
        isMapping = false;
        detachedObservable = new DetachedObservable();
        config = new BindConfig();
        param = new Store<>();
        addMapperAdapter(new ParamAdapter(param));
        addOnAttachStateChangeListener(this);
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

    protected void setUniInterface(UniInterface uniInterface){
        this.uniInterface = uniInterface;
    }

    protected void addMapperAdapter(MapperAdapter adapter){
        mapper.addAdapter(adapter);
    }

    public String excute(int requestCode, UniInterface uniInterface){
        uniInterface.onBind(requestCode, null);
        if(!isMapping) {
            mapper.injectWithout(LayoutAdapter.class);
            isMapping = true;
        }
        InnerUniTask task = new InnerUniTask(requestCode, detachedObservable, uniInterface);
        detachedObservable.add(task);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task.getId();
    }

    public String excute(int requestCode){
        UniInterface tempUniImp = uniInterface;
        if(tempUniImp==null) {
            tempUniImp = this;
        }
        return this.excute(requestCode, tempUniImp);
    }

    public String excute(UniInterface uniInterface){
        return this.excute(-1, uniInterface);
    }

    public String excute(){
        return this.excute(-1);
    }

    protected void fail(String message) throws UniLoadFailException {
        throw new UniLoadFailException(message);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        detachedObservable.cancelAll();
    }

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {

    }

    @Override
    public void onPre(int requestCode) {

    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) throws Exception {

    }

    @Override
    public void onUpdate(int requestCode, Object value) {

    }

    @Override
    public void onPost(int requestCode) {

    }

    @Override
    public void onFail(int requestCode, boolean isException, String message, Exception e) {

    }

    @Override
    public void onCancelled(int requestCode) {

    }

}
