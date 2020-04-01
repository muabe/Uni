package com.markjmind.uni.boot;

import com.markjmind.uni.UniFragment;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by MarkJ on 2016-10-30.
 */

public class SimpleBoot extends UniBoot{
    public static int HOME;

    /************************************** Must implement ************************************/

    public static SimpleBoot get(FragmentActivity activity){
        return UniBoot.get(activity, SimpleBoot.class);
    }

    public static SimpleBoot get(UniFragment uniFragment){
        return UniBoot.get(uniFragment.getActivity(), SimpleBoot.class);
    }

    public static SimpleBoot putContentView(FragmentActivity activity){
        return UniBoot.putContentView(activity, SimpleBoot.class);
    }


    @Override
    public void onAttach(FragmentActivity activity) {
        SimpleBoot.HOME = id.home;

        getRootView().removeView(view.top);
        getRootView().removeView(view.bottom);
        getRootView().removeView(view.left);
        getRootView().removeView(view.right);

        addBackPressObserver(SimpleBoot.HOME, new BackPressObserver() {
            @Override
            public boolean isBackPressed(int stackCount, UniBoot.BackPressAdapter backPressAdapter) {
                if(stackCount > 0){
                    backPressAdapter.backPress();
                    return true;
                }else{
                    return false;
                }
            }
        });

    }

    public static boolean onBackPressed(FragmentActivity activity){
        SimpleBoot boot = SimpleBoot.get(activity);
        return boot.onBackPressed();
    }

    /************************************** End ************************************/


    public SimpleBoot setHomeFragment(UniFragment uniFragment){
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .replace(SimpleBoot.HOME, uniFragment);
        return this;
    }
}
