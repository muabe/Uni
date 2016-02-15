/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import com.markjmind.uni.hub.Store;
import com.markjmind.uni.viewer.ProgressViewListener;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-05
 */
public class BindConfig {
    protected Store<?> param = new Store<>();
    protected boolean isAsync;
    protected ProgressViewController progressController;



    public BindConfig(){
        progressController = new ProgressViewController();
    }

    /************************************************* 바인드 속성 관련 ************************************/

    public boolean isAsync() {
        return isAsync;
    }

    public BindConfig setAsync(boolean isAsync) {
        this.isAsync = isAsync;
        return this;
    }

    public BindConfig setProgressLayout(int R_layout_id, ProgressViewListener progressViewListener){
        progressController.setProgressView(R_layout_id, progressViewListener);
        return this;
    }

    public BindConfig setEnableProgress(boolean enable){
        progressController.setEnable(enable);
        return this;
    }


    /************************************************* 파라미터 관련 ************************************/

    /**
     * 다른 Viewer로 넘기는 파라미터를 설정한다.
     *
     * @param key   Parameter Key
     * @param value Parameter Value
     */
    public BindConfig addParam(String key, Object value) {
        param.add(key, value);
        return this;
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Value
     */
    public Object getParam(String key) {
        return param.get(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Value
     */
    public Object optParam(String key, Object defalut) {
        Object result = getParam(key);
        if (result == null)
            result = defalut;
        return result;
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter String Value
     */
    public String getParamString(String key) {
        return (String) getParam(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter String Value
     */
    public String optParamString(String key, String defalut) {
        return (String) optParam(key, defalut);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Int Value
     */
    public int getParamInt(String key) {
        return (Integer) getParam(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Int Value
     */
    public int optParamInt(String key, int defalut) {
        return (Integer) optParam(key, defalut);
    }

    /**
     * 다른 Viewer로 전달한 파라미터 Store를 받는다.
     *
     * @return Parameter store
     */
    public Store<?> getParamStore() {
        return param;
    }

    /**
     * 다른 Viewer로 전달한 파라미터 Store를 설정한다.
     */
    public BindConfig setParamStore(Store<Object> store) {
        param = store;
        return this;
    }


}
