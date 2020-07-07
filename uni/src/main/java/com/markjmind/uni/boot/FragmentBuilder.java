package com.markjmind.uni.boot;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.markjmind.uni.OnBackpressCallback;
import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.util.ReflectionUtil;

import java.util.ArrayList;
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
    private FragmentActivity activity;

    private FragmentManager fragmentManager;
//    private FragmentTransaction transaction;
    private boolean allowingStateLoss = false;
    private boolean history = true;
    private Store param;
    private OnBackpressCallback finishedListener;
    private int parentsViewID = -1;
    private int inAnimRes=-1, outAnimRes=-1;

    protected FragmentBuilder(FragmentActivity activity){
        this.activity = activity;
        this.finishedListener = null;
    }

    public static <T extends UniBoot>T setContentView(FragmentActivity activity, Class<T> boot){
        return (T) ReflectionUtil.getInstance(boot).initLayout(activity);
    }

    /**
     * Class<T>로 지정한 Type의 UniBoot 객체를 리턴한다.<br>
     * UniBoot를 사용하지 않았다면 null을 리턴한다.
     * @param boot UniBoot Type
     * @param <T> UniBoot를 상속 받은 Class
     * @return T Type의 UniBoot 객체
     */
    public static <T extends UniBoot>T getBoot(FragmentActivity activity, Class<T> boot){
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

    public static FragmentBuilder getBuilder(FragmentActivity activity){
        return new FragmentBuilder(activity);
    }

    public static FragmentBuilder getBuilder(UniFragment uniFragment){
        return new FragmentBuilder(uniFragment.getActivity()).setParentsId(uniFragment.getParentsViewID());
    }

    public FragmentBuilder setParentsId(int id){
        this.parentsViewID = id;
        return this;
    }

    public int getParentsId(){
        return parentsViewID;
    }

    public void replace(UniFragment uniFragment, String tag){
        if(parentsViewID < 0) {
            throw new RuntimeException(new NullPointerException("parentsID is null"));
        }else{
            this.replace(getParentsId(), uniFragment, tag);
        }
    }

    public void replace(UniFragment uniFragment){
        this.replace(uniFragment, FragmentBuilder.getDefalutTag(getParentsId()));
    }

    public void replace(int parentsID, UniFragment uniFragment, String tag){
        uniFragment.setRefreshBackStack(true);
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        UniFragment currFragment = getCurrentFragment(parentsID);
         setOption(parentsID, uniFragment);
        if(finishedListener != null) {
            currFragment.setRefreshBackStack(false);
            finishedListener.setCallbackClass(currFragment.getClass());
        }
        FragmentTransaction transaction = getTransaction();
        if(inAnimRes > 0 && outAnimRes > 0){
//            transaction.setCustomAnimations(inAnimRes, inAnimRes);
        }

//        안드로이드 애니메이션이 돌고 있는동안 replace가 되면 에러나는 버그 때문에 추가된 코드
//        if(outAnimRes > 0 ) {
//            if (currFragment != null && currFragment.getView() != null) {
//                ViewParent parent = currFragment.getView().getParent();
//                if (parent instanceof ViewGroup) {
//                    ViewGroup parentViewGroup = (ViewGroup) parent;
//                    parentViewGroup.removeAllViews();
//
//                }
//            }
//        }


        if(currFragment != null){
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.hide(currFragment);
            transaction.remove(currFragment);
        }

        transaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(parentsID, uniFragment, tag);


        transaction.show(uniFragment);

        if(history){
            if ( allowingStateLoss ) {
                transaction.addToBackStack(stackName)
                        .commitAllowingStateLoss();
            }
            else {
                //아래걸 써야하는데 폰이 잠김(화면꺼짐) 상태에서는 fragment가 replace가 안됨
                transaction.addToBackStack(stackName).commit();
            }
        }else{
            if ( allowingStateLoss || !isScreenOn() )  {
                transaction.commitAllowingStateLoss();
            }
            else {
                try {
                    transaction.commitAllowingStateLoss();
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
        if(finishedListener!=null) {
            uniFragment.setPreOnBackpressListener(finishedListener);
        }
    }

    public UniFragment getCurrentFragment(int parentsID){
        return getCurrentFragment(FragmentBuilder.getDefalutTag(parentsID));
    }

    private UniFragment getCurrentFragment(String tagName){
        return (UniFragment)(getFragmentManager().findFragmentByTag(tagName));
    }

    public void replace(int parentsID, UniFragment uniFragment){
//        this.replace(parentsID, uniFragment, ""+uniFragment.getClass());
        this.replace(parentsID, uniFragment, FragmentBuilder.getDefalutTag(parentsID));
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
    }




    public FragmentBuilder popBackStackClear(boolean clearAll, int parentsID){
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        if(containStackEntry(stackName)) {
            try {
                int entry = getFragmentManager().getBackStackEntryCount();
                Stack<UniFragment> uniFragments = new Stack<>();
                for (int i = entry - 1; i >= 0; i--) {
                    String tagName = getFragmentManager().getBackStackEntryAt(i).getName();
                    if (tagName.contains(stackName)) {
                        getFragmentManager().popBackStackImmediate();
                    } else {

                        UniFragment uniFragment = getCurrentFragment(tagName);
                        uniFragments.push(uniFragment);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.remove(uniFragment);
                        getFragmentManager().executePendingTransactions();
                        transaction.commitAllowingStateLoss();
                        getFragmentManager().popBackStackImmediate();
                    }
                }
                if (clearAll) {
                    getFragmentManager().beginTransaction()
                            .remove(getCurrentFragment(stackName))
                            .commitAllowingStateLoss();
                }

                int stackCount = uniFragments.size();
                if (stackCount > 0) {
                    for (int i = 0; i < stackCount; i++) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        UniFragment uniFragment = uniFragments.pop();
                        uniFragment.setRefreshBackStack(false);
                        String tagName = getDefalutTag(uniFragment.getParentsViewID());
                        transaction.addToBackStack(tagName);
                        transaction.replace(uniFragment.getParentsViewID(), uniFragment, tagName);
                        transaction.commitAllowingStateLoss();
                    }
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
                if(stackName!=null) {
                    UniFragment currentFragment = getCurrentFragment(stackName);
                    if (currentFragment != null) {
                        currentFragment.getFragmentStack().clearHistoryOnResume(clearAll, parentsID);
                    }
                }
            }
        }
        return this;
    }


    public FragmentBuilder popBackStackClear(boolean clearAll) {
        if(clearAll) {
            int entry = getFragmentManager().getBackStackEntryCount();
            ArrayList<String> stackNames = new ArrayList<>();
            for (int i = entry - 1; i >= 0; i--) {
                String tagName = getFragmentManager().getBackStackEntryAt(i).getName();
                if (!stackNames.contains(tagName)) {
                    stackNames.add(tagName);
                }
            }
            try {
                getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                for (String tagName : stackNames) {
                    getFragmentManager().beginTransaction()
                            .remove(getCurrentFragment(tagName))
                            .commitAllowingStateLoss();
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
                String stackName = getFirstStackName();
                if(stackName!=null) {
                    UniFragment currentFragment = getCurrentFragment(stackName);
                    if (currentFragment != null) {
                        currentFragment.getFragmentStack().clearHistoryOnResume(true, null);
                    }
                }
            }

        }else{
            popBackStackClear();
        }

        return this;
    }

    public FragmentBuilder historyAllClear(){
        return popBackStackClear(true);
    }

    private FragmentBuilder popBackStackClear() {
        try {
            getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }catch (IllegalStateException e){
            e.printStackTrace();
            String stackName = getFirstStackName();
            if(stackName!=null) {
                UniFragment currentFragment = getCurrentFragment(stackName);
                if (currentFragment != null) {
                    currentFragment.getFragmentStack().clearHistoryOnResume(false, null);
                }
            }
        }

        return this;
    }

    public boolean popBackStack(int parentsID) {
        boolean result = false;
        String stackName = FragmentBuilder.getDefalutStack(parentsID);
        if(containStackEntry(stackName)) {
            try {
                int entry = getFragmentManager().getBackStackEntryCount();
                Stack<UniFragment> uniFragments = new Stack<>();
                for (int i = entry - 1; i >= 0; i--) {
                    String tagName = getFragmentManager().getBackStackEntryAt(i).getName();
                    if (tagName.contains(stackName)) {
                        popBackStackImmediateToCallback(stackName, parentsID);
                        result = true;
                        break;
                    } else {
                        UniFragment uniFragment = getCurrentFragment(tagName);
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
                        String tagName = getDefalutTag(uniFragment.getParentsViewID());
                        transaction.addToBackStack(tagName);
                        transaction.replace(uniFragment.getParentsViewID(), uniFragment, tagName);
                        transaction.commitAllowingStateLoss();
                    }
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
                popStackOnResume(stackName, parentsID);
            }
        }

        return result;
    }

    private void popStackOnResume(String stackName, int parentsID){
        UniFragment currentFragment;
        if(stackName == null) {
            currentFragment = getCurrentFragment(parentsID);
        }else{
            currentFragment = getCurrentFragment(stackName);
        }
        if (currentFragment != null) {
            try{
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                getFragmentManager().executePendingTransactions();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.show(currentFragment);
                transaction.commitNowAllowingStateLoss();

            }catch (Exception ex){
                currentFragment.getFragmentStack().popStackOnResume(parentsID);
                ex.printStackTrace();
            }
        }
    }

    public boolean popBackStack() {
        if(getFragmentManager().getBackStackEntryCount()>0) {
            try {
                popBackStackImmediateToCallback(null, getParentsId());

            }catch (IllegalStateException e){
                e.printStackTrace();
                String stackName = getFirstStackName();
                popStackOnResume(stackName, getParentsId());
            }
            return true;
        }
        return false;
    }

    private void popBackStackImmediateToCallback(String stackName, int parentsViewID){
        UniFragment currFragment = getCurrentFragment(parentsViewID);
        OnBackpressCallback backpressCallback = currFragment.getPreOnBackpressListener();

        try {
            getFragmentManager().popBackStackImmediate();
//            UniFragment uniFragment = getCurrentFragment(parentsViewID);
//            if(uniFragment!=null) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                getFragmentManager().executePendingTransactions();
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                transaction.show(uniFragment);
//                transaction.commitNowAllowingStateLoss();
//
//            }
        }catch (Exception e){
            if(currFragment!=null){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                getFragmentManager().executePendingTransactions();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.hide(currFragment);
                transaction.remove(currFragment);
                transaction.commitNowAllowingStateLoss();

            }
            popStackOnResume(stackName, parentsViewID);
        }
        if (backpressCallback != null) {
            getCurrentFragment(parentsViewID).setOriginOnBackpressCallback(backpressCallback);
        }

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

    private String getFirstStackName(){
        int entry = getFragmentManager().getBackStackEntryCount();
        if(entry>0){
            return getFragmentManager().getBackStackEntryAt(entry-1).getName();
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

    public FragmentBuilder setAnimations(int inRes, int outRes){
        this.inAnimRes = inRes;
        this.outAnimRes = outRes;
        return this;
    }

    public FragmentTransaction getTransaction(){
//        if(transaction == null){
//            this.transaction = getFragmentManager().beginTransaction();
//        }
        return getFragmentManager().beginTransaction();
    }

    public FragmentBuilder setOnBackCallback(OnBackpressCallback onBackCallback){
        this.finishedListener = onBackCallback;
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
            fragmentManager = activity.getSupportFragmentManager();
        }
        return fragmentManager;
    }

    public static String getDefalutStack(int parentsID){
        return DEFALUT_TAG + parentsID;
    }

    public static String getDefalutTag(int parentsID){
        return DEFALUT_TAG + parentsID;
    }


}

