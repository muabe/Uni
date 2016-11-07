package com.markjmind.uni.boot;

import android.app.Activity;

import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;

/**
 * Created by MarkJ on 2016-10-30.
 */

public class SimpleBoot extends UniBoot{
    public static int HOME;

    /************************************** Must implement ************************************/

    public static SimpleBoot get(Activity activity){
        return UniBoot.get(activity, SimpleBoot.class);
    }

    public static SimpleBoot get(UniFragment uniFragment){
        return UniBoot.get(uniFragment.getActivity(), SimpleBoot.class);
    }

    public static SimpleBoot putContentView(Activity activity){
        return UniBoot.putContentView(activity, SimpleBoot.class);
    }


    @Override
    public void onAttach(Activity activity) {
        setCustomLayout(R.layout.uni_boot_frame_layout);
        SimpleBoot.HOME = id.home;


        getRootView().removeView(view.top);
        getRootView().removeView(view.bottom);
        getRootView().removeView(view.left);
        getRootView().removeView(view.right);


    }

    /************************************** End ************************************/
}
