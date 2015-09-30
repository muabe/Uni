package com.markjmind.uni;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * start : 2013.11.17<br>
 * <br>
 * OnClick 이벤트 콜백 리시버 패턴 클래스<br>
 * 
 * @author 오재웅
 *
 */
public class OnClickListenerReceiver implements OnClickListener{
	private Context context; 
	private Object receiver;
	private String methodName;
	private Object param;
	 
	private OnClickListenerReceiver(){
		
	}
	 
	 public OnClickListenerReceiver(Context context){
		 this.context = context;
	 }
	 public void setParam(Object param){
		 this.param = param;
	 }
	@Override
	public void onClick(View v) {
		invoke(receiver,methodName,v);
	}
	
	 private void invoke(Object receiver, String methodName,View v){
		 Class cls=receiver.getClass();
		 try {
				if(param!=null){
					Class[] params = {View.class, param.getClass()};
					Method method = cls.getMethod(methodName, params);
					method.invoke(receiver, v, param);
				}else{
					Class[] params = {View.class};
					Method method = cls.getMethod(methodName, params);
					method.invoke(receiver, v);
				}
			} catch (IllegalArgumentException e) {
				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 함수에 매개변수가 잘못 지정되었습니다.",e);
			} catch (IllegalAccessException e) {
				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 접근권한이 없는 함수입니다.",e);
			} catch (InvocationTargetException e) {
				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 함수를 실행하는중 Exception이 발생하였습니다.",e);
			}catch (NoSuchMethodException e) {
				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", Field에 해당하는 OnClick 함수가 없습니다.",e);
			}
	 }
	 
	 /**
	  * 해당 receiver 클래스의 함수를 R_id_view에 Click리스너로 등록한다.
	  * @param receiver 함수를 호출할 클래스 객체
	  * @param methodName 함수명
	  * @param R_id_view 이벤트를 등록할 view id
	  */
	public void setOnClickListener(Object receiver, String methodName,int R_id_view, View parents){
		parents.findViewById(R_id_view).setOnClickListener(getOnClickListener(receiver,methodName));
	}
	
	 /**
	  * 해당 receiver 클래스의 함수를 R_id_view에 Click리스너로 등록한다.
	  * @param receiver 함수를 호출할 클래스 객체
	  * @param methodName 함수명
	  * @param R_id_view 이벤트를 등록할 view id
	 * @param activity 해당 activity
	 */
	public static void setOnClickListener(Object receiver, String methodName,int R_id_view, Activity activity){
		activity.findViewById(R_id_view).setOnClickListener(getOnClickListener(receiver,methodName));
	}
	
	/**
	 * 해당 receiver 클래스의 함수를 view에 Click리스너로 등록한다.
	 * @param receiver	함수를 호출할 클래스 객체
	 * @param methodName	함수명
	 * @param view 이벤트를 등록할 view
	 */
	public void setOnClickListener(Object receiver, String methodName,View view){
		view.setOnClickListener(getOnClickListener(receiver,methodName));
	}
	
	/**
	 * 해당 receiver 클래스의 함수를 OnClickListener리스너로 반환한다.
	 * @param receiver 함수를 호출할 클래스 객체
	 * @param methodName 함수명
	 * @return OnClickListener OnClickListener 
	 */
	public static OnClickListener getOnClickListener(Object receiver, String methodName){
		OnClickListenerReceiver ocl = new OnClickListenerReceiver();
		ocl.receiver = receiver;
		ocl.methodName = methodName;
		return ocl;
	 }
	 
	 /**
	  * 해당 receiver 클래스의 함수를 R_id_view에 Click리스너로 등록한다.
	  * @param receiver 함수를 호출할 클래스 객체
	  * @param methodName 함수명
	  * @param R_id_view 이벤트를 등록할 view id
	  */
	public void setOnClickParamListener(Object receiver, String methodName,int R_id_view, View parents,Object param){
		parents.findViewById(R_id_view).setOnClickListener(getOnClickParamListener(receiver,methodName,param));
	}
	
	 /**
	  * 해당 receiver 클래스의 함수를 R_id_view에 Click리스너로 등록한다.
	  * 파라미터를 추가할수 있다.
	  * @param receiver 함수를 호출할 클래스 객체
	  * @param methodName 함수명
	  * @param param
	  * @param R_id_view 이벤트를 등록할 view id
	  * @param activity 해당 activity
	 */
	public static void setOnClickParamListener(Object receiver, String methodName,int R_id_view, Activity activity,Object param){
		activity.findViewById(R_id_view).setOnClickListener(getOnClickParamListener(receiver,methodName,param));
	}
	
	/**
	 * 해당 receiver 클래스의 함수를 view에 Click리스너로 등록한다.
	 * 파라미터를 추가할수 있다.
	 * @param receiver	함수를 호출할 클래스 객체
	 * @param methodName	함수명
	 * @param param
	 * @param view 이벤트를 등록할 view
	 */
	public void setOnClickParamListener(Object receiver, String methodName,View view,Object param){
		view.setOnClickListener(getOnClickParamListener(receiver,methodName,param));
	}
	
	/**
	 * 해당 receiver 클래스의 함수를 OnClickListener리스너로 반환한다.
	 * 파라미터를 추가할수 있다.
	 * @param receiver 함수를 호출할 클래스 객체
	 * @param methodName 함수명
	 * @param param
	 * @return OnClickListener OnClickListener 
	 */
	public static OnClickListener getOnClickParamListener(Object receiver, String methodName, Object param){
		OnClickListenerReceiver ocl = new OnClickListenerReceiver();
		ocl.setParam(param);
		ocl.receiver = receiver;
		ocl.methodName = methodName;
		return ocl;
	 }
}