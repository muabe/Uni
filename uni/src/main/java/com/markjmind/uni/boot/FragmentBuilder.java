package com.markjmind.uni.boot;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.util.ReflectionUtil;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-02
 */
public class FragmentBuilder {

    private Activity activity;
//    private UniFragment fragment;
    private int parentsId;
    private String tag;
    private boolean allowingStateLoss = false;

    private FragmentBuilder(Activity activity, int parentsId){
        this.activity = activity;
        this.parentsId = parentsId;
    }


    public static <T extends UniBoot>T setContentView(Activity activity, Class<T> boot){
        return (T) ReflectionUtil.getInstance(boot).initLayout(activity);
    }

    /**
     * Class<T>로 지정한 Type의 UniBoot 객체를 리턴한다.<br>
     * UniBoot를 사용하지 않았다면 null을 리턴한다.
     * @param boot UniBoot Type
     * @param <T> UniBoot를 상속 받은 Class
     * @return T Type의 UniBoot 객체
     */
    public static <T extends UniBoot>T getBoot(Activity activity, Class<T> boot){
        View rootView = activity.findViewById(R.id.uni_boot_frame_root);
        T bootStrap = null;
        if(rootView!=null){
            Log.e("FragmentBuilder", "기존 부트 사용~~~~");
            bootStrap = (T)rootView.getTag();
            if(bootStrap==null){
                Log.e("FragmentBuilder", "기존 부트 새로만들었다~~~");
                bootStrap = ReflectionUtil.getInstance(boot);
                if(bootStrap!=null){
                    bootStrap.initLayout(activity);
                }
            }
        }
        return bootStrap;
    }

    public static FragmentBuilder getBuilder(Activity activity, int parentsViewID){
        return new FragmentBuilder(activity, parentsViewID);
    }

    public static FragmentBuilder getBuilder(UniFragment currFragment, int parentsViewID){
        return new FragmentBuilder(currFragment.getActivity(), parentsViewID);
    }

    public static FragmentBuilder getBuilder(UniFragment currFragment){
        return FragmentBuilder.getBuilder(currFragment, currFragment.getParentsViewID());
    }

    public void replace(UniFragment uniFragment, boolean isHistory){
        uniFragment.setRefreshBackStack(true);
        String stackName = uniFragment.getClass().getName();
        uniFragment.setParentsViewID(parentsId);

        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction()
                .replace(parentsId, uniFragment, tag);
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

    public void replace(UniFragment uniFragment){
        this.replace(uniFragment, true);
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
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
//    public FragmentBuilder setSync(boolean isSync){
//        fragment.setAsync(isSync);
//        return this;
//    }

    /**
     * @param isAllowingStateLoss
     * @return
     */
    public FragmentBuilder setAllowingStateLoss(boolean isAllowingStateLoss) {
        this.allowingStateLoss = isAllowingStateLoss;
        return this;
    }

    public FragmentManager getFragmentManager(){
        return activity.getFragmentManager();
    }

}

