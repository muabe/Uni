package com.markjmind.uni.boot;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.util.ReflectionUtil;

import java.util.ArrayList;

/**
 * Created by MarkJ on 2016-10-29.
 */

public abstract class UniBoot{
    private final int layoutID = R.layout.uni_boot_frame_layout;
    private RelativeLayout rootView;
    public IDs id = new IDs();
    public Views view = new Views();
    protected Activity activity;

    protected Point windowSize = new Point();

    public class IDs{
        public int top = R.id.uni_boot_frame_top;
        public int custom = R.id.uni_boot_frame_custom;
        public int home = R.id.uni_boot_frame_home;
        public int bottom = R.id.uni_boot_frame_bottom;
        public int left = R.id.uni_boot_frame_left;
        public int right = R.id.uni_boot_frame_right;
    }

    public class Views{
        public FrameLayout top;
        public FrameLayout custom;
        public FrameLayout home;
        public FrameLayout root_home;
        public FrameLayout bottom;
        public LinearLayout left;
        public LinearLayout right;

    }

    /**
     * Class<T>로 지정한 Type의 UniBoot 객체를 리턴한다.<br>
     * UniBoot를 사용하지 않았다면 null을 리턴한다.
     * @param boot UniBoot Type
     * @param <T> UniBoot를 상속 받은 Class
     * @return T Type의 UniBoot 객체
     */
    protected static <T extends UniBoot>T get(Activity activity, Class<T> boot){
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


    protected static <T extends UniBoot>T putContentView(Activity activity, Class<T> boot){
         return (T)ReflectionUtil.getInstance(boot).initLayout(activity);
    }

    protected static <T extends UniBoot>T putContentView(Activity activity, T boot){
        return (T)boot.initLayout(activity);
    }

    UniBoot initLayout(Activity activity){
        this.activity = activity;
        activity.setContentView(layoutID);
        rootView = (RelativeLayout)activity.findViewById(R.id.uni_boot_frame_root);
        view.top = (FrameLayout)activity.findViewById(id.top);
        view.custom = (FrameLayout)activity.findViewById(id.custom);
        view.home = (FrameLayout)activity.findViewById(id.home);
        view.bottom = (FrameLayout)activity.findViewById(id.bottom);
        view.left = (LinearLayout)activity.findViewById(id.left);
        view.right = (LinearLayout)activity.findViewById(id.right);
        view.root_home = (FrameLayout)activity.findViewById(R.id.uni_boot_frame_root_home);

        WindowManager wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(windowSize);

        view.left.getLayoutParams().width = windowSize.x;
        view.left.setX(-windowSize.x);

        view.right.getLayoutParams().width = windowSize.x;
        view.right.setX(windowSize.x);
        rootView.setTag(this);

        onAttach(activity);

        return this;
    }

    protected abstract void onAttach(Activity activity);

    public RelativeLayout getRootView(){
        return this.rootView;
    }

//    public FragmentBuilder getBuiler(int parentsID, UniFragment uniFragment){
//        FragmentBuilder builder = FragmentBuilder.getBuilder(activity, parentsID, uniFragment);
//        uniFragment.setUniBoot(this);
//        return builder;
//    }


    /********************** layout 확장 *********************/
    protected void setLayout(ViewGroup parentsView, int layoutID){
        parentsView.removeAllViews();
        ((LayoutInflater)parentsView.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(layoutID, parentsView);
    }

    protected void setTopLayout(int layoutID){
        setLayout(view.top, layoutID);
    }

    protected void setCustomLayout(int layoutID){
        setLayout(view.custom, layoutID);
    }

    protected void setHomeLayout(int layoutID){
        setLayout(view.home, layoutID);
    }

    protected void setBottomLayout(int layoutID){
        setLayout(view.bottom, layoutID);
    }

    protected void setLeftLayout(int layoutID){
        setLayout(view.left, layoutID);
    }

    protected void setRightLayout(int layoutID){
        setLayout(view.right, layoutID);
    }

    /********************** id 변경 *********************/
    protected void changeTopID(int topID){
        id.top = topID;
    }

    protected void changeCustomID(int customID){
        id.custom = customID;
    }

    protected void changeHomeID(int homeID){
        id.home = homeID;
    }

    protected void changeBottomID(int bottomID){
        id.bottom = bottomID;
    }

    protected void changeLeftID(int leftID){
        id.left = leftID;
    }

    protected void changeRightID(int rightID){
        id.right = rightID;
    }

    ArrayList<BackPressAdapter> backPressList = new ArrayList<>();
    public void addBackPressObserver(int parendtsID, BackPressObserver observer){
        backPressList.add(new BackPressAdapter(parendtsID, observer));
    }

    public boolean onBackPressed(){
        FragmentManager fragmentManager = activity.getFragmentManager();
        for(BackPressAdapter adapter : backPressList){
            if(adapter.isBackPressed(fragmentManager)){
                return true;
            }
        }
        return false;
    }

    public interface BackPressObserver{
        boolean isBackPressed(int stackCount, BackPressAdapter backPressAdapter);
    }
    public class BackPressAdapter{
        public int parentsID;
        public BackPressObserver observer;
        public String stackName;
        public FragmentManager fragmentManager;
        public UniFragment currentFragment;
        public BackPressAdapter(int parendtsID, BackPressObserver observer){
            this.parentsID = parendtsID;
            this.observer = observer;
            this.stackName = FragmentBuilder.getDefalutStack(parendtsID);
        }

        boolean isBackPressed(FragmentManager fragmentManager){
            this.fragmentManager = fragmentManager;
            this.currentFragment = (UniFragment)fragmentManager.findFragmentById(parentsID);
            return observer.isBackPressed(getBackStackEntryCount(fragmentManager, stackName), this);
        }

        public void backPress(){
            currentFragment.onBackPressed();
        }
    }

    protected int getBackStackEntryCount(FragmentManager fragmentManager, String tag) {
        int count = 0;
        int size = fragmentManager.getBackStackEntryCount();
        for ( int entry = 0; entry < size; entry++ ) {
            String name = fragmentManager.getBackStackEntryAt(entry).getName();
            Log.i("dd",tag+":"+name+" "+(entry+1)+"/"+size);
            if ( tag.equals(name) ){
                count++;
            }
        }
        return count;
    }
}
