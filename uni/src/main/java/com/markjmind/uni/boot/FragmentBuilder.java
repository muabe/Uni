package com.markjmind.uni.boot;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

import com.markjmind.uni.UniFragment;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-02
 */
public class FragmentBuilder {

    private Activity activity;
    private UniFragment fragment;
    private int parentsId;
    private String tag;
    private boolean allowingStateLoss = false;

    private FragmentBuilder(Activity activity, int parentsId, UniFragment uniFragment){
        fragment = uniFragment;
        this.activity = activity;
        this.parentsId = parentsId;

    }

    private FragmentBuilder(Activity activity, int parentsId, Class<? extends UniFragment> fragmentClass){
        fragment = getFragmentInstance(fragmentClass);
        this.activity = activity;
        this.parentsId = parentsId;
    }

    private <T extends UniFragment>T getFragmentInstance(Class<T> fragmentClass){
        T frag = null;
        try {
            frag = (T)fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)frag;
    }

    public static FragmentBuilder getBuilder(Activity activity, int parentsId, Class<? extends UniFragment> fragmentClass){
        return new FragmentBuilder(activity, parentsId, fragmentClass);
    }

    public static FragmentBuilder getBuilder(Activity activity, int parentsId, UniFragment uniFragment){
        return new FragmentBuilder(activity, parentsId, uniFragment);
    }

    public FragmentManager getFragmentManager(){
        return activity.getFragmentManager();
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
    }

    public void replace(boolean isHistory){
        fragment.setRefreshBackStack(true);
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction()
                .replace(parentsId, fragment, tag);
        String stackName = fragment.getClass().getName();
        if(isHistory){
            if ( allowingStateLoss ) {
                transaction.addToBackStack(stackName).commitAllowingStateLoss();
            }
            else {
                transaction.addToBackStack(stackName).commit();
            }
        }else{
            if ( allowingStateLoss || !isScreenOn() )  {
                transaction.commitAllowingStateLoss();
            }
            else {
                try {
                    transaction.commit();
                } catch ( IllegalStateException e ) {
                    e.printStackTrace();
                    transaction.commitAllowingStateLoss();
                }
            }
        }
    }

    public UniFragment getFragment(){
        return fragment;
    }

    public void replace(){
        this.replace(true);
    }

    public FragmentBuilder addParam(String key, Object value){
        fragment.param.add(key, value);
        return this;
    }

    public FragmentBuilder addHistory(Class<? extends UniFragment>... fragmentClasses){
        if(fragmentClasses.length >0) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            for (Class<? extends UniFragment> fragmentClass : fragmentClasses) {
                String stackName = fragmentClass.getName();
                transaction.addToBackStack(stackName);
            }
            if (allowingStateLoss) {
                transaction.commitAllowingStateLoss();
            }
            else{
                transaction.commit();
            }
        }
        return this;
    }

    public FragmentBuilder addHistory(UniFragment... uniFragment){
        if(uniFragment.length >0) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            for (UniFragment anUniFragment : uniFragment) {
                String stackName = anUniFragment.getClass().getName();
                transaction.addToBackStack(stackName);
            }
            if (allowingStateLoss) {
                transaction.commitAllowingStateLoss();
            }
            else{
                transaction.commit();
            }
        }
        return this;
    }

    /**
     * 태그지정 옵션
     * @param tag
     * @return
     */
    public FragmentBuilder setTag(String tag){
        this.tag = tag;
        return this;
    }

    /**
     * Sync 지정옵션
     * @param isSync
     * @return
     */
    public FragmentBuilder setSync(boolean isSync){
        fragment.setAsync(isSync);
        return this;
    }

    /**
     * @param isAllowingStateLoss
     * @return
     */
    public FragmentBuilder setAllowingStateLoss(boolean isAllowingStateLoss) {
        this.allowingStateLoss = isAllowingStateLoss;
        return this;
    }
}

