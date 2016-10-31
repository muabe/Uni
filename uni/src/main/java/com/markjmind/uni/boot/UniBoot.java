package com.markjmind.uni.boot;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.markjmind.uni.R;
import com.markjmind.uni.UniFragment;

/**
 * Created by MarkJ on 2016-10-29.
 */

public abstract class UniBoot {
    private final int layoutID = R.layout.uni_boot_frame_layout;
    private RelativeLayout rootView;
    public IDs id = new IDs();
    public Views view = new Views();
    private Activity activity;

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

    public static <T>T attachContentView(Activity activity, Class<T> boot){
        try {
            return (T)(((UniBoot)boot.newInstance()).initLayout(activity));
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private UniBoot initLayout(Activity activity){
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

        onAttach(activity);

        return this;
    }

    protected abstract void onAttach(Activity activity);



    public RelativeLayout getRootView(){
        return this.rootView;
    }

    public FragmentBuilder getBuiler(int parentsID, UniFragment uniFragment){
        FragmentBuilder builder = FragmentBuilder.getBuilder(activity, parentsID, uniFragment);
        uniFragment.setUniBoot(this);
        return builder;
    }


    /********************** layout 확장 *********************/
    protected void setLayout(ViewGroup parentsView, int layoutID){
        parentsView.removeAllViews();
        ((LayoutInflater)parentsView.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(layoutID, rootView);
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


}
