package com.markjmind.uni;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.TaskObserver;
import com.markjmind.uni.thread.UniMainAsyncTask;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-16
 */
public class UniProgress implements TaskObserver {
    private enum Mode{
        none, view, dialog
    }

    private ViewGroup parents;
    private boolean isEnable;
    private View layout;
    private int layoutId;
    private Mode mode;
    private LinearLayout progressLayout;
    private ProgressInterface progress;
    private OnProgressListener listener;
    private int theme;

    protected UniProgress(){
        this.mode = Mode.none;
        this.isEnable = true;
        this.layoutId = -1;
        this.theme = -1;
    }

    protected UniProgress(ViewGroup parents) {
        this();
        init(parents);
    }

    protected void init(ViewGroup uniView){
        this.parents = uniView;
        this.progressLayout = new LinearLayout(uniView.getContext());
        this.progressLayout.setGravity(Gravity.CENTER);
        this.progressLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.progressLayout.setClickable(true);
    }

    @Override
    public void onPreExecute(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) {
        show(cancelAdapter);
    }

    @Override
    public void doInBackground(UniMainAsyncTask uniTask, CancelAdapter cancelAdapter) throws Exception {

    }

    @Override
    public void onProgressUpdate(UniMainAsyncTask uniTask, Object value, CancelAdapter cancelAdapter) {
        listener.onUpdate(getLayout(), value, cancelAdapter);
    }

    @Override
    public void onPostExecute(UniMainAsyncTask uniTask) {
        dismiss();
    }

    @Override
    public void onFailExecute(UniMainAsyncTask uniTask, boolean isException, String message, Exception e) {
        dismiss();
    }

    @Override
    public void onCancelled(UniMainAsyncTask uniTask, boolean attach) {
        dismiss(attach);
    }


    public synchronized void show(CancelAdapter cancelAdapter){
        if(mode != Mode.none && isEnable) {
            if (mode == Mode.dialog) {
                if (progress == null) {
                    if(theme == -1){
                        progress = new ProgressAlter(parents.getContext(), progressLayout);
                    }else{
                        progress = new ProgressAlter(parents.getContext(), progressLayout, theme);
                    }

                }

            }else if(mode == Mode.view){
                if (progress == null) {
                    progress = new ProgressView(progressLayout);
                }
            }

            if(layoutId!=-1) {
                LayoutInflater inflater = ((LayoutInflater) parents.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                layout = inflater.inflate(layoutId, progressLayout, false);
            }

            if(listener!=null){
                listener.onStart(layout, cancelAdapter);
            }
            progress.show(layout);
        }
    }

    public UniProgress setOnProgressListener(OnProgressListener listener){
        this.listener = listener;
        return this;
    }

    public OnProgressListener getOnProgressListener(){
        return listener;
    }

    public void dismiss(){
       this.dismiss(true);
    }

    public synchronized void dismiss(boolean attach){
        if(mode!= Mode.none && isEnable && progress!=null && progress.isShow()){
            if(listener!=null){
                listener.onDestroy(layout, attach);
            }
            progress.dismiss();
        }

    }

    public boolean isEnable(){
        return isEnable;
    }

    public UniProgress setEnable(boolean enable){
        this.isEnable = enable;
        return this;
    }

    public UniProgress setLayout(int layoutId){
        this.layoutId = layoutId;
        return this;
    }

    public View getLayout(){
        return layout;
    }

    public UniProgress bindDialog(int layoutId){
        setLayout(layoutId);
        if(mode == Mode.view && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode = Mode.dialog;
        return this;
    }

    public UniProgress bind(int layoutId){
        setLayout(layoutId);
        if(mode == Mode.dialog && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode =Mode.view;
        return this;
    }

    protected boolean isAble(){
        if(isEnable && this.mode != Mode.none){
            return true;
        }else{
            return false;
        }
    }


    public interface OnProgressListener{
        void onStart(View layout, CancelAdapter cancelAdapter);
        void onUpdate(View layout, Object value, CancelAdapter cancelAdapter);
        void onDestroy(View layout, boolean attach);
    }

    interface ProgressInterface{
        boolean isShow();
        void show(View view);
        void dismiss();
    }

    private class ProgressAlter extends AlertDialog implements ProgressInterface{
        ViewGroup progressLayout;

        public ProgressAlter(Context context, ViewGroup layout) {
            super(context);
            this.progressLayout = layout;
        }

        public ProgressAlter(Context context, ViewGroup layout, int theme) {
            super(context, theme);
            this.progressLayout = layout;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(progressLayout);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            this.setCancelable(false);
        }

        @Override
        public boolean isShow() {
            return super.isShowing();
        }

        public synchronized void show(View view) {
            if(view!=null) {
                progressLayout.addView(view);
            }
            super.show();
        }

        @Override
        public synchronized void dismiss() {
            if(progressLayout !=null) {
                progressLayout.removeAllViews();
            }
            super.dismiss();
        }

    }


    private class ProgressView implements ProgressInterface{
        ViewGroup progressLayout;
        boolean isShowing;

        public ProgressView(ViewGroup layout){
            this.progressLayout = layout;
        }

        @Override
        public boolean isShow() {
            return isShowing;
        }

        @Override
        public synchronized void show(View view) {
            if(!isShowing) {
                isShowing = true;
                if(view!=null) {
                    progressLayout.addView(view);
                }
                parents.addView(progressLayout, parents.getChildCount() - 1);
            }
        }

        @Override
        public synchronized void dismiss() {
            if(isShowing) {
                if(progressLayout !=null) {
                    progressLayout.removeAllViews();
                    parents.removeView(progressLayout);
                }
                isShowing = false;
            }
        }
    }
}
