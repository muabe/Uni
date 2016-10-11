/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.aop.CancelAop;
import com.markjmind.uni.thread.aop.UniAop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-26
 */
public class UniDialog extends Dialog implements UniInterface {
    private UniTask uniTask;
    private UniLayout uniLayout;
    public Store<?> param;
    public ProgressBuilder progress;
    private UniAop aop;

    private OnDismissResult onDismissResult;

    public UniDialog(Context context) {
        super(context);
        uniLayout = null;
        new UniTask(true).bindDialog(this);
    }

    public UniDialog(Context context, int themeResId) {
        super(context, themeResId);
        uniLayout = null;
        new UniTask(true).bindDialog(this);
    }

    void setUniTask(UniTask uniTask) {
        this.uniTask = uniTask;
        uniTask.setUniInterface(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniLayout = new UniLayout(getContext());
        setContentView(uniLayout);
        uniTask.bind(this, uniLayout, null, null);
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
     * 필수 항목
     *********************************************/
    public UniLayout getUniLayout() {
        return uniLayout;
    }

    /***************************************************
     * 실행 관련
     *********************************************/
    public void setCancelAop(CancelAop cancelAop) {
        aop.setCancelAop(cancelAop);
    }

    public UniAop getAop() {
        return aop;
    }

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
                finalTemp.onClick(UniDialog.this, v);
            }
        });
    }

    public void dismiss(Object arg) {
        if (onDismissResult != null) {
            onDismissResult.setArg(arg);
        }
    }

    public void setDismissResultLstener(DismissResultLstener dismissResultLstener) {
        if (dismissResultLstener == null) {
            setOnDismissListener(null);
        } else {
            onDismissResult = new OnDismissResult(dismissResultLstener);
            setOnDismissListener(new Dialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onDismissResult.onResult();
                }
            });
        }
    }

    interface DismissResultLstener {
        public void onDismiss(Object arg);
    }

    interface ClickViewListener {
        public void onClick(UniDialog uniDialog, View view);
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
