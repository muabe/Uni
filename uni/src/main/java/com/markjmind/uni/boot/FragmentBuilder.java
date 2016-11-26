package com.markjmind.uni.boot;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;

import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.util.ReflectionUtil;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-02
 */
public class FragmentBuilder {
    private static final String DEFALUT_TAG = "UNI";
    private Activity activity;
//    private UniFragment fragment;
    private FragmentTransaction transaction;
    private boolean allowingStateLoss = false;
    private boolean history = true;
    private Store param;
    private UniFragment.OnFinishedListener finishedListener;

    protected FragmentBuilder(Activity activity){
        this.activity = activity;
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
            bootStrap = (T)rootView.getTag();
            if(bootStrap==null){
                bootStrap = ReflectionUtil.getInstance(boot);
                if(bootStrap!=null){
                    bootStrap.initLayout(activity);
                }
            }
        }
        return bootStrap;
    }

    public static FragmentBuilder getBuilder(Activity activity){
        return new FragmentBuilder(activity);
    }

    public static FragmentBuilder getBuilder(UniFragment uniFragment){
        return new FragmentBuilder(uniFragment.getActivity());
    }

    public void replace(int parentsID, UniFragment uniFragment, String tag){
        uniFragment.setRefreshBackStack(true);
        String stackName = FragmentBuilder.getDefalutStack(parentsID);

        setOption(parentsID, uniFragment);

        FragmentTransaction transaction = getTransaction()
                .replace(parentsID, uniFragment, tag);
        if(history){
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
                }
            }
        }
    }

    private void setOption(int parentsID, UniFragment uniFragment){
        if(param!=null){
            uniFragment.param.putAll(param);
        }
        uniFragment.setParentsViewID(parentsID);
        if(finishedListener != null){
            uniFragment.setOnFinishedListener(finishedListener);
        }
    }

    public void replace(int parentsID, UniFragment uniFragment){
        this.replace(parentsID, uniFragment, ""+uniFragment.getClass());
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
    }

    public FragmentBuilder clearHistory(int parentsID){
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        if(isScreenOn()) {
            getFragmentManager().popBackStackImmediate(stackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return this;
    }

    public FragmentBuilder setHistory(boolean isHistory){
        history = isHistory;
        return this;
    }

    public FragmentBuilder addParam(String key, Object value){
        if(param==null){
            param = new Store();
        }
        param.add(key, value);
        return this;
    }

    public FragmentTransaction getTransaction(){
        if(transaction == null){
            this.transaction = activity.getFragmentManager().beginTransaction();
        }
        return transaction;
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

    public FragmentBuilder setOnFinishedListener(UniFragment.OnFinishedListener finishedListener){
        this.finishedListener = finishedListener;
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

    public static String getDefalutStack(int parentsID){
        return DEFALUT_TAG+parentsID;
    }


}

