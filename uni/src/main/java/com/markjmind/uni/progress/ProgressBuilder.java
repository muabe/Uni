package com.markjmind.uni.progress;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.ThreadProcessObserver;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-16
 */
public class ProgressBuilder extends ThreadProcessObserver {
    public Store<?> param;

    private ViewGroup parents;
    private boolean isEnable;
    private View layout;
    private LinearLayout progressLayout;
    private ProgressInterface progressInterface;
    private OnProgressListener listener;
    private int theme;
    private boolean isShowing = false;
    private boolean autoCancel = true;
    private UniProgress uniProgress;

    public ProgressBuilder(){
        this.isEnable = true;
        this.theme = -1;
        this.param = new Store<>();
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
        if(autoCancel) {
            dismiss(attach);
        }
    }

    public void setAutoCancel(boolean autoCancel){
        this.autoCancel = autoCancel;
    }

    public synchronized void show(CancelAdapter cancelAdapter){
        if(isEnable) {
            if(uniProgress != null) {
                isShowing = true;
                if (uniProgress.getMode() == UniProgress.DIALOG ) {
                    if (progressInterface == null || uniProgress.getMode() != progressInterface.getMode()) {
                        if(theme == -1){
                            progressInterface = new ProgressAlter(parents.getContext(), progressLayout);
                        }else{
                            progressInterface = new ProgressAlter(parents.getContext(), progressLayout, theme);
                        }

                    }

                }else if(uniProgress.getMode() == UniProgress.VIEW){
                    if (progressInterface == null || uniProgress.getMode() != progressInterface.getMode()) {
                        progressInterface = new ProgressView(progressLayout, parents);
                    }else{
                        ((ProgressView) progressInterface).reset(progressLayout, parents);
                    }
                }

                layout = uniProgress.mapperInit(progressLayout, param);
                if(listener != null){
                    listener.onStart(layout, cancelAdapter);
                }
                progressInterface.show(layout);
            }
        }
    }

    public boolean isShowing(){
        return isShowing;
    }
    public synchronized void dismiss(){
        this.dismiss(true);
        isShowing = false;
    }

    public synchronized void dismiss(boolean attach){
        if(uniProgress != null && progressInterface !=null && progressInterface.isShowing()){
            if(listener !=null){
                listener.onDestroy(layout, attach);
            }
            if(uniProgress != null) {
                uniProgress.param.clear();
            }
            progressInterface.dismiss();
        }
        isShowing = false;

    }

    public boolean isEnable(){
        return isEnable;
    }

    public ProgressBuilder setEnable(boolean enable){
        this.isEnable = enable;
        return this;
    }

    public UniProgress set(int progressMode, UniProgress uniProgress){
        this.uniProgress = uniProgress;
        this.uniProgress.hasListener = true;
        this.listener = uniProgress;
        if(progressInterface !=null && progressInterface.isShowing()){
            progressInterface.dismiss();
        }
        uniProgress.setMode(progressMode);
        return this.uniProgress;
    }

    public UniProgress set(int progressMode, int layoutId){
        if(uniProgress!=null && progressMode == UniProgress.DIALOG) {
            if (uniProgress.getMode() == UniProgress.VIEW && progressInterface != null && progressInterface.isShowing()) {
                progressInterface.dismiss();
            }
        }else if(uniProgress!=null && progressMode == UniProgress.VIEW){
            if(uniProgress.getMode() == UniProgress.DIALOG && progressInterface !=null && progressInterface.isShowing()){
                progressInterface.dismiss();
            }
        }
        this.uniProgress = new UniProgress(layoutId);
        this.uniProgress.hasListener = false;
        this.listener = null;
        uniProgress.setMode(progressMode);
        return this.uniProgress;
    }


    public UniProgress get(){
        return this.uniProgress;
    }

    public boolean isAble(){
        if(isEnable && this.uniProgress != null){
            return true;
        }else{
            return false;
        }
    }


    public interface ProgressInterface{
        boolean isShowing();
        void show(View view);
        void dismiss();
        int getMode();
    }

}
