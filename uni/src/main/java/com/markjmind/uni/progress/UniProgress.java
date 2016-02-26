package com.markjmind.uni.progress;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.ProcessObserver;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-16
 */
public class UniProgress implements ProcessObserver {
    public enum Mode{
        none, view, dialog
    }

    private ViewGroup parents;
    private boolean isEnable;
    private View layout;
    private Mode mode;
    private LinearLayout progressLayout;
    private ProgressInterface progress;
    private OnProgressListener listener;
    private int theme;

    private ProgressInfo progressInfo;

    public UniProgress(){
        this.mode = Mode.none;
        this.isEnable = true;
        this.theme = -1;
    }

    public UniProgress(ViewGroup parents) {
        this();
        setParents(parents);
    }

    public void setParents(ViewGroup parents){
        this.parents = parents;
        this.progressLayout = new LinearLayout(parents.getContext());
        this.progressLayout.setGravity(Gravity.CENTER);
        this.progressLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.progressLayout.setClickable(true);
    }

    @Override
    public void onPreExecute(CancelAdapter cancelAdapter) {
        show(cancelAdapter);
    }

    @Override
    public void doInBackground(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {

    }

    @Override
    public void onProgressUpdate(Object value, CancelAdapter cancelAdapter) {
        if(listener!=null) {
            listener.onUpdate(layout, value, cancelAdapter);
        }
    }

    @Override
    public void onPostExecute() {
        dismiss();
    }

    @Override
    public void onFailedExecute(String message, Object arg) {
        dismiss();
    }

    @Override
    public void onExceptionExecute(Exception e) {
        dismiss();
    }

    @Override
    public void onCancelled(boolean attach) {
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
                    progress = new ProgressView(progressLayout, parents);
                }else{
                    ((ProgressView)progress).reset(progressLayout, parents);
                }
            }

            if(progressInfo != null) {
                layout = progressInfo.mapperInit(progressLayout);
            }

            if(listener != null){
                listener.onStart(layout, cancelAdapter);
            }
            progress.show(layout);
        }
    }

    public void dismiss(){
       this.dismiss(true);
    }

    public synchronized void dismiss(boolean attach){
        if(mode!= Mode.none && progress!=null && progress.isShow()){
            if(listener !=null){
                listener.onDestroy(layout, attach);
            }
            if(progressInfo != null) {
                progressInfo.param.clear();
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

    public UniProgressDialog dialogInfo(UniProgressDialog progressInfo){
        this.progressInfo = progressInfo;
        this.listener = progressInfo.getListener();
        if(mode == Mode.view && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode = progressInfo.getMode();
        return (UniProgressDialog)this.progressInfo;
    }

    public void dialogInfo(int layoutId){
        this.progressInfo = new ProgressInfo(layoutId) {
            @Override
            public Mode getMode() {
                return Mode.dialog;
            }
        };
        this.listener = null;
        if(mode == Mode.view && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode = progressInfo.getMode();
    }

    public UniProgressView viewInfo(UniProgressView progressInfo){
        this.progressInfo = progressInfo;
        this.listener = progressInfo.getListener();
        if(mode == Mode.dialog && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode = progressInfo.getMode();
        return (UniProgressView)this.progressInfo;
    }

    public void viewInfo(int layoutId){
        this.progressInfo = new ProgressInfo(layoutId) {
            @Override
            public Mode getMode() {
                return Mode.view;
            }
        };
        this.listener = null;
        if(mode == Mode.dialog && progress!=null && progress.isShow()){
            progress.dismiss();
        }
        this.mode = progressInfo.getMode();
    }

    public ProgressInfo getInfo(){
        return this.progressInfo;
    }

    public boolean isAble(){
        if(isEnable && this.mode != Mode.none){
            return true;
        }else{
            return false;
        }
    }


    public interface ProgressInterface{
        boolean isShow();
        void show(View view);
        void dismiss();
    }

}
