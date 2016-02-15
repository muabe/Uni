package com.markjmind.uni.mapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Mapper {
	protected Context context;
	protected Finder finder;
	protected Object targetObject;
	protected Class<?> targetClass;
	protected HashMap<Integer, View> viewHash = new HashMap<>();

	private Method[] methods;
	private Field[] fields;
	private HashMap<Class<?>, MapperAdapter> adapterMap = new HashMap<>();

	public Mapper(View finder){
		this(finder, finder);
	}

	public Mapper(Activity finder) {
		this(finder, finder);
	}

	public Mapper(Dialog finder){
		this(finder, finder);
	}

	public Mapper(View finder, Object targetObject){
		this.context = finder.getContext();
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
	}

	public Mapper(Activity finder, Object targetObject){
		this.context = finder;
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
	}

	public Mapper(Dialog finder, Object targetObject){
		this.context = finder.getContext();
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
	}

	private void setAdapterList(AccessibleObject[] abs, ArrayList<MapperAdapter<?, ?>> adapterList){
		if(abs!=null) {
			for (AccessibleObject ab : abs) {
				Annotation[] annotations = ab.getDeclaredAnnotations();
				if (annotations != null && annotations.length > 0) {
					for (Annotation annotation : annotations) {
						for (MapperAdapter adapter : adapterList) {
							if (annotation.annotationType().equals(adapter.getAnnotationClass())) {
								adapter.inject(ab.getAnnotation(adapter.getAnnotationClass()), ab, adapterList);
							}
						}
					}
				}
			}
		}
	}
	private void setClassAdapter(ArrayList<MapperAdapter<?, ?>> classList){
		if(classList!=null) {
			Annotation[] annotations = targetClass.getDeclaredAnnotations();
			if (annotations != null && annotations.length > 0) {
				for (Annotation annotation : annotations) {
					for (MapperAdapter adapter : classList) {
						if (annotation.annotationType().equals(adapter.getAnnotationClass())) {
							adapter.inject(targetClass.getAnnotation(adapter.getAnnotationClass()), targetClass, classList);
						}
					}
				}
			}
		}
	}

	public void clearAdapter(){
		adapterMap.clear();
	}

	public void inject(Class<? extends MapperAdapter<? extends Annotation, ?>>... types){
		if(adapterMap.size()>0) {
			if(types==null){
				types = new Class[adapterMap.size()];
				types = adapterMap.keySet().toArray(types);
				inject(types);
			}
			ArrayList<MapperAdapter<?, ?>> clzList = new ArrayList<>();
			ArrayList<MapperAdapter<?, ?>> fieldList = new ArrayList<>();
			ArrayList<MapperAdapter<?, ?>> methodList = new ArrayList<>();
			for (Class type : types) {
				MapperAdapter<?, ?> adapter = adapterMap.get(type);
				Class<?> mapperType = adapter.getType();
				if (mapperType.equals(Class.class)) {
					clzList.add(adapter);
				} else if (mapperType.equals(Field.class)) {
					fieldList.add(adapter);
				} else if (mapperType.equals(Method.class)) {
					methodList.add(adapter);
				}
			}
			setClassAdapter(clzList);
			if (methods == null) {
				methods = targetClass.getDeclaredMethods();
			}
			setAdapterList(methods, methodList);
			if (fields == null) {
				fields = targetClass.getDeclaredFields();
			}
			setAdapterList(fields, fieldList);
		}
	}

	public void injectWithout(Class<? extends MapperAdapter<? extends Annotation, ?>>... types){
		if(types==null){
			inject();
		}else{
			HashMap<Class<?>, MapperAdapter> tempMap = new HashMap<>();
			tempMap.putAll(adapterMap);
			for(Class<? extends MapperAdapter<? extends Annotation, ?>> type : types){
				tempMap.remove(type);
			}
			types = new Class[tempMap.size()];
			types = tempMap.keySet().toArray(types);
			inject(types);
		}
	}
	public void addAdapter(MapperAdapter adapter){
		adapter.setMapper(this);
		adapterMap.put(adapter.getClass(), adapter);
	}

	public <T extends MapperAdapter<? extends Annotation, ?>>T getAdapter(Class<T> type){
		return (T) adapterMap.get(type);
	}

	public <T extends MapperAdapter<? extends Annotation, ?>>void removeAdapter(Class<T> type){
		adapterMap.remove(type);
	}

}
