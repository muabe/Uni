package com.markjmind.uni.viewer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UniLoadFailException;
import com.markjmind.uni.hub.Store;

import java.util.HashMap;


/**
 * start : 2013.11.17<br>
 * <br>
 * Base Viewer 클래스<br>
 * 일반 Viewer을 정의하는 클래스는<br>
 * BaseLayout 를 상속하여 view_init메소드에 화면을 정의한다.
 *
 * @author 오재웅
 * @version 2013.11.17
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 */

public class Viewer {

    public static final int REQUEST_CODE_NONE = -1;
    protected static Store<InnerAsyncTask> asyncTaskPool = new Store<>();
    private static int taskIndex = 0;
    FrameLayout frame;
    View viewer;
    protected ViewGroup parentView;
    private int id;
    private int layoutId;
    Store<?> param;
    HashMap<Integer, OnClickListenerReceiver> onClickParams = new HashMap<>();
    ViewerBuilder builder;
    private RefreshBuilder reBuilder;

    private boolean enablePostView;
    private UniAsyncTask uniAsyncTask;


    private Object result;

    boolean isPrepare = false;

    /**
     * onPost시 view를 enable할지 여부
     *
     * @return
     */
    public boolean isEnablePostView() {
        return enablePostView;
    }

    /**
     * onPost시 view를 enable할지 여부 결정
     *
     * @return
     */
    public void setEnablePostView(boolean enablePostView) {
        this.enablePostView = enablePostView;
    }


    /**
     * 기본생성자
     */
    protected Viewer() {
        param = new Store<>();
        this.enablePostView = true;
        uniAsyncTask = new UniAsyncTask() {
            @Override
            public void onBind(int requestCode, ViewerBuilder build, Viewer viewer) {
                viewer.onBind(requestCode, build);
            }

            @Override
            public void onPre(int requestCode, Viewer viewer) {
                viewer.onPre(requestCode);
            }

            @Override
            public void onLoad(int requestCode, UpdateEvent event, Viewer viewer) throws Exception {
                viewer.onLoad(requestCode, event);
            }

            @Override
            public void onUpdate(int requestCode, Object value, Viewer viewer) {
                viewer.onUpdate(requestCode, value);
            }

            @Override
            public void onPost(int requestCode, Viewer viewer) {
                viewer.onPost(builder.requestCode);
            }

            @Override
            public void onFail(int requestCode, boolean isException, String message, Exception e, Viewer viewer) {
                viewer.onFail(builder.requestCode, isException, message, e);
            }

            @Override
            public void onCancelled(int requestCode, Viewer viewer) {
                viewer.onCancelled(builder.requestCode);
            }
        };
    }

    /*****************************************
     * 외부 인터페이스
     ********************************************/

    public void onBind(int requestCode, ViewerBuilder build) {
    }

    public void onPre(int requestCode) {
    }

    public void onLoad(int requestCode, UpdateEvent event) throws Exception {
    }

    public void onUpdate(int requestCode, Object value) {
    }

    public void onPost(int requestCode) {
    }

    public void onFail(int requestCode, boolean isException, String message, Exception e) {
    }

    public void onCancelled(int requestCode) {
    }

    void runLoad() {
        excute(TASK_LOAD, getTaskKey(TASK_LOAD, this, uniAsyncTask));
    }

    void runPost() {
//		Viewer.cancelTask(getTaskKey(TASK_CHANGE));
        Viewer.cancelTaskAll(this);
        uniAsyncTask.post(builder.requestCode, this);
    }


/***************************************** 초기화 관련 ********************************************/

    /**
     * Viewer를 초기화 한
     *
     * @param layout_id Viewer의 Layout ID
     */
    protected void init(int layout_id) {
        setLayoutId(layout_id);
        frame = new FrameLayout(builder.context);
        viewer = (ViewGroup) getLayoutInfalter(layoutId);
        onClickParams.clear();
        UniMemberMapper.injection(this);
    }

    /**
     * Viewer가 속한 Activity를 가져온다.
     *
     * @return Activity
     */
    public Activity getActivity() {
        return builder.activity;
    }

    /**
     * Viewer가 속한 Dialog를 가져온다.
     *
     * @return Dialog
     */
    public Dialog getDialog() {
        return builder.dialog;
    }

/*************************************************** bindLayout 관련 *********************************************/

        void removeAfterOutAnim() {
        if (outAnimation != null) {
            final View[] childArr = new View[parentView.getChildCount()];
            for (int i = 0; i < childArr.length; i++) {
                childArr[i] = parentView.getChildAt(i);
//				outAnimation.setAnimationListener(new RemoveAnimation(childArr[i]));
                childArr[i].startAnimation(outAnimation);
                parentView.removeView(childArr[i]);
            }
        } else {
            if (parentView != null) {
                parentView.removeAllViews();
            }
        }
    }

    void removeViewer() {
        frame.removeAllViews();
        viewer = null;
    }

    public Viewer prepare() {
        isPrepare = true;

        return this;
    }

    /**
     * index에 해당하는 부모 ViewGroup아래 JwViewer를 추가한다.
     *
     * @param parents 부모 ViewGroup
     * @param index   index
     * @return
     */
    public Viewer add(ViewGroup parents, int index) {
        setParentView(parents);
        isPrepare = false;
        uniAsyncTask.bind(builder.requestCode, builder, this);
        init(builder.layout_id);
        if (builder.isAsync()) {
            excute(TASK_ADD, getTaskKey(TASK_ADD, this, uniAsyncTask));
        } else {
            Viewer.cancelTaskAll(this);
            frame.addView(viewer);
            if (index == -1) {
                parentView.addView(frame);
            } else {
                parentView.addView(frame, index);
            }
            uniAsyncTask.post(builder.requestCode, this);
        }
        return this;
    }

    public Viewer create(){
        isPrepare = true;

        Viewer.cancelTaskAll(this);
        uniAsyncTask.bind(builder.requestCode, builder, this);

        setLayoutId(builder.layout_id);
        frame = new FrameLayout(builder.context);
        viewer = (ViewGroup) getLayoutInfalter(layoutId);
        onClickParams.clear();
        frame.addView(viewer);
        UniMemberMapper.injection(this);
        return this;
    }

    public Viewer excute(){
        if (builder.isAsync()) { // Ansync 일때
            if(isPrepare){
                setParentView((ViewGroup)frame.getParent());
            }
            runLoad();
        } else {
            runPost();
        }
        return this;
    }

    /**
     * 부모 ViewGroup 아래의 View들을 모두 지우고<br>
     * 현재 Viewer로 변경한다.
     *
     * @param parents 부모 ViewGroup
     * @return
     */
    public Viewer change(ViewGroup parents) {
        setParentView(parents);
        isPrepare = false;
        Viewer.cancelTaskAll(this);
        uniAsyncTask.bind(builder.requestCode, builder, this);
        init(builder.layout_id);
        if (builder.isAsync()) { // Ansync 일때
            excute(TASK_CHANGE, getTaskKey(TASK_CHANGE, this, uniAsyncTask));
        } else {
            removeAfterOutAnim();
            frame.addView(viewer);
            parentView.addView(frame, parentView.getLayoutParams());
            uniAsyncTask.post(builder.requestCode, this);
        }
        return this;
    }



    public Viewer change(int parents_id) {
        return this.change((ViewGroup) findGobalView(parents_id));
    }

    private void setParentView(ViewGroup parents) {
        parentView = parents;
        this.setId(parentView.hashCode());
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public <Result> Result getResult() {
        return (Result) result;
    }

/************************************************* 파라미터 관련 ************************************/
    /**
     * 다른 Viewer로 넘기는 파라미터를 설정한다.
     *
     * @param key   Parameter Key
     * @param value Parameter Value
     */
    public Viewer addParam(String key, Object value) {
        param.add(key, value);
        return this;
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Value
     */
    public Object getParam(String key) {
        return param.get(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Value
     */
    public Object optParam(String key, Object defalut) {
        Object result = getParam(key);
        if (result == null)
            result = defalut;
        return result;
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter String Value
     */
    public String getParamString(String key) {
        return (String) getParam(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter String Value
     */
    public String optParamString(String key, String defalut) {
        return (String) optParam(key, defalut);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Int Value
     */
    public int getParamInt(String key) {
        return (Integer) getParam(key);
    }

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Int Value
     */
    public int optParamInt(String key, int defalut) {
        return (Integer) optParam(key, defalut);
    }

    /**
     * 다른 Viewer로 전달한 파라미터 Store를 받는다.
     *
     * @return Parameter store
     */
    public Store<?> getParamStore() {
        return param;
    }

    /**
     * 다른 Viewer로 전달한 파라미터 Store를 설정한다.
     */
    public void setParamStore(Store<Object> store) {
        param = store;
    }


    public static String[] getBox(Class<Viewer> viewerClass) {
        return UniMemberMapper.injectionBox(viewerClass);
    }

    /***************************************************
     * onClick 관련
     *********************************************/
    public void setOnClickParam(View view, Object... clickParam) {
        if (onClickParams.containsKey(view.hashCode())) {
            OnClickListenerReceiver rec = onClickParams.get(view.hashCode());
            rec.setParam(clickParam);
        }
    }

    /***************************************************
     * Task 관련
     *********************************************/
    public final static String TASK_CHANGE = "TASK_CHANGE"; //어싱크 뷰 체인지 뷰어
    public final static String TASK_ADD = "TASK_ADD"; //어싱크 뷰 인풋 뷰어
    public final static String TASK_LOAD = "TASK_LOAD"; //어싱크 로딩만
    public final static String TASK_EXCUTE = "TASK_EXCUTE"; //전체를 로딩하지않고 스레드만사용
    String state;


    private void excute(String state, String taskKey) {
        this.state = state;
        if (TASK_ADD.equals(state)) {
            taskIndex++; // viewer.add 일경우 task를 cancel하지 않는다.
        } else {
            Viewer.cancelTaskAdd();
            Viewer.cancelTaskAll(this);
        }
        InnerAsyncTask innerAsyncTask = new InnerAsyncTask(taskKey, state, this, builder, uniAsyncTask);
        asyncTaskPool.add(taskKey, innerAsyncTask);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            innerAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            innerAsyncTask.execute();
        }
    }

    protected String excute(int requestCode, UniAsyncTask vListener) {
        builder.setRequestCode(requestCode);
        vListener.onBind(requestCode, builder, this);
        String taskKey = getTaskKey(TASK_EXCUTE, this, vListener);
        Viewer.cancelTask(taskKey);
        InnerAsyncTask innerAsyncTask = new InnerAsyncTask(taskKey, TASK_EXCUTE, this, builder, vListener);
        asyncTaskPool.add(taskKey, innerAsyncTask);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            innerAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            innerAsyncTask.execute();
        }

        return taskKey;
    }

    protected void fail(String message) throws UniLoadFailException {
        throw new UniLoadFailException(message);
    }

    protected String excute(UniAsyncTask vListener) {
        return excute(builder.requestCode, vListener);
    }

    private static String getTaskKey(String taskName, Viewer vwr, UniAsyncTask uniAsyncTask) {
        String taskKey = taskName + "_" + vwr.getId() + "_" + uniAsyncTask.hashCode();
        if (TASK_ADD.equals(taskName)) {
            taskKey += taskIndex;
        }
        return taskKey;
    }

    static void cancelTaskAll(Viewer vwr) {
        String[] keys = getRunTaskKeys();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains(TASK_CHANGE + "_" + vwr.getId())) {
                Viewer.cancelTask(keys[i]);
                continue;
            }
            if (keys[i].contains(TASK_EXCUTE + "_" + vwr.getId())) {
                Viewer.cancelTask(keys[i]);
            }

        }
    }


    private static void cancelTask(String taskKey) {
        InnerAsyncTask innerAsyncTask = asyncTaskPool.get(taskKey);
        if (innerAsyncTask != null) {
            innerAsyncTask.stop();
        }
    }

    /**
     * viewer.add 일경우 cancel task
     * add의 경우 tackKey 마지막에 index가 붙는데
     * for문을 돌면서 해당 add의 이름을 가진 task를 전부 해제한다.
     */
    private static void cancelTaskAdd() {
        String[] keys = getRunTaskKeys();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].indexOf(TASK_ADD) >= 0) {
                Viewer.cancelTask(keys[i]);
            }
        }
    }


    public static int getRunTaskCount() {
        return asyncTaskPool.size();
    }

    public static String[] getRunTaskKeys() {
        return asyncTaskPool.getKeys();
    }

    public void cancel(String taskKey){
        Viewer.cancelTask(taskKey);
    }
    public void cancelAll(){
        Viewer.cancelTaskAll(this);
    }

    public void setCancelListener(CancelListener cancelListener){
        uniAsyncTask.setCancelListener(cancelListener);
    }

    /***************************************************
     * view control
     *********************************************/

    private View getLayoutInfalter(int layout_id) {
        try {
            View v = ((LayoutInflater) builder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
            return v;
        }catch(Resources.NotFoundException e){
            throw new Resources.NotFoundException(ErrorMessage.Runtime.inflater(this.getClass()));
        }
    }


/*************************************************** 외부 지원함수 *********************************************/
    /**
     * Viewer ID를 가져온다.
     *
     * @return Viewer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Viewer ID를 설정한다.
     *
     * @param id Viewer ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Layout ID를 가져온다.
     *
     * @return Layout ID
     */
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * Layout ID를 가져온다.
     *
     * @param layoutId Layout ID
     */
    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    /**
     * Context 객체를 반환한다.
     *
     * @return Context
     */
    public Context getContext() {
        return builder.context;
    }

    /**
     * viewer의 Context 객체를 반환한다.
     *
     * @return Context
     */
    public Context getViewerContext() {
        return viewer.getContext();
    }

    /**
     * Resources를 반환한다.
     *
     * @return Resources
     */
    public Resources getResources() {
        return builder.context.getResources();
    }

    /**
     * 부모의 view를  반환한다.
     *
     * @return 부모 View
     */
    public ViewGroup getParent() {
        return this.parentView;
    }

    /**
     * Viewer에해당하는 Layout
     *
     * @return Viewer에해당하는 Layout
     */
    public ViewGroup getLayout() {
        return (ViewGroup) viewer;
    }

    /**
     * Viewer에해당하는 View를 가져온다.
     *
     * @return Viewer에해당하는 View
     */
    public View getView() {
        return frame;
    }

    /**
     * activity안에 있는 View를 찾는다
     *
     * @param R_id_view 찾을 View ID
     * @return View 해당 View
     */
    public View findGobalView(int R_id_view) {
        return builder.findGobalView(R_id_view);
    }

    /**
     * 현재 viewer에 속해있는 View를 찾는다
     *
     * @param R_id_view 찾을 View ID
     * @return View 해당 View
     */
    public View findViewById(int R_id_view) {
        return Jwc.findViewById(R_id_view, viewer);
    }

    /**
     * parents에 속해있는 View를 찾는다
     *
     * @param R_id_view 찾을 View ID
     * @return View  해당 View
     */
    public View findViewById(int R_id_view, View parents) {
        return Jwc.findViewById(R_id_view, parents);
    }

    /**
     * 현재 viewer에 속해있는 View를 tag로 찾는다
     *
     * @param tag 찾을 tag
     * @return View 해당 View
     */
    public View findViewWithTag(String tag) {
        return findViewWithTag(tag, viewer);
    }

    /**
     * parnets에 속해있는 View를 tag로 찾는다
     *
     * @param tag     찾을 tag
     * @param parents parnets View
     * @return View 해당 View
     */
    public View findViewWithTag(String tag, View parents) {
        return Jwc.findViewWithTag(tag, parents);
    }

    /**
     * Viewer를 visible한다.
     */
    public void visible() {
        viewer.setVisibility(View.VISIBLE);
    }

    /**
     * Viewer layout을 삭제한다.
     */
    public void removeLayout() {
        if (parentView != null) {
            parentView.removeView(this.getLayout());
        }
    }

    /***************************************************
     * 애니메이션 관련
     *********************************************/

    Animation outAnimation;

    public Viewer setOutAnim(Animation outAnimation) {
        this.outAnimation = outAnimation;
        return this;
    }

    Animation inAnimation;

    public Viewer setInAnim(Animation inAnimation) {
        this.inAnimation = inAnimation;
        return this;
    }

    public Viewer setAnimation(Animation animation) {
        this.inAnimation = animation;
        return this;
    }

    public Viewer setAnimation(int R_anim_id) {
        return setAnimation(AnimationUtils.loadAnimation(getContext(), R_anim_id));
    }

    public Viewer setAnimationOut(Animation animation) {
        this.outAnimation = animation;
        return this;
    }

    public Viewer setAnimationOut(int R_anim_id) {
        return setAnimationOut(AnimationUtils.loadAnimation(getContext(), R_anim_id));
    }

    boolean outEndAnimate = false;

    public Viewer setAnimation(Animation inAnimation, Animation outAnimation, boolean outEndAnimate) {
        this.inAnimation = inAnimation;
        this.outAnimation = outAnimation;
        this.outEndAnimate = outEndAnimate;
        return this;
    }

    public Viewer setAnimation(int R_anim_inId, int R_anim_outId, boolean outEndAnimate) {
        return setAnimation(AnimationUtils.loadAnimation(getContext(), R_anim_inId), AnimationUtils.loadAnimation(getContext(), R_anim_outId), outEndAnimate);
    }


    /***************************************************
     * 빌드 관련
     *********************************************/

    public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Activity activity) {
        ViewerBuilder builder = new ViewerBuilder(jwViewerClass, activity);
        return builder;
    }

    public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Dialog dialog) {
        ViewerBuilder builder = new ViewerBuilder(jwViewerClass, dialog);
        return builder;
    }

    public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Viewer viewer) {
        ViewerBuilder builder;
        if (viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.ACTIVITY)) {
            return new ViewerBuilder(jwViewerClass, viewer.getActivity());
        } else if (viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.DIALOG)) {
            return new ViewerBuilder(jwViewerClass, viewer.getDialog());
        } else {
            return null;
        }
    }

    public RefreshBuilder reBuild(int requestCode) {
        if (reBuilder == null) {
            reBuilder = new RefreshBuilder(this);
        }
        reBuilder.setRequestCode(requestCode);
        return reBuilder;
    }

    public RefreshBuilder reBuild() {
        return this.reBuild(REQUEST_CODE_NONE);
    }

    public int getRequestCode() {
        return builder.requestCode;
    }


/*************************************************** 캐쉬 관련 *********************************************/

//	public Viewer clearCache(){
//	JwViewerCache.clear(layoutId, parentView);
//	return this;
//}
//
//	public static void clearAllCache(){
//		JwViewerCache.clear();
//	}
//
//	public static Viewer getViewerCache(int layoutId, ViewGroup parents){
//		Viewer jv = JwViewerCache.get(layoutId, parents);
//		return jv;
//	}
//	public static Viewer getViewerCache(int layoutId, int parents_id,Activity activity){
//		ViewGroup parents = (ViewGroup)Jwc.getView(parents_id, activity);
//		return getViewerCache(layoutId, parents);
//	}
//	public static Viewer getViewerCache(int layoutId, int parents_id,Dialog dialog){
//		ViewGroup parents = (ViewGroup)Jwc.getView(parents_id, dialog);
//		return getViewerCache(layoutId, parents);
//	}

/*************************************************** 기타 내부함수 관련 *********************************************/
}
