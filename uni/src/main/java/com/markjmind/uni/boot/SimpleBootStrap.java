package com.markjmind.uni.boot;

import android.app.Activity;

import com.markjmind.uni.UniFragment;

/**
 * Created by MarkJ on 2016-10-30.
 */

public class SimpleBootStrap extends UniBoot{

    @Override
    protected void onAttach(Activity activity) {
        getRootView().removeView(view.top);
        getRootView().removeView(view.bottom);
        getRootView().removeView(view.left);
        getRootView().removeView(view.right);
    }

    public FragmentBuilder getHomeBuiler(UniFragment uniFragment){
        return getBuiler(id.home, uniFragment);
    }
}
