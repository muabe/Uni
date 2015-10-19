package com.markjmind.uni;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.hub.Store;

/**
 * 
 * preload
 * async
 * Cache
 * 
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @date 2015. 9. 1.
 */
public class ViewerBuilder {
	public static enum TYPE_MODE{
		ACTIVITY,
		DIALOG
	}

	protected Class<? extends Viewer> jwViewerClass;
	protected Activity activity;
	protected Dialog dialog;
	protected Context context;
	protected int layout_id; 
	protected TYPE_MODE mode;
	protected boolean isAsync = true;
	protected boolean isPreLayout = false;
	protected boolean hasLoadView = false;
	protected View loadView;
	protected Store<Object> param;
	protected UpdateListener updateListener;
	protected int requestCode;

	
	/**
	 * Activity build 생성자
	 * @param R_layout_id
	 * @param jwViewerClass
	 * @param activity
	 */
	public ViewerBuilder(int R_layout_id, Class<? extends Viewer> jwViewerClass, Activity activity){
		this.jwViewerClass = jwViewerClass;
		this.activity = activity;
		this.context = activity;
		this.layout_id = R_layout_id;
		this.mode = TYPE_MODE.ACTIVITY;
		this.param = new Store<Object>();
		this.requestCode = Viewer.REQUEST_CODE_NONE;
	}
	public ViewerBuilder(Class<? extends Viewer> jwViewerClass, Activity activity){
		this(JwMemberMapper.injectionLayout(jwViewerClass), jwViewerClass, activity);
	}
	
	
	/**
	 * Dialog build 생성자
	 * @param R_layout_id
	 * @param jwViewerClass
	 * @param dialog
	 */
	public ViewerBuilder(int R_layout_id, Class<? extends Viewer> jwViewerClass, Dialog dialog){
		this.jwViewerClass = jwViewerClass;
		this.dialog = dialog;
		this.context = dialog.getContext();
		this.layout_id = R_layout_id;
		this.mode = TYPE_MODE.DIALOG;
	}
	public ViewerBuilder(Class<? extends Viewer> jwViewerClass, Dialog dialog){
		this(JwMemberMapper.injectionLayout(jwViewerClass), jwViewerClass, dialog);
	}



/***************************************** 속성 관련 ******************************************/
	
	public ViewerBuilder setAsync(boolean isAsync){
		this.isAsync = isAsync;
		return this;
	}
	public boolean isAsync(){
		return this.isAsync;
	}
	
	
	public ViewerBuilder setPreLayout(boolean isPreLayout){
		this.isPreLayout = isPreLayout;
		return this;
	}
	public boolean isPreLayout(){
		return this.isPreLayout;
	}
	
	public ViewerBuilder setLoadView(View loadView, UpdateListener updateListener){
		hasLoadView = true;
		this.loadView = loadView;
		this.loadView.setClickable(true);
		this.updateListener = updateListener;
		return this;
	}

	public ViewerBuilder setLoadView(int R_layout_id, UpdateListener updateListener){
		return this.setLoadView(getLayoutInfalter(R_layout_id), updateListener);
	}


	public View getLoadView(){
		return loadView;
	}

	public ViewerBuilder setRequestCode(Integer requestCode){
		this.requestCode = requestCode;
		return this;
	}

	public Integer getRequestCode(){
		return this.requestCode;
	}

	

/***************************************** bind 관련******************************************/
	/**
	 * 바인딩한 Viewer를 리턴한다.
	 * @return Viewer
	 */
	private Viewer getViewer(){
		try {
			Viewer jv = jwViewerClass.newInstance();
			jv.builder = this;
			jv.init(layout_id);
			return jv;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Viewer change(ViewGroup parents){
		return getViewer().change(parents);
	}
	
	/**
	 * 부모 ViewGroup 아래의 View들을 모두 지우고<br>
	 * 현재 Viewer로 변경한다.
	 * @param R_id_parents 부모 ViewGroup ID
	 * @return
	 */
	public Viewer change(int R_id_parents){
		return this.change((ViewGroup)findViewById(R_id_parents));
	}

	public Viewer change(Viewer viewer){
		return this.change(viewer.getParent());
	}

	/**
	 * index에 해당하는 부모 ViewGroup아래 JwViewer를 추가한다.
	 * @param parents 부모 ViewGroup ID
	 * @param index index
	 * @return
	 */
	protected Viewer add(ViewGroup parents, int index){
		Viewer jv = getViewer().change(parents);
		jv.add(parents, index);
		return jv;
	}


	public Viewer add(int R_id_parents, int index) {
		ViewGroup parents = (ViewGroup)findViewById(R_id_parents);
		return add(parents, index);
	}

	/**
	 *
	 *
	 * 부모 ViewGroup아래 JwViewer를 추가한다.
	 * @param parents 부모 ViewGroup
	 * @return
	 */
	public Viewer add(ViewGroup parents) {
		return add(parents,-1);
	}

	/**
	 * 부모 ViewGroup아래 JwViewer를 추가한다.
	 * @param R_id_parents 부모 ViewGroup ID
	 * @return
	 */
	public Viewer add(int R_id_parents) {
		ViewGroup parents = (ViewGroup)findViewById(R_id_parents);
		return add(parents);
	}

/************************************************* 파라 미터 ************************************/
	/**
	 * 다른 Viewer로 넘기는 파라미터를 설정한다.
	 * @param key Parameter Key
	 * @param value Parameter Value
	 */
	public ViewerBuilder addParam(String key, Object value){
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
		return (String)getParam(key);
	}

	/**
	 * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.<br>
	 * key에 대응하는 value가 없으면 defalut값을 리턴한다.
	 * @param key Parameter Key
	 * @param defalut value
	 * @return Parameter String Value
	 */
	public String optParamString(String key, String defalut){
		return (String)optParam(key, defalut);
	}
	/**
	 * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.
	 * @param key Parameter Key
	 * @return Parameter Int Value
	 */
	public int getParamInt(String key){
		return (Integer)getParam(key);
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
	public Store<Object> getParamStore(){
		return param;
	}

	/**
	 *  다른 Viewer로 전달한 파라미터 Store를 설정한다.
	 */
	public ViewerBuilder setParamStore(Store<Object> store){
		param = store;
		return this;
	}

/************************************************* 화면 컨트롤 관련 ************************************/
	private View getLayoutInfalter(int layout_id){
		View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
		return v;
	}

	/**
	 * activity안에 있는 View를 찾는다
	 * @param R_id_view 찾을 View ID
	 * @return View 해당 View
	 */
	View findViewById(int R_id_view){
		if(mode.equals(TYPE_MODE.ACTIVITY)){
			return activity.findViewById(R_id_view);
		}else if(mode.equals(TYPE_MODE.DIALOG)){
			return dialog.findViewById(R_id_view);
		}else{
			return null;
		}
	}

}
