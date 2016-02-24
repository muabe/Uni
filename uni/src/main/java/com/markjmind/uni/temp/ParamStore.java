package com.markjmind.uni.temp;

import com.markjmind.uni.common.Store;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
class ParamStore<T> extends Store implements ParamInterface<T>{

    private T obj;
    public ParamStore(T obj){
        this.obj = obj;
    }

    public T addParam(String key, Object value) {
        add(key, value);
        return obj;
    }

    public Object getParam(String key) {
        return get(key);
    }

    public Object optParam(String key, Object defalut) {
        Object result = getParam(key);
        if (result == null)
            result = defalut;
        return result;
    }

    public String getParamString(String key) {
        return (String) getParam(key);
    }

    public String optParamString(String key, String defalut) {
        return (String) optParam(key, defalut);
    }

    public int getParamInt(String key) {
        return (Integer) getParam(key);
    }

    public int optParamInt(String key, int defalut) {
        return (Integer) optParam(key, defalut);
    }

}
