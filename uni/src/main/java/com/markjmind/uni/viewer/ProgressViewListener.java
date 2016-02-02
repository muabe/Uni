package com.markjmind.uni.viewer;

import android.view.View;

/**
 * Created by codemasta on 2015-09-16.
 */
public interface ProgressViewListener {
    public void onStart(int requestCode, View loadView);
    public void onUpdate(int requestCode, View loadView, Object value);
    public void onDestroy(int requestCode, View loadView);
}
