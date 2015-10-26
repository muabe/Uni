package com.markjmind.uni;

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

import com.markjmind.uni.hub.Store;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * start : 2013.11.17<br>
 * <br>
 * Base Viewer 클래스<br>
 * 일반 Viewer을 정의하는 클래스는<br>
 * BaseLayout 를 상속하여 view_init메소드에 화면을 정의한다. 
 * 
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @version 2013.11.17
 */

public class Viewer {

	public static enum Status{
		None,
		Bind,
		Pre,
		Load,
		Cancel,
		Post,
		Complete,
		Fail,
		Error
	}
	public static final int REQUEST_CODE_NONE = -1;
	public Status currentStatus;
	static Store<ViewerAsyncTask> asyncTaskPool = new Store<ViewerAsyncTask>();
	private static int taskIndex = 0;
	FrameLayout frame;
	View viewer;
	protected ViewGroup parentView;
	private String id;
	private int layoutId;
	private Store<?> param;
	private HashMap<Integer, OnClickListenerReceiver> onClickParams = new HashMap<>();

	ViewerBuilder builder;

	private RefreshBuilder reBuilder;

	private boolean enablePostView;
	/**
	 * onPost시 view를 enable할지 여부
	 * @return
	 */
	public boolean isEnablePostView() {
		return enablePostView;
	}

	/**
	 * onPost시 view를 enable할지 여부 결정
	 * @return
	 */
	public void setEnablePostView(boolean enablePostView) {
		this.enablePostView = enablePostView;
	}



	/**
	 * 기본생성자
	 */
	protected Viewer(){
		param = new Store<>();
		this.enablePostView = true;
	}

/***************************************** 초기화 관련 ********************************************/

	/**
	 * Viewer를 초기화 한
	 * @param layout_id Viewer의 Layout ID
	 */
	protected void init(int layout_id){
		currentStatus = Status.None;
		setLayoutId(layout_id);
		frame = new FrameLayout(builder.context);
		makeViewer();
	}
	
	/**
	 * Viewer가 속한 Activity를 가져온다.
	 * @return Activity
	 */
	public Activity getActivity(){
		return builder.activity;
	}

	/**
	 * Viewer가 속한 Dialog를 가져온다.
	 * @return Dialog
	 */
	public Dialog getDialog(){
		return builder.dialog;
	}

/*************************************************** bind 관련 *********************************************/

	/**
	 * Viewer layout를 생성하고 초기화한다.<br>
	 * annotation에 관련된 사항을 injection한다.
	 */
	void makeViewer(){
		//뷰어를 새로만듬
		viewer = (ViewGroup)getLayoutInfalter(layoutId);
		onClickParams.clear();
		JwMemberMapper.injection(this);
	}

	void removeAfterOutAnim(){
		if(outAnimation!=null){
			final View[] childArr = new View[parentView.getChildCount()];
			for(int i=0;i<childArr.length;i++){
				childArr[i] = parentView.getChildAt(i);
//				outAnimation.setAnimationListener(new RemoveAnimation(childArr[i]));
				childArr[i].startAnimation(outAnimation);
				parentView.removeView(childArr[i]);
			}
		}else{
			if(parentView!=null){
				parentView.removeAllViews();
			}
		}
	}

	void removeViewer(){
		frame.removeAllViews();
		viewer = null;
	}

	//FIXME viewer의 바인드가 전에는 onPost에서만 이루어지는 줄알았음 지금은 prelayout에서도 사용
	//add일때 처리를 생각해봐야함
	/**
	 * async상황에서 실제 Frame에 Viewer layout을 추가한다.<br>
	 * change인지 add인지 확인하여
	 * @param jv
	 * @param task
	 * @return
	 */
	synchronized static boolean asyncBind(Viewer jv, String task){
		if(jv.getParent()==null){
			return false;
		}
		ViewGroup parentView = jv.getParent();
		int viewerIndex = -1;
		if(TASK_ACV.equals(task)){ // change일 경우
			if(!jv.builder.isPreLayout()){
				jv.removeViewer();
			}
		}else{ // add일 경우
			for(int i=0;i<parentView.getChildCount();i++){
				if(jv.frame == parentView.getChildAt(i)){
					viewerIndex = i;
					break;
				}
			}
			jv.removeViewer();
		}

		//애니메이션 때문에 이러는것 같음
		if(viewerIndex>=0){
//			jv.frame.addView(jv.viewer, viewerIndex, jv.parentView.getLayoutParams());
			jv.frame.addView(jv.viewer, jv.frame.getLayoutParams());
		}else{
			jv.frame.addView(jv.viewer, jv.frame.getLayoutParams());
		}
		return true;
	}



	/**
	 * index에 해당하는 부모 ViewGroup아래 JwViewer를 추가한다.
	 * @param parents 부모 ViewGroup
	 * @param index index
	 * @return
	 */
	public Viewer add(ViewGroup parents, int index){
		parentView = parents;
		inner_bind();
		if(builder.isAsync()){
			excute(TASK_APUT);
		}else{
			cancelTaskAcv();
			frame.addView(viewer);
			if(index==-1){
				parentView.addView(frame);
			}else{
				parentView.addView(frame, index);
			}
			inner_post();
		}
		return this;
	}

	/**
	 * 부모 ViewGroup 아래의 View들을 모두 지우고<br>
	 * 현재 Viewer로 변경한다.
	 * @param parents 부모 ViewGroup
	 * @return
	 */
	public Viewer change(ViewGroup parents){
		parentView = parents;
		inner_bind();
		cancelTaskAcv();
		if(builder.isAsync()){ // Ansync 일때
			excute(TASK_ACV);
		}else{
			removeAfterOutAnim();
			frame.addView(viewer);
			parentView.addView(frame, parentView.getLayoutParams());
			inner_post();
		}
		return this;
	}

	public void create(){
		makeViewer();
	}

	
/***************************************** 외부 인터페이스 ********************************************/

	public void onBind(int requestCode, ViewerBuilder build){}

	public void onPre(int requestCode){}

	/**
	 * loading() 리턴값이 true 일때 호출된다.<br>
	 * Viewer를 재정의해야할 함수<br>
	 * Viewer에 데이터값이나 셋팅하거나 view표현되어야할 값들 셋팅한다.
	 */
	public boolean onLoad(int requestCode, UpdateEvent event) throws Exception{ return true; }

	public void onUpdate(int requestCode, Object value){}

	public void onPost(int requestCode){}

	/**
	 * onloading() 리턴값이 false일때 호출된다.<br>
	 * 로딩이 실패했을 경우 표현해야할 화면을 정의한다.
	 */
	public void onFail(Integer requestCode, Exception e){}

	public void onCancelled(Integer requestCode){}


	//////////////// inner 작업 ///////////////////////////////////////////////////

	void inner_bind(){
		onBind(builder.requestCode, builder);
		if(builder.getParamStore().size()>0) {
			param.putAll(builder.getParamStore());
		}
		setStatus(Status.Bind);
		builder.getParamStore().clear();
	}

	void inner_pre(){
//		if(builder.hasLoadView && builder.loadView!=null) {
//			frame.addView(builder.loadView);
//			Log.e("sd","sd");
//		}
		setStatus(Status.Pre);
		onPre(builder.requestCode);
	}

	boolean inner_load(UpdateEvent event) throws Exception{
		setEnablePostView(true);
		return onLoad(builder.requestCode, event);
	}

	void inner_update(Object value){
		onUpdate(builder.requestCode, value);
		if(builder.loadController.loadViewListener !=null){
			builder.loadController.loadViewListener.loadUpdate(builder.requestCode, builder.loadController.loadView, value);
		}
	}

	boolean inner_post(){
		if(getParent()==null || frame==null){
			return false;
		}

		//액티비티나 다이얼로그가 종료되다면 cancel한다.
		if(builder.mode.equals(ViewerBuilder.TYPE_MODE.ACTIVITY)){
			if(getActivity().isFinishing()){
				cancelTaskAcv();
				builder.requestCode = REQUEST_CODE_NONE;
				return false;
			}
		}else if(builder.mode.equals(ViewerBuilder.TYPE_MODE.DIALOG)){
			if(!getDialog().isShowing()){
				cancelTaskAcv();
				builder.requestCode = REQUEST_CODE_NONE;
				return false;
			}
		}else{
			builder.requestCode = REQUEST_CODE_NONE;
			return false;
		}

		// 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
		// 로딩뷰는 한번뜨고 메모리에서 사라진다.(재사용시 데이터 초기화 문제)
		if(builder.loadController.isEnable()){
			builder.loadController.onDestroy(builder.requestCode, frame);
		}

		//hashCode가 구조상 유일 아이디가 될수있음을 확인했음
		int hashId = frame.hashCode();
		frame.setId(hashId);
		if(findGobalView(hashId)!=null){
			setStatus(Status.Post);
			onPost(builder.requestCode);
			setStatus(Status.Complete);
		}else{
			setStatus(Status.Error);
		}
		frame.setId(-1);
		builder.requestCode = REQUEST_CODE_NONE;
		return true;
	}

	void inner_cancelled(){
		if(getParent()!=null && frame!=null){
			// 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
			if(builder.loadController.isEnable()){
				builder.loadController.onDestroy(builder.requestCode, frame);
			}
		}
		setStatus(Status.Cancel);
		onCancelled(builder.requestCode);
		builder.requestCode = REQUEST_CODE_NONE;
	}

	/**
	 * 네트워크등 Thread 처리 관련 내용을 정의한다.
	 * @return
	 */
	void inner_view_fail(Exception e){
		if(getParent()!=null && frame!=null){
			// 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
			if(builder.loadController.isEnable()){
				builder.loadController.onDestroy(builder.requestCode, frame);
			}
		}
		setStatus(Status.Fail);
		onFail(builder.requestCode, e);
		setStatus(Status.Complete);
		builder.requestCode = REQUEST_CODE_NONE;
	}

	void runPre(){
		if(currentStatus!=Status.None) {
			cancelTaskAcv();
			removeViewer();
			makeViewer();
			frame.addView(viewer);
			inner_pre();
		}
		builder.requestCode = REQUEST_CODE_NONE;
	}

	void runLoad(){
		excute(TASK_LOAD);
	}

	void runPost(){
		cancelTaskAcv();
		inner_post();
	}


	private void setStatus(Status status){
		currentStatus = status;
	}

	
/************************************************* 파라미터 관련 ************************************/
	/**
	 * 다른 Viewer로 넘기는 파라미터를 설정한다.
	 * @param key Parameter Key
	 * @param value Parameter Value
	 */
	public Viewer addParam(String key, Object value){
		param.add(key, value);
		return this;
	}
	
	/**
	 * 다른 Viewer로 전달한 파라미터를 받는다.
	 * @param key Parameter Key
	 * @return Parameter Value
	 */
	public Object getParam(String key){
		return param.get(key);
	}
	
	/**
	 * 다른 Viewer로 전달한 파라미터를 받는다.<br>
	 * key에 대응하는 value가 없으면 defalut값을 리턴한다.
	 * @param key Parameter Key
	 * @param defalut value
	 * @return Parameter Value
	 */
	public Object optParam(String key, Object defalut){
		Object result = getParam(key);
		if(result==null)
			result = defalut;
		return result;
	}
	
	/**
	 * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.
	 * @param key Parameter Key
	 * @return Parameter String Value
	 */
	public String getParamString(String key){
		return (String) getParam(key);
	}
	
	/**
	 * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.<br>
	 * key에 대응하는 value가 없으면 defalut값을 리턴한다.
	 * @param key Parameter Key
	 * @param defalut value
	 * @return Parameter String Value
	 */
	public String optParamString(String key, String defalut){
		return (String) optParam(key, defalut);
	}

	/**
	 * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.
	 * @param key Parameter Key
	 * @return Parameter Int Value
	 */
	public int getParamInt(String key){
		return (Integer) getParam(key);
	}
	
	/**
	 * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.<br>
	 * key에 대응하는 value가 없으면 defalut값을 리턴한다.
	 * @param key Parameter Key
	 * @param defalut value
	 * @return Parameter Int Value
	 */
	public int optParamInt(String key, int defalut){
		return (Integer)optParam(key, defalut);
	}
	/**
	 * 다른 Viewer로 전달한 파라미터 Store를 받는다.
	 * @return Parameter store
	 */
	public Store<?> getParamStore(){
		return param;
	}
	
	/**
	 *  다른 Viewer로 전달한 파라미터 Store를 설정한다.
	 */
	public void setParamStore(Store<Object> store){
		param = store;
	}
	

	public static String[] getBox(Class<?> viewerClass){
		return JwMemberMapper.injectionBox(viewerClass);
	}

/*************************************************** onClick 관련 *********************************************/
	public void setOnClickParam(View view, Object... clickParam){
		if(onClickParams.containsKey(view.hashCode())){
			OnClickListenerReceiver rec = onClickParams.get(view.hashCode());
			rec.setParam(clickParam);
		}
	}

	public Viewer setOnClickListener(View view, String methodName, Class<?>... paramClassTypes){
		OnClickListenerReceiver	oclReceiver = new OnClickListenerReceiver(this);
		oclReceiver.setOnClickListener(view, methodName, paramClassTypes);
		onClickParams.put(view.hashCode(), oclReceiver);
		return this;
	}

	public Viewer setOnClickListener(View view, Method method){
		OnClickListenerReceiver	oclReceiver = new OnClickListenerReceiver(this);
		oclReceiver.setOnClickListener(view, method);
		onClickParams.put(view.hashCode(), oclReceiver);
		return this;
	}

/*************************************************** Task 관련 *********************************************/
	public final static String TASK_ACV="acv"; //어싱크 뷰 체인지 뷰어
	public final static String TASK_APUT="aput"; //어싱크 뷰 인풋 뷰어
	public final static String TASK_LOAD="load"; //어싱크 로딩만
	String task;

	public String getTaskName(){
		return task;
	}


	private void excute(String taskName){
		this.task = taskName;
		String taskKey = getTaskKey(taskName);
		if(TASK_APUT.equals(taskName)){
			taskIndex++;
		}
		else{
			cancelTaskAput();
			cancelTaskAcv();
		}
		ViewerAsyncTask viewerAsyncTask = new ViewerAsyncTask(taskKey, this);
		asyncTaskPool.add(taskKey, viewerAsyncTask);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
			viewerAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			viewerAsyncTask.execute();
		}
	}
	
	public String getTaskKey(String taskName){
		String taskKey = taskName+"_"+parentView.hashCode();
		if(TASK_APUT.equals(taskName)){
			taskKey+=taskIndex;
		}
		return taskKey;
	}
	
	public static void cancelTask(String taskKey){
		ViewerAsyncTask viewerAsyncTask = asyncTaskPool.get(taskKey);
		if(viewerAsyncTask!=null){
			viewerAsyncTask.stop();
		}
	}
	
	public static void cancelTaskAcv(View parents){
		String taskKey = TASK_ACV+"_"+parents.hashCode();
		cancelTask(taskKey);
	}
	public static void cancelTaskAput(View parents){
		String[] keys = getRunTaskKeys();
		for(int i=0;i<keys.length;i++){
			String tempKey = TASK_APUT+"_"+parents.hashCode();
			if(keys[i].indexOf(tempKey)>=0){
				cancelTask(keys[i]);
			}
		}
	}
	
	public void cancelTaskAput(){
		cancelTaskAput(parentView);
	}
	
	public void cancelTaskAcv(){
		String taskKey = getTaskKey(TASK_ACV);
		cancelTask(taskKey);
	}
	
	public void cancelTaskAput(View parents, int taskIndex){
		String taskKey = TASK_APUT+"_"+parents.hashCode()+"_"+taskIndex;
		cancelTask(taskKey);
	}
	public void cancelTaskAput(int taskIndex){
		cancelTaskAput(parentView, taskIndex);
	}
	public static int getRunTaskCount(){
		return asyncTaskPool.size();
	}
	
	public static String[] getRunTaskKeys(){
		return asyncTaskPool.getKeys();
	}


/*************************************************** view control *********************************************/

	private View getLayoutInfalter(int layout_id){
		View v = ((LayoutInflater)builder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
		return v;
	}


/*************************************************** 외부 지원함수 *********************************************/
	/**
	 * Viewer ID를 가져온다.
	 * @return Viewer ID
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Viewer ID를 설정한다.
	 * @param id Viewer ID
	 */
	public void setId(String id){
		this.id =id;
	}
	
	/**
	 * Layout ID를 가져온다.
	 * @return Layout ID
	 */
	public int getLayoutId(){
		return layoutId;
	}
	
	/**
	 * Layout ID를 가져온다.
	 * @param layoutId Layout ID
	 */
	public void setLayoutId(int layoutId){
		this.layoutId = layoutId;
	}
	
	/**
	 * Context 객체를 반환한다.
	 * @return Context
	 */
	public Context getContext(){
		return builder.context;
	}
	/**
	 * viewer의 Context 객체를 반환한다.
	 * @return Context
	 */
	public Context getViewerContext(){
		return viewer.getContext();
	}
	
	/**
	 * Resources를 반환한다.
	 * @return Resources
	 */
	public Resources getResources(){
		return builder.context.getResources();
	}

	/**
	 * 부모의 view를  반환한다.
	 * @return 부모 View
	 */
	public ViewGroup getParent(){
		return this.parentView;
	}
	
	/**
	 * Viewer에해당하는 Layout
	 * @return Viewer에해당하는 Layout
	 */
	public ViewGroup getLayout(){
		return (ViewGroup)viewer;
	}
	
	/**
	 * Viewer에해당하는 View를 가져온다.
	 * @return Viewer에해당하는 View
	 */
	public View getView(){
		return viewer;
	}
	
	/**
	 * activity안에 있는 View를 찾는다
	 * @param R_id_view 찾을 View ID
	 * @return View 해당 View
	 */
	public View findGobalView(int R_id_view){
		return builder.findGobalView(R_id_view);
	}
	
	/**
	 * 현재 viewer에 속해있는 View를 찾는다
	 * @param R_id_view 찾을 View ID
	 * @return View 해당 View
	 */
	public View findViewById(int R_id_view){
		return Jwc.findViewById(R_id_view, viewer);
	}
	/**
	 * parents에 속해있는 View를 찾는다
	 * @param R_id_view 찾을 View ID
	 * @return View  해당 View
	 */
	public View findViewById(int R_id_view, View parents){
		return Jwc.findViewById(R_id_view, parents);
	}
	
	/**
	 * 현재 viewer에 속해있는 View를 tag로 찾는다
	 * @param tag 찾을 tag
	 * @return View 해당 View
	 */
	public View findViewWithTag(String tag){
		return findViewWithTag(tag, viewer);
	}

	/**
	 * parnets에 속해있는 View를 tag로 찾는다
	 * @param tag 찾을 tag
	 * @param parents parnets View
	 * @return View 해당 View
	 */
	public View findViewWithTag(String tag,View parents){
		return Jwc.findViewWithTag(tag, parents);
	}

	/**
	 * Viewer를 visible한다.
	 */
	public void visible(){
		viewer.setVisibility(View.VISIBLE);
	}

	/**
	 * Viewer layout을 삭제한다.
	 */
	public void removeLayout(){
		if(parentView!=null){
				parentView.removeView(this.getLayout());
		}
	}

/*************************************************** 애니메이션 관련 *********************************************/

	Animation outAnimation;
	public Viewer setOutAnim(Animation outAnimation){
		this.outAnimation = outAnimation;
		return this;
	}
	Animation inAnimation;
	public Viewer setInAnim(Animation inAnimation){
		this.inAnimation = inAnimation;
		return this;
	}
	public Viewer setAnimation(Animation animation){
		this.inAnimation = animation;
		return this;
	}
	public Viewer setAnimation(int R_anim_id){
		return setAnimation(AnimationUtils.loadAnimation(getContext(), R_anim_id));
	}
	public Viewer setAnimationOut(Animation animation){
		this.outAnimation = animation;
		return this;
	}
	public Viewer setAnimationOut(int R_anim_id){
		return setAnimationOut(AnimationUtils.loadAnimation(getContext(), R_anim_id));
	}
	boolean outEndAnimate=false;
	public Viewer setAnimation(Animation inAnimation,Animation outAnimation,boolean outEndAnimate){
		this.inAnimation = inAnimation;
		this.outAnimation = outAnimation;
		this.outEndAnimate=outEndAnimate;
		return this;
	}
	public Viewer setAnimation(int R_anim_inId,int R_anim_outId,boolean outEndAnimate){
		return setAnimation(AnimationUtils.loadAnimation(getContext(), R_anim_inId), AnimationUtils.loadAnimation(getContext(), R_anim_outId),outEndAnimate);
	}


/*************************************************** 빌드 관련 *********************************************/

	public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Activity activity){
		ViewerBuilder builder = new ViewerBuilder(jwViewerClass, activity);
		return builder;
	}

	public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Dialog dialog){
		ViewerBuilder builder = new ViewerBuilder(jwViewerClass, dialog);
		return builder;
	}

    public static ViewerBuilder build(Class<? extends Viewer> jwViewerClass, Viewer viewer){
		ViewerBuilder builder;
		if(viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.ACTIVITY)){
			return new ViewerBuilder(jwViewerClass, viewer.getActivity());
		}else if(viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.DIALOG)){
			return new ViewerBuilder(jwViewerClass, viewer.getDialog());
		}else{
			return null;
		}
	}

	public RefreshBuilder reBuild(int requestCode){
		if(reBuilder==null){
			reBuilder = new RefreshBuilder(this);
		}
		reBuilder.setRequestCode(requestCode);
		return reBuilder;
	}

	public RefreshBuilder reBuild(){
		return this.reBuild(REQUEST_CODE_NONE);
	}

//	public void setLoadView(View loadView, UpdateListener updateListener){
////		boolean enableLoad = builder.hasLoadView;
//		builder.setLoadView(loadView, updateListener);
////		builder.hasLoadView = enableLoad;
//	}
//
//	public void setLoadView(int R_layout_id, UpdateListener updateListener){
////		boolean enableLoad = builder.hasLoadView;
//		builder.setLoadView(R_layout_id, updateListener);
////		builder.hasLoadView = enableLoad;
//	}
//
////	public void enableLoadView(boolean enable){
////		builder.hasLoadView = enable;
////	}



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
