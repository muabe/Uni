package com.markjmind.uni.mapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.markjmind.uni.annotiation.Box;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.annotiation.OnClick;
import com.markjmind.uni.annotiation.Param;
import com.markjmind.uni.exception.ErrorMessage;
import com.markjmind.uni.exception.UinMapperException;
import com.markjmind.uni.viewer.JwStringID;
import com.markjmind.uni.viewer.OnClickListenerReceiver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Mapper {

	private Context context;
	private Finder finder;
	private Object injectionObject;

	private abstract class Finder{
		private Object obj;

		public Finder(Object obj){
			this.obj = obj;
		}

		public <T>T getFinder(Class<T> clz){
			return (T)obj;
		}

		public abstract View findViewById(int id);
	}

	public Mapper(){}


	public Mapper(View finder, Object injectionObject){
		setFinder(finder, injectionObject);
	}

	public Mapper(Activity finder, Object injectionObject){
		setFinder(finder, injectionObject);
	}

	public Mapper(Dialog finder, Object injectionObject){
		setFinder(finder, injectionObject);
	}

	public void setFinder(View finder, Object injectionObject){
		this.context = finder.getContext();
		this.injectionObject = injectionObject;
		final Class<View> clz = (Class<View>)finder.getClass();
		this.finder = new Finder(finder){
			@Override
			public View findViewById(int id) {
				return getFinder(clz).findViewById(id);
			}
		};
	}

	public void setFinder(Activity finder, Object injectionObject){
		this.context = finder;
		this.injectionObject = injectionObject;
		final Class<Activity> clz = (Class<Activity>)finder.getClass();
		this.finder = new Finder(finder){
			@Override
			public View findViewById(int id) {
				return getFinder(clz).findViewById(id);
			}
		};
	}

	public void setFinder(Dialog finder, Object injectionObject){
		this.context = finder.getContext();
		this.injectionObject = injectionObject;
		final Class<Dialog> clz = (Class<Dialog>) finder.getClass();
		this.finder = new Finder(finder){
			@Override
			public View findViewById(int id) {
				return getFinder(clz).findViewById(id);
			}
		};
	}

	public View findViewById(int id){
		return finder.findViewById(id);
	}
	
	public String[] injectionBox(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Box.class)){
			Box par = viewerClass.getAnnotation(Box.class);
			return par.value();
		}else{
			throw new UinMapperException(ErrorMessage.Runtime.box(viewerClass),null);
		}
	}

	public int injectionLayout(Class<?> viewerClass){
		if(hasLayout(viewerClass)){
			Layout lytId = viewerClass.getAnnotation(Layout.class);
			return lytId.value();
		}else{
			throw new UinMapperException(ErrorMessage.Runtime.injectLayout(viewerClass),null);
		}
	}

	public static boolean hasLayout(Class<?> viewerClass){
		return viewerClass.isAnnotationPresent(Layout.class);
	}

	public int injectionLayout(){
		return injectionLayout(injectionObject.getClass());
	}

	public void injectionView(){
		Class<?> viewerClass = injectionObject.getClass();
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
						id = getMethodID(viewerClass, method.getName(), context);
					}
					View view = setMethod(injectionObject,id,method);
					viewHash.put(id, view);
				}else{
					for(int id:list){
						View view = setMethod(injectionObject,id,method);
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
					id = getFieldID(viewerClass, field.getName(), context);
				}
				setField(injectionObject, id, field, viewHash);
				continue;
			}else if(field.isAnnotationPresent(Param.class)){
				Param param = field.getAnnotation(Param.class);
				String key = param.value();
				if(key.trim().length()==0){
					key = field.getName();
				}
				//TODO 파이미터 설정
//				Object value = obj.getParam(key);
//				setField(obj, value, field);
			}
		}
	}

	private int getFieldID(Class<?> viewerClass, String idName, Context app){
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

	private View setField(Object obj, int id, Field field, HashMap<Integer, View> viewHash){
		View v;
		if(viewHash.containsKey(id)){
			v = viewHash.get(id);
		}else{
			v = findViewById(id);
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

	private void setField(Object viewer, Object vaule, Field field){
		try {
			field.setAccessible(true);
			field.set(viewer, vaule);
		}catch (IllegalAccessException e) {
			throw new UinMapperException(ErrorMessage.Runtime.injectFieldIllegalAccess(viewer.getClass(), field.getName()),e);
		}
	}

	private int getMethodID(Class<?> viewerClass, String idName, Context app) throws UinMapperException {
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

	private View setMethod(Object obj, Integer id, Method method){
		View view = findViewById(id);
		if(view==null){ // view가 없을경우
			throw new UinMapperException(ErrorMessage.Runtime.injectMethod(obj.getClass(), method.getName()),null);
		}
		OnClickListenerReceiver oclReceiver = new OnClickListenerReceiver(obj);
		oclReceiver.setOnClickListener(view, method);

		//TODO 파이미터 설정
//		obj.onClickParams.put(view.hashCode(), oclReceiver);
		return view;
	}



}
