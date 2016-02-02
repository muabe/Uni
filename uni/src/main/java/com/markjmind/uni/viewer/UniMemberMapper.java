package com.markjmind.uni.viewer;

import android.content.Context;
import android.view.View;

import com.markjmind.uni.annotiation.Box;
import com.markjmind.uni.annotiation.Param;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.annotiation.OnClick;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */

public class UniMemberMapper {
	
	public static String[] injectionBox(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Box.class)){
			Box par = viewerClass.getAnnotation(Box.class);
			return par.value();
		}else{
			throw new UinMapperException(ErrorMessage.Runtime.box(viewerClass),null);
		}
}

	public static boolean hasInjectionLayout(Class<?> viewerClass){
		return viewerClass.isAnnotationPresent(Layout.class);
	}

	public static int injectionLayout(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Layout.class)){
			Layout lytId = viewerClass.getAnnotation(Layout.class);
			return lytId.value();
		}else{
			throw new UinMapperException(ErrorMessage.Runtime.injectLayout(viewerClass),null);
		}

	}

	public static void injection(Viewer obj){
		Class<?> viewerClass = obj.getClass();
		/** method **/
		HashMap<Integer, View> viewHash = new HashMap<>();
		Method[] methods = viewerClass.getDeclaredMethods();
		for(Method method:methods){
			if(method.isAnnotationPresent(OnClick.class)){ // onClick의 경우
				OnClick oc = method.getAnnotation(OnClick.class);
				int[] list = oc.ids();
				if(list.length==0){
					int id = oc.value();
					if(id==-1) {
						id = getMethodID(viewerClass, method.getName(), obj.getContext());
					}
					View view = setMethod(obj,id,method);
					viewHash.put(id, view);
				}else{
					for(int id:list){
						View view = setMethod(obj,id,method);
						viewHash.put(id, view);
					}
				}
			}
		}

		/** field **/
		Field[] fields = viewerClass.getDeclaredFields();
		for(Field field:fields){
			if(field.isAnnotationPresent(GetView.class)){ // GetView의 경우
				GetView getView = field.getAnnotation(GetView.class);
				int id = getView.value();
				if(id==-1){
					id = getFieldID(viewerClass, field.getName(), obj.getContext());
				}
				setField(obj, id, field, viewHash);
				continue;
			}else if(field.isAnnotationPresent(Param.class)){
				Param param = field.getAnnotation(Param.class);
				String key = param.value();
				if(key.trim().length()==0){
					key = field.getName();
				}
				Object value = obj.getParam(key);
				setField(obj, value, field);
			}

		}
	}

	private static int getFieldID(Class<?> viewerClass, String idName, Context app){
		Class cls = JwStringID.getRClass("id", app);
		Field field;
		try {
			field = cls.getDeclaredField(idName);
			int value = field.getInt(null);
			return value;
		} catch (SecurityException e) {
			throw new SecurityException(ErrorMessage.Runtime.fieldSecurity(viewerClass, idName),e);
		} catch (NoSuchFieldException e) {
			throw new UinMapperException(ErrorMessage.Runtime.fieldNoSuch(viewerClass, idName),e);
		} catch (IllegalArgumentException e) {
			throw new UinMapperException(ErrorMessage.Runtime.fieldIllegalArgument(viewerClass, idName),e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException(ErrorMessage.Runtime.fieldIllegalAccess(viewerClass, idName),e);
		}
	}

	private static View setField(Viewer obj, int id, Field field, HashMap<Integer, View> viewHash){
		View v;
		if(viewHash.containsKey(id)){
			v = viewHash.get(id);
		}else{
			v = obj.findViewById(id);
		}
		if(v==null){
			throw new UinMapperException(ErrorMessage.Runtime.injectField(obj.getClass(), field.getName()));
		}
		try {
			field.setAccessible(true);
			field.set(obj, v);
		}catch (IllegalAccessException e) {
			throw new UinMapperException(ErrorMessage.Runtime.injectFieldIllegalAccess(obj.getClass(), field.getName()),e);
		}
		return v;
	}

	private static void setField(Viewer viewer, Object vaule, Field field){
		try {
			field.setAccessible(true);
			field.set(viewer, vaule);
		}catch (IllegalAccessException e) {
			throw new UinMapperException(ErrorMessage.Runtime.injectFieldIllegalAccess(viewer.getClass(), field.getName()),e);
		}
	}

	private static int getMethodID(Class<?> viewerClass, String idName, Context app) throws UinMapperException {
		Class cls = JwStringID.getRClass("id", app);
		Field field;
		try {
			field = cls.getDeclaredField(idName);
			int value = field.getInt(null);
			return value;
		} catch (SecurityException e) {
			throw new UinMapperException(ErrorMessage.Runtime.methodSecurity(viewerClass, idName),e);
		} catch (NoSuchFieldException e) {
			throw new UinMapperException(ErrorMessage.Runtime.methodNoSuch(viewerClass, idName),e);
		} catch (IllegalArgumentException e) {
			throw new UinMapperException(ErrorMessage.Runtime.methodIllegalArgument(viewerClass, idName),e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException(ErrorMessage.Runtime.methodIllegalAccess(viewerClass, idName),e);
		}
	}

	private static View setMethod(Viewer obj, Integer id, Method method){
		View view = obj.findViewById(id);
		if(view==null){ // view가 없을경우
			throw new UinMapperException(ErrorMessage.Runtime.injectMethod(obj.getClass(), method.getName()),null);
		}
		OnClickListenerReceiver oclReceiver = new OnClickListenerReceiver(obj);
		oclReceiver.setOnClickListener(view, method);
		obj.onClickParams.put(view.hashCode(), oclReceiver);
		return view;
	}



}
