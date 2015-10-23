package com.markjmind.uni;

import android.view.View;

/**
 * Created by codemasta on 2015-09-16.
 */
public abstract class UpdateListener{
    public abstract void onCreate(int requestCode, View loadView);
    public void onUpdate(int requestCode, View loadView, Object value){}
    public void onDestroy(int requestCode){}
}
