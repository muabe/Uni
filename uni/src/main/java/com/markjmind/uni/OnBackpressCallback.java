package com.markjmind.uni;

import com.markjmind.uni.common.Store;

public abstract class OnBackpressCallback<T extends UniFragment> {
    Class<?> callbackClass;
    public Store<?> result = new Store<>();
    public abstract void callback(T fragment, Store<?> result);


    public void backEvent(T fragment){
        callback(fragment, result);
    }

    public void setCallbackClass(Class<?> callbackClass) {
        this.callbackClass = callbackClass;
    }
}
