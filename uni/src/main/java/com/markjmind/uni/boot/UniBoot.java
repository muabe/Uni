package com.markjmind.uni.boot;


import android.app.Activity;
import android.widget.FrameLayout;
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
        public FrameLayout bottom;
        public FrameLayout left;
        public FrameLayout right;
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
        view.left = (FrameLayout)activity.findViewById(id.left);
        view.right = (FrameLayout)activity.findViewById(id.right);
        onAttach(activity);

        return this;
    }

    protected void onAttach(Activity activity){

    }

    public RelativeLayout getRootView(){
        return this.rootView;
    }

    public FragmentBuilder getBuiler(int parentsID, UniFragment uniFragment){
        FragmentBuilder builder = FragmentBuilder.getBuilder(activity, parentsID, uniFragment);
        uniFragment.setUniBoot(this);
        return builder;
    }

    public FragmentBuilder getBuiler(int parentsID, Class<? extends UniFragment> uniFragmentClass){
        FragmentBuilder builder = FragmentBuilder.getBuilder(activity, parentsID, uniFragmentClass);
        builder.getFragment().setUniBoot(this);
        return builder;
    }
}
