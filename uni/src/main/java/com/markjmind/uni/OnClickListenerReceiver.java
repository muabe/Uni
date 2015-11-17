package com.markjmind.uni;

import android.view.View;
import android.view.View.OnClickListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * start : 2013.11.17<br>
 * <br>
 * OnClick 이벤트 콜백 리시버 패턴 클래스<br>
 * 
 * @author 오재웅
 *
 */
public class OnClickListenerReceiver implements OnClickListener{
	private Object receiver;
	private Method method;
	private ArrayList<Object> param;


	public OnClickListenerReceiver(Object receiver){
		this.receiver = receiver;
		this.param = new ArrayList<>();
		this.param.add(null);
	}

	@Override
	public void onClick(View v) {
		invoke(receiver, v);
	}
	
//	 private void invoke(Object receiver, String methodName,View v){
//		 Class cls=receiver.getClass();
//		 try {
//				if(param!=null){
//					Class[] params = {View.class, param.getClass()};
//					Method method = cls.getMethod(methodName, params);
//					method.invoke(receiver, v, param);
//				}else{
//					Class[] params = {View.class};
//					Method method = cls.getMethod(methodName, params);
//					method.invoke(receiver, v);
//				}
//			} catch (IllegalArgumentException e) {
//				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 함수에 매개변수가 잘못 지정되었습니다.",e);
//			} catch (IllegalAccessException e) {
//				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 접근권한이 없는 함수입니다.",e);
//			} catch (InvocationTargetException e) {
//				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", 함수를 실행하는중 Exception이 발생하였습니다.",e);
//			}catch (NoSuchMethodException e) {
//				throw new JwMapperException("\n["+receiver.getClass().getName()+"]method:"+methodName+", Field에 해당하는 OnClick 함수가 없습니다.",e);
//			}
//	 }

	public void setParam(Object... param){
		this.param.clear();
		this.param.add(null);
		if(param!=null) {
			this.param.addAll(Arrays.asList(param));
		}
	}

	private void invoke(Object receiver, View v){
		try {
			Class<?>[] paramTypes = method.getParameterTypes();

			if(paramTypes==null || paramTypes.length==0 || paramTypes[0] != View.class){
				throw new UinMapperException("\n["+receiver.getClass().getName()+"."+method.getName()+", 첫번째 Parameter인 View를 지정하지 않았습니다.",null);
			}

			//TODO 파라미터가 index를 넘어 버린다면??
			param.set(0, v);
			for(int i=param.size();i<paramTypes.length;i++){
				param.add(null);
			}
			method.invoke(receiver, param.toArray());

		} catch (IllegalArgumentException e) {
			throw new UinMapperException("\n["+receiver.getClass().getName()+"."+method.getName()+", 함수에 매개변수가 잘못 지정되었습니다.",e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException("\n["+receiver.getClass().getName()+"."+method.getName()+", 접근권한이 없는 함수입니다.",e);
		} catch (InvocationTargetException e) {
			throw new UinMapperException("\n["+receiver.getClass().getName()+"."+method.getName()+", 함수를 실행하는중 Exception이 발생하였습니다.",e);
		}
	}
	 
	public void setOnClickListener(View target, Method method){
		this.method = method;
		target.setOnClickListener(this);
	}
	public void setOnClickListener(View view, String methodName, Class<?>... paramClassType){
		try {
			int paramCount = 1;
			if(paramClassType!=null){
				paramCount = paramClassType.length+1;
			}
			Class<?>[] types = new Class[paramCount];
			types[0]=View.class;
			for(int i=0;i<paramClassType.length;i++){
				types[i+1] = paramClassType[i];
			}
			Method method = getClass().getMethod(methodName, types);
			setOnClickListener(view, method);
		} catch (NoSuchMethodException e) {
			int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
			throw new UinMapperException("\n"+getClass().getName()+"."+methodName+"(View view), method가 존재하지 않습니다.",e);
		}
	}
}