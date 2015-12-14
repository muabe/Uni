package com.markjmind.uni;

import com.markjmind.uni.hub.Store;

/**
 * Created by codemasta on 2015-10-23.
 */
public class RefreshBuilder{

    private Viewer jv;
    private ViewerBuilder builder;

    public RefreshBuilder(Viewer jv){
        this.jv = jv;
        this.builder = jv.builder;
    }

    public void onLoad(){
        jv.runLoad();
    }

    public void onPost(){
        jv.runPost();
    }

    public RefreshBuilder setPreLayout(boolean isPreLayout){
        builder.setPreLayout(isPreLayout);
        return this;
    }

    public RefreshBuilder setLoadLayout(int R_layout_id, ProgressViewListener progressViewListener){
        builder.setProgressLayout(R_layout_id, progressViewListener);
        return this;
    }

    public RefreshBuilder setEnableLoadLayout(boolean enable){
        builder.setEnableLoadLayout(enable);
        return this;
    }

    public RefreshBuilder setRequestCode(int requestCode){
        builder.setRequestCode(requestCode);
        return this;
    }

    /**
     * 다른 Viewer로 넘기는 파라미터를 설정한다.
     * @param key Parameter Key
     * @param value Parameter Value
     */
    public RefreshBuilder addParam(String key, Object value){
        builder.addParam(key, value);
        return this;
    }

    /**
     *  다른 Viewer로 전달한 파라미터 Store를 설정한다.
     */
    public RefreshBuilder setParamStore(Store<?> store){
        builder.setParamStore(store);
        return this;
    }

}
