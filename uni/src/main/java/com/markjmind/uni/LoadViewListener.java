package com.markjmind.uni;

import android.view.View;

/**
 * Created by codemasta on 2015-09-16.
 */
public interface LoadViewListener {
    public void loadCreate(int requestCode, View loadView);
    public void loadUpdate(int requestCode, View loadView, Object value);
    public void loadDestroy(int requestCode);
}
