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
import com.markjmind.uni.common.Store;
import com.markjmind.uni.util.ReflectionUtil;

import java.util.Stack;

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

    private FragmentManager fragmentManager;
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
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(parentsID, uniFragment, tag);
        if(history){
            UniFragment currentFragment = getCurrentFragment(parentsID);
            if(currentFragment!=null) {
                uniFragment.getFragmentStack().index = getStackEntryCount(stackName);
            }
            stackName = uniFragment.getFragmentStack().getName(stackName);

            if ( allowingStateLoss ) {
                transaction.addToBackStack(stackName)
                        .commitAllowingStateLoss();
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

    public UniFragment getCurrentFragment(int parentsID){
        return getCurrentFragment(FragmentBuilder.getDefalutStack(parentsID));
    }

    private UniFragment getCurrentFragment(String tagName){
        return (UniFragment)(getFragmentManager().findFragmentByTag(tagName));
    }

    public void replace(int parentsID, UniFragment uniFragment){
//        this.replace(parentsID, uniFragment, ""+uniFragment.getClass());
        this.replace(parentsID, uniFragment, FragmentBuilder.getDefalutStack(parentsID));
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
    }




    public FragmentBuilder popBackStackClear(int parentsID){
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        if(containStackEntry(stackName)) {
            int entry = getFragmentManager().getBackStackEntryCount();
            Stack<UniFragment> uniFragments = new Stack<>();
            for (int i = entry - 1; i >= 0; i--) {
                String tagName = getFragmentManager().getBackStackEntryAt(i).getName();
                if (tagName.contains(stackName)) {
                    getFragmentManager().popBackStackImmediate();
                } else {
                    Log.e("dd", "tagName:" + getCurrentFragment(tagName.split("_")[0]).getClass());
                    Log.e("dd", "tagName:" + tagName);

                    UniFragment uniFragment = getCurrentFragment(tagName.split("_")[0]);
                    uniFragments.push(uniFragment);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.remove(uniFragment);
                    getFragmentManager().executePendingTransactions();
                    transaction.commitAllowingStateLoss();
                    getFragmentManager().popBackStackImmediate();


                }
            }
            getFragmentManager().beginTransaction()
                .remove(getCurrentFragment(stackName))
                .commitAllowingStateLoss();

            int stackCount = uniFragments.size();
            if (stackCount > 0) {
                for (int i = 0; i < stackCount; i++) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    UniFragment uniFragment = uniFragments.pop();
                    uniFragment.setRefreshBackStack(false);
                    stackName = getDefalutStack(uniFragment.getParentsViewID());
                    uniFragment.getFragmentStack().index = i;
                    transaction.addToBackStack(uniFragment.getFragmentStack().getName(stackName));

                    Log.e("dd", "tagName:" + uniFragment.getClass());
                    Log.e("dd", "gettagName:" + uniFragment.getFragmentStack().getName(stackName));
                    transaction.replace(uniFragment.getParentsViewID(), uniFragment, stackName);
                    transaction.commitAllowingStateLoss();
                }
            }
        }

        return this;
    }

    public boolean popBackStack(int parentsID) {
        boolean result = false;
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        if(containStackEntry(stackName)) {
            int entry = getFragmentManager().getBackStackEntryCount();
            Stack<UniFragment> uniFragments = new Stack<>();
            for (int i = entry - 1; i >= 0; i--) {
                String tagName = getFragmentManager().getBackStackEntryAt(i).getName();
                if (tagName.contains(stackName)) {
                    getFragmentManager().popBackStackImmediate();
                    result = true;
                    break;
                } else {
                    UniFragment uniFragment = getCurrentFragment(tagName.split("_")[0]);
                    uniFragments.push(uniFragment);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.remove(uniFragment);
                    getFragmentManager().executePendingTransactions();
                    transaction.commitAllowingStateLoss();
                    getFragmentManager().popBackStackImmediate();


                }
            }
            int stackCount = uniFragments.size();
            if (stackCount > 0) {
                for (int i = 0; i < stackCount; i++) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    UniFragment uniFragment = uniFragments.pop();
                    uniFragment.setRefreshBackStack(false);
                    stackName = getDefalutStack(uniFragment.getParentsViewID());
                    uniFragment.getFragmentStack().index = getStackEntryCount(stackName) + i;
                    transaction.addToBackStack(uniFragment.getFragmentStack().getName(stackName));
                    transaction.replace(uniFragment.getParentsViewID(), uniFragment, stackName);
                    transaction.commitAllowingStateLoss();
                }
            }
        }

        return result;
    }

    public void popBackStack() {
        if(getFragmentManager().getBackStackEntryCount()>0) {
            try {
                getFragmentManager().popBackStackImmediate();
            }catch (IllegalStateException e){
                int entry = getFragmentManager().getBackStackEntryCount();
                for (int i = entry - 1; i >= 0; i--) {
                    getCurrentFragment(getFragmentManager().getBackStackEntryAt(i).getName()).getFragmentStack().popStackOnResume();
                }
            }
        }
    }


    private FragmentBuilder popBackStackClear() {
        String stackName = getLastStackName();
        if(stackName!=null) {
            try {
                getFragmentManager().popBackStackImmediate(stackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }catch (IllegalStateException e){
                e.printStackTrace();
                UniFragment currentFragment = getCurrentFragment(stackName);
                if(currentFragment!=null) {
                    currentFragment.getFragmentStack().clearHistoryOnResume();
                }
            }
        }
        return this;
    }

    private int getStackEntryCount(String stackName){
        int entry = getFragmentManager().getBackStackEntryCount();
        int count = 0;
        for (int i = entry - 1; i >= 0; i--) {
            if (getFragmentManager().getBackStackEntryAt(i).getName().contains(stackName)) {
                count++;
            }
        }
        return count;
    }

    private boolean equalsStackEntry(String stackName){
        int entry = getFragmentManager().getBackStackEntryCount();
        for (int i = entry - 1; i >= 0; i--) {
            if (getFragmentManager().getBackStackEntryAt(i).getName().equals(stackName)) {
                return true;
            }
        }
        return false;
    }

    private boolean containStackEntry(String stackName){
        int entry = getFragmentManager().getBackStackEntryCount();
        for (int i = entry - 1; i >= 0; i--) {
            if (getFragmentManager().getBackStackEntryAt(i).getName().contains(stackName)) {
                return true;
            }
        }
        return false;
    }

    private String getLastStackName(){
        int entry = getFragmentManager().getBackStackEntryCount();
        if(entry>0){
            return getFragmentManager().getBackStackEntryAt(0).getName();
        }
        return null;
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
            this.transaction = getFragmentManager().beginTransaction();
        }
        return transaction;
    }

     public FragmentBuilder addHistory(int parentsID, UniFragment... uniFragment){
        if(uniFragment.length >0) {
            String stackName = getDefalutStack(parentsID);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            for (UniFragment anUniFragment : uniFragment) {
                anUniFragment.getFragmentStack().index = getStackEntryCount(stackName);
                transaction.addToBackStack(anUniFragment.getFragmentStack().getName(stackName));
                transaction.replace(parentsID, anUniFragment, stackName);
            }
            transaction.commit();
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
        if(fragmentManager == null){
            fragmentManager = activity.getFragmentManager();
        }
        return fragmentManager;
    }

    public static String getDefalutStack(int parentsID){
        return DEFALUT_TAG+parentsID;
    }


}

