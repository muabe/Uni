package com.markjmind.libox.common;

import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;

/**
 * Created by codemasta on 2015-09-14.
 */
public class DMViewer extends Viewer {

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        build.setAsync(true).setPreLayout(true);
    }

    @Override
    public void onPre(int requestCode) {

    }

    @Override
    public boolean onLoad(int requestCode, UpdateEvent event) {
        return true;
    }

    @Override
    public void onPost(int requestCode) {
    }

    @Override
    public void onFail(Integer requestCode) {

    }

}
