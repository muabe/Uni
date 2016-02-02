package com.markjmind.uni;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.MapperInterface;
import com.markjmind.uni.viewer.UpdateEvent;
import com.markjmind.uni.viewer.ViewerBuilder;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class UniView extends FrameLayout implements UniInterface, BuildInterface, MapperInterface {
    private ViewGroup layout;
    private UniInterface uniInterface;
    private Mapper mapper;

    public UniView(Context context) {
        super(context);
        onCreateView(null);
    }

    public UniView(Context context, Object mappingObject, ViewGroup container) {
        super(context);
        mapping(new Mapper(this, mappingObject), container);
    }

    public UniView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //todo attrs에 layout이 있으면 onCreateView에 넣어주자
        onCreateView(null);
    }

    public UniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView(null);
    }

    private void onCreateView(ViewGroup layout){
        mapping(new Mapper(this, this), this);
    }

    @Override
    public void setLayout(ViewGroup layout) {
        this.layout = layout;
        if(this.layout !=null) {
            this.addView(this.layout);
            this.setLayoutParams(this.layout.getLayoutParams());
        }
    }

    public void mapping(Mapper mapper, ViewGroup container){
//        if(Mapper.hasLayout(uniFragment.getClass())) {
        this.mapper = mapper;
        if(layout==null) {
            int layoutId = mapper.injectionLayout();
            LayoutInflater inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            layout = (ViewGroup) inflater.inflate(layoutId, container, false);
        }
        setLayout(layout);
    }

    public ViewGroup getLayout(){
        return this.layout;
    }

    protected void setUniInterface(UniInterface uniInterface){
        this.uniInterface = uniInterface;
    }

    @Override
    public UniView getUniView() {
        return this;
    }

    @Override
    public UniInterface getUniInterface() {
        return null;
    }

    public void excute(int requestCode){
        mapper.injectionView();
        if(uniInterface==null) {
            this.onPost(requestCode);
        }else{
            uniInterface.onPost(requestCode);
        }
    }

    public void excute(){
        this.excute(-1);
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
