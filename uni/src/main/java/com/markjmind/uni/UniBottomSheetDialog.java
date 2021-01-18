package com.markjmind.uni;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.common.SimpleLog;
import com.markjmind.uni.common.Store;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.aop.AopListener;

import java.util.ArrayList;

public class UniBottomSheetDialog extends BottomSheetDialog implements UniInterface {
    private UniTask uniTask;
    private UniLayout uniLayout;
    public Store<?> param;
    public ProgressBuilder progressBuilder;
    private ArrayList<AopListener> aopListeners = new ArrayList<>();
    private OnDismissResult onDismissResult;
    protected SimpleLog log;

    public UniBottomSheetDialog(Context context) {
        super(context);
        init();
    }

    public UniBottomSheetDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        uniLayout = null;
        uniTask = new UniTask(true);
        uniTask.initAtrribute(this, this);
        log = new SimpleLog(getClass());
    }

    void setUniTask(UniTask uniTask) {
        this.uniTask = uniTask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniLayout = new UniLayout(getContext());
        setContentView(uniLayout);
        uniTask.syncUniLayout(uniLayout);
        uniTask.setBindInfo(this, uniLayout, null, null);
        super.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getTask().execute();
            }
        });
    }

    @Override
    public void setOnShowListener(OnShowListener listener) {
        final OnShowListener lisner = listener;
        super.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getTask().execute();
                lisner.onShow(dialog);
            }
        });
    }

    public void setAsync(boolean isAsync) {
        this.uniTask.setAsync(isAsync);
    }

    public boolean isAsync() {
        return this.uniTask.isAsync();
    }


    /***************************************************
     * 간편기능 함수
     *********************************************/
    public UniBottomSheetDialog fullSize(){
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            getContext().setTheme(themeResId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public <T extends Application>T app(Class<T> appClass){
        return appClass.cast(getContext().getApplicationContext());
    }

    public String getString(int res){
        return getContext().getString(res);
    }

    public void toast(String msg){

    }

    /***************************************************
     * 필수 항목
     *********************************************/
    public UniLayout getUniLayout() {
        return uniLayout;
    }

    /*************************************************** BootStrap Builder관련 *********************************************/
    public FragmentBuilder getBuilder(FragmentActivity activity){
        return FragmentBuilder.getBuilder(activity);
    }

    public FragmentBuilder getBuilder(UniFragment uniFragment){
        return FragmentBuilder.getBuilder(uniFragment);
    }

    /***************************************************
     * 실행 관련
     *********************************************/
    public TaskController getTask() {
        return uniTask.getTask();
    }

    /***************************************************
     * 인터페이스 관련
     *********************************************/

    @Override
    public void onBind() {
    }

    @Override
    public void onPre() {
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onPost() {
    }

    @Override
    public void onPostFail(String message, Object arg) {
    }

    @Override
    public void onException(Exception e) {
    }

    @Override
    public void onCancelled(boolean attach) {
    }

    /***************************************************
     * 추가 리스너 관련
     *********************************************/

    public void setClickViewListener(int id, ClickViewListener onClickView) {
        final ClickViewListener finalTemp = onClickView;
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalTemp.onClick(UniBottomSheetDialog.this, v);
            }
        });
    }

    public void dismiss(Object arg) {
        if (onDismissResult != null) {
            onDismissResult.setArg(arg);
        }
        dismiss();
    }

    public void setDismissResultLstener(DismissResultLstener dismissResultLstener) {
        if (dismissResultLstener == null) {
            setOnDismissListener(null);
        } else {
            onDismissResult = new OnDismissResult(dismissResultLstener);
            setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDismissResult.onResult();
                }
            });
        }
    }

    public interface DismissResultLstener {
        void onDismiss(Object arg);
    }

    interface ClickViewListener {
        void onClick(UniBottomSheetDialog uniDialog, View view);
    }

    private class OnDismissResult {
        private Object arg;
        private DismissResultLstener dismissResultLstener;

        public OnDismissResult(DismissResultLstener dismissResultLstener) {
            this.dismissResultLstener = dismissResultLstener;
        }

        void setArg(Object arg) {
            this.arg = arg;
        }

        void onResult() {
            dismissResultLstener.onDismiss(arg);
        }
    }

}
