package com.markjmind.mobile.api.android.controller;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.mobile.api.hub.Store;


/**
 * start : 2013.11.17<br>
 * <br>
 * Base Viewer 클래스<br>
 * 일반 Viewer을 정의하는 클래스는<br>
 * BaseLayout 를 상속하여 view_init메소드에 화면을 정의한다. 
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public abstract class JwViewer {
	
	private static ViewerXmlMapper vxm;
	private Context context;
	private Activity activity;
	private Dialog dialog;
	private ViewGroup viewer;
	private ViewGroup parentView;
	private OnClickListenerReceiver oclReceiver;
	private String id;
	private int layoutId;
	private Object param;
	private Store loadingParam;
	
	public static final int INSTANCE_NEW=0;
	public static final int INSTANCE_SAVE=2;
	
	public TYPE_MODE mode;
	enum TYPE_MODE{
		ACTIVITY,
		DIALOG
	}
	/**
	 * Viewer를 재정의해야할 함수
	 * Viewer에 데이터값이나 셋팅하거나 view표현되어야할 값들 셋팅한다.
	 */
	public abstract void view_init();
	
	public boolean loading(){
		return true;
	}
	public void view_fail(){
		
	}
	/**
	 * 기본생성자
	 */
	protected JwViewer(){}

	/**
	 * 생성자 init을 포함한다.
	 * @param activity
	 * @param layout_id
	 */
//	public JwViewer(Activity activity, int layout_id){
//		init(activity, layout_id);
//	}
	
	/**
	 *  Viewer 초기화
	 * @param activity
	 * @param layout_id
	 */
	public void init(Activity activity,int layout_id){
		setLayoutId(layout_id);
		mode = TYPE_MODE.ACTIVITY;
		if(!activity.equals(this.activity)){
			this.activity = activity;
			this.context = activity;
		}
	}
	/**
	 * 
	 * @param activity
	 * @param layout_id
	 */
	public void init(Dialog dialog,int layout_id){
		setLayoutId(layout_id);
		mode = TYPE_MODE.DIALOG;
		if(!dialog.equals(this.dialog)){
			this.dialog = dialog;
			this.context = dialog.getContext();
		}
	}
	
	
	public Activity getActivity(){
		return activity;
	}
	
	public Dialog getDialog(){
		return dialog;
	}
	
	public Object getParameter(){
		return param;
	}
	
	public Object getParameter(Object defalut){
		if(param==null){
			return defalut;
		}
		return param;
	}
	
	public Object getLoadingParameter(String key){
		if(loadingParam==null){
			return null;
		}
		return loadingParam.get(key);
	}
	
	public Object getLoadingParameter(String key, Object defalut){
		if(loadingParam==null){
			return defalut;
		}
		return loadingParam.get(key);
	}
	public void setLoadingParameter(String key, Object value){
		loadingParam.add(key, value);
	}
	
	/**
	 * 매핑을 위해 XML을 로드한다.
	 * @param R_raw_xml 로드할 xml id
	 * @param app Application
	 */
	public static void xmlLoad(int R_raw_xml, Application app){
		if(vxm == null){
			vxm = new ViewerXmlMapper(app);
			vxm.load(R_raw_xml);
		}
	}
	
	/**
	 * Viewer를 등록한다.
	 * @param id Viewer ID name
	 * @param R_layout_id layout ID
	 * @param viewerClass Viewer Class
	 */
	public static void addViewer(String id, int R_layout_id, Class viewerClass){
		JwViewer.addViewer(id, R_layout_id, "",  viewerClass);
	}
	/**
	 * Viewer를 등록한다.
	 * @param id Viewer ID name
	 * @param R_layout_id  layout ID
	 * @param desc 설명
	 * @param viewerClass Viewer Class
	 */
	public static void addViewer(String id, int R_layout_id, String desc, Class viewerClass){
		vxm.add(id, R_layout_id, desc, null, viewerClass);
	}
	/**
	 * Viewer를 등록한다. 
	 * @param id id Viewer ID name
	 * @param jwViewer JwViewer 객체
	 */
	public static void addViewer(String id, JwViewer jwViewer){
		jwViewer.setId(id);
		vxm.add(id, jwViewer.getLayoutId(), "", jwViewer, jwViewer.getClass());
	}
	
	public void viewerInit(){
		viewer = (ViewGroup)Jwc.getViewInfalter(layoutId,context);
	}
	/**
	 *  id에 해당하는 Viewer를 반환한다.
	 * @param id Viewer ID
	 * @param activity activity
	 * @return Viewer
	 */
	public static JwViewer getViewer(String id, Activity activity){
		return vxm.getViewer(id, activity);
	}
	public static JwViewer getViewer(String id, Dialog dialog){
		return vxm.getViewer(id, dialog);
	}
	public static void changeViewer(String id, int R_id_parents, Activity activity){
		vxm.getViewer(id, activity).changeViewer(R_id_parents);
	}
	public static void changeViewer(String id, int R_id_parents, Object param, Activity activity){
		vxm.getViewer(id, activity).changeViewer(R_id_parents, param);
	}
	public static void changeViewer(String id, int R_id_parents, Object param, Dialog dialog){
		vxm.getViewer(id, dialog).changeViewer(R_id_parents, param);
	}
	public static void changeViewer(String id, int R_id_parents, Dialog dialog){
		vxm.getViewer(id, dialog).changeViewer(R_id_parents);
	}
	
	public JwViewer changeViewer(int R_id_parents) {
		return changeViewer(R_id_parents,null);
	}
	
	public JwViewer changeViewer(ViewGroup parents) {
		return changeViewer(parents,null);
	}
	
	public JwViewer changeViewer(int R_id_parents, Object param) {
		this.param = param;
		ViewGroup parents;
		viewerInit();
		if(mode==TYPE_MODE.DIALOG){
			parents = (ViewGroup)Jwc.getView(R_id_parents, dialog);
			Jwc.changeLayout(viewer, (ViewGroup)Jwc.getView(R_id_parents, dialog));
		}else{
			parents = (ViewGroup)Jwc.getView(R_id_parents, activity);
			Jwc.changeLayout(viewer, (ViewGroup)Jwc.getView(R_id_parents, activity));
		}
		parentView = parents;
		view_init();
		return this;
	}
	
	public JwViewer  changeViewer(ViewGroup parents, Object param) {
		this.param = param;
		viewerInit();
		View view = Jwc.changeLayout(viewer, parents);
		parentView = parents;
		view_init();
		return this;
	}

	public JwViewer  addViewer(int R_id_parents,Object param) {
		this.param = param;
		ViewGroup parents = (ViewGroup)Jwc.getView(R_id_parents, activity);
		viewerInit();
		View view = Jwc.addLayout(viewer, parents);
		parentView = parents;
		view_init();
		return this;
	}
	
	/**
	 * Asyc
	 * @param R_id_parents
	 * @param param
	 * @return
	 */
	public static Store asyncStore = new Store();
	boolean isIndoBack = false;
	static Refresh ref;
	public static void asyncChangeViewer(String id, int R_id_parents, Activity activity, boolean isIndoBack){
		vxm.getViewer(id, activity).asyncChangeViewer(R_id_parents, isIndoBack);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Object param, Activity activity, boolean isIndoBack){
		vxm.getViewer(id, activity).asyncChangeViewer(R_id_parents, param, isIndoBack);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Object param, Dialog dialog, boolean isIndoBack){
		vxm.getViewer(id, dialog).asyncChangeViewer(R_id_parents, param, isIndoBack);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Dialog dialog, boolean isIndoBack){
		vxm.getViewer(id, dialog).asyncChangeViewer(R_id_parents, isIndoBack);
	}
	
	public static void asyncChangeViewer(String id, int R_id_parents, Activity activity){
		vxm.getViewer(id, activity).asyncChangeViewer(R_id_parents, false);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Object param, Activity activity){
		vxm.getViewer(id, activity).asyncChangeViewer(R_id_parents, param, false);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Object param, Dialog dialog){
		vxm.getViewer(id, dialog).asyncChangeViewer(R_id_parents, param, false);
	}
	public static void asyncChangeViewer(String id, int R_id_parents, Dialog dialog){
		vxm.getViewer(id, dialog).asyncChangeViewer(R_id_parents, false);
	}
	
	
	
	public JwViewer asyncChangeViewer(int R_id_parents, boolean isIndoBack) {
		return asyncChangeViewer(R_id_parents,null, isIndoBack);
	}
	
	public JwViewer asyncChangeViewer(ViewGroup parents, boolean isIndoBack) {
		return asyncChangeViewer(parents,null, isIndoBack);
	}
	
	public JwViewer asyncChangeViewer(int R_id_parents, Object param, boolean isIndoBack) {
		ViewGroup parents;
		if(mode==TYPE_MODE.DIALOG){
			parents = (ViewGroup)Jwc.getView(R_id_parents, dialog);
		}else{
			parents = (ViewGroup)Jwc.getView(R_id_parents, activity);
		}
		return asyncChangeViewer(parents, param,isIndoBack);
	}
	
	public JwViewer  asyncChangeViewer(ViewGroup parents, Object param, boolean isIndoBack) {
		this.param = param;
		parentView = parents;
		this.isIndoBack = isIndoBack;
		excute();
		return this;
	}
	
	/**
	 * 클래스로 직접 컨트롤
	 * @param R_id_parents
	 * @param param
	 * @return
	 */
	public static void asyncChangeViewer(int layout_id, Class jwViewerClass, int R_id_parents, Object param, Activity activity, boolean isIndoBack){
		try {
			JwViewer viewer = (JwViewer)jwViewerClass.newInstance();
			viewer.init(activity, layout_id);
			viewer.asyncChangeViewer(R_id_parents, param, isIndoBack);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	public static void asyncChangeViewer(int layout_id, Class jwViewerClass, int R_id_parents, Object param, Activity activity){
		asyncChangeViewer(layout_id, jwViewerClass, R_id_parents, param, activity, false);
	}
	public static void asyncChangeViewer(int layout_id, Class jwViewerClass, int R_id_parents, Activity activity){
		asyncChangeViewer(layout_id, jwViewerClass, R_id_parents, null, activity, false);
	}
	public static void asyncChangeViewer(int layout_id, Class<JwViewer> jwViewerClass, int R_id_parents, Object param, Dialog dialog, boolean isIndoBack){
		try {
			JwViewer viewer = jwViewerClass.newInstance();
			viewer.init(dialog, layout_id);
			viewer.asyncChangeViewer(R_id_parents, param, isIndoBack);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	public static void asyncChangeViewer(int layout_id, Class<JwViewer> jwViewerClass, int R_id_parents, Object param, Dialog dialog){
		asyncChangeViewer(layout_id, jwViewerClass, R_id_parents, param, dialog, false);
	}
	public static void asyncChangeViewer(int layout_id, Class<JwViewer> jwViewerClass, int R_id_parents, Dialog dialog){
		asyncChangeViewer(layout_id, jwViewerClass, R_id_parents, null, dialog, false);
	}
	
	
	private void excute(){
		if(ref!=null){
			ref.stop();
			ref.cancel(true);
		}
		ref = new Refresh();
		
//		ref.onPreExecute();
		ref.execute(param);
	}

	public JwViewer  asyncAddViewer(int R_id_parents,Object param, boolean isIndoBack) {
//		this.param = param;
//		ViewGroup parents ;
//		if(mode==TYPE_MODE.DIALOG){
//			parents = (ViewGroup)Jwc.getView(R_id_parents, dialog);
//		}else{
//			parents = (ViewGroup)Jwc.getView(R_id_parents, activity);
//		}
//		View view = Jwc.addLayout(viewer, parents);
//		parentView = parents;
//		Refresh ref = new Refresh();
//		ref.execute(param);
		return this;
	}
	

	public void view_pre(){
		if(parentView!=null){
			parentView.removeAllViews();
		}
	}
	public void onProgressUpdate(Integer... values){
	}
	
	//////////////////////////////////////////////////////////////////////////////
	private  class Refresh extends AsyncTask<Object, Integer, Object>{
		boolean isStop;
		
		public void stop(){
			isStop=true;
		}
		@Override
		protected void onPreExecute() {
			isStop = false;
			JwViewer.this.view_pre();
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			JwViewer.this.onProgressUpdate(values);
		}
		@Override
		protected Object doInBackground(Object... params) {
			loadingParam = new Store();
			loadingParam.clear();
			if(isIndoBack){
				viewerInit();
			}
			return loading(); //데이터 가져오기
		}
		@Override
		protected void onPostExecute(Object result) {
			boolean isView = (Boolean)result;
			if(isView){
				if(!isStop){
					if(!isIndoBack){
						viewerInit();
					}
					View view = Jwc.changeLayout(viewer, parentView);
					view_init();
				}else{
					view_fail();
//					Log.d("Jwviewr", "오재웅 Jwviewr stop2");
				}
			}
		}
	}
	
	public void setPreView(int R_layout_id){
		if(parentView!=null){
			parentView.removeAllViews();
		}
		ViewGroup loading = (ViewGroup) Jwc.getViewInfalter( R_layout_id, getParent().getContext());
		loading.setLayoutParams(getParent().getLayoutParams());
		((ViewGroup)getParent()).addView(loading);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Viewer ID를 가져온다.
	 * @return Viewer ID
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Viewer ID를 설정한다.
	 * @param Viewer ID
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
	 * @param Layout ID
	 */
	public void setLayoutId(int layoutId){
		this.layoutId = layoutId;
	}
	
	/**
	 * Context 객체를 반환한다.
	 * @return Context
	 */
	public Context getContext(){
		return context;
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
		return context.getResources();
	}
	
	
	/**
	 * id에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param R_id_view 이벤트를 등록할 id
	 */
	public void setOnClickListener(String methodName, int R_id_view){
//		if(oclReceiver==null){
			oclReceiver = new OnClickListenerReceiver(context);
//		}
		oclReceiver.setOnClickListener(this, methodName, R_id_view, viewer);
	}
	/**
	 * parents의 child view중 tag에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param tag 이벤트를 등록할 tag
	 * @param parents tag를 찾을 parents view 
	 */
	public void setOnClickListener(String methodName, String tag, View parents){
//		if(oclReceiver==null){
			oclReceiver = new OnClickListenerReceiver(context);
//		}
		oclReceiver.setOnClickListener(this, methodName, getViewTag(tag, parents));
	}
	
	/**
	 * tag에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param tag 이벤트를 등록할 tag
	 */
	public void setOnClickListener(String methodName, String tag){
		setOnClickListener(methodName, tag, viewer);
	}
	
	/**
	 * tag에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param tag 이벤트를 등록할 tag
	 */
	public void setOnClickParamListener(String methodName, String tag, Object param){
		setOnClickParamListener(methodName, tag, viewer,param);
	}
	
	/**
	 * id에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param R_id_view 이벤트를 등록할 id
	 */
	public void setOnClickParamListener(String methodName, int R_id_view, Object param){
//		if(oclReceiver==null){
			oclReceiver = new OnClickListenerReceiver(context);
//		}
		oclReceiver.setOnClickParamListener(this, methodName, R_id_view, viewer,param);
	}
	/**
	 * parents의 child view중 tag에 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param tag 이벤트를 등록할 tag
	 * @param parents tag를 찾을 parents view 
	 */
	public void setOnClickParamListener(String methodName, String tag, View parents, Object param){
//		if(oclReceiver==null){
			oclReceiver = new OnClickListenerReceiver(context);
//		}
		oclReceiver.setOnClickParamListener(this, methodName, getViewTag(tag, parents),param);
	}
	

	
	/**
	 * tag명과 동일한 메소드를 찾아 해당하는 뷰에 이벤트를  등록한다.
	 * @param methodName 메소드명
	 * @param tag 이벤트를 등록할 tag
	 */
	public void setOnClickListenerByTag(String tag){
		setOnClickListener(tag, tag, viewer);
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
		return viewer;
	}
	
	/**
	 * activity안에 있는 View를 찾는다
	 * @param R_id_view
	 * @return View
	 */
	public View findViewById(int R_id_view){
		return viewer.findViewById(R_id_view);
	}
	
	/**
	 * 현재 viewer에 속해있는 View를 찾는다
	 * @param R_id_view
	 * @return View
	 */
	public View getView(int R_id_view){
		return Jwc.getView(R_id_view, viewer);
	}
	/**
	 * 현재 viewer에 속해있는 View를 tag로 찾는다
	 * @param tag
	 * @param parents
	 * @return View
	 */
	public View getViewTag(String tag,View parents){
		return Jwc.getViewTag(tag, parents);
	}
	/**
	 * 현재 viewer에 속해있는 View를 tag로 찾는다
	 * @param tag
	 * @param parents
	 * @return View
	 */
	public View getViewTag(String tag){
		return getViewTag(tag, viewer);
	}
	
	public void visible(){
		viewer.setVisibility(View.VISIBLE);
	}
	
	public void inVisible(){
		viewer.setVisibility(View.INVISIBLE);
	}
	
	public void gone(){
		viewer.setVisibility(View.GONE);
	}
	
	public void removeLayout(){
		if(parentView!=null){
			if(parentView instanceof ViewGroup){
				((ViewGroup)parentView).removeView(this.getLayout());
			}
		}
	}
}
