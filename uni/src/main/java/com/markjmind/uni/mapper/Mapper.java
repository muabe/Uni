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

	protected Method[] methods;
	protected Field[] fields;
	protected HashMap<Class<?>, MapperAdapter> adapterMap = new HashMap<>();

	protected Mapper(){

	}

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
		reset(finder, targetObject);
	}

	public Mapper(Activity finder, Object targetObject){
		reset(finder, targetObject);
	}

	public Mapper(Dialog finder, Object targetObject){
		reset(finder, targetObject);
	}

	public void reset(View finder, Object targetObject){
		this.context = finder.getContext();
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
		adapterMap.clear();
		viewHash.clear();
	}

	public void reset(Activity finder, Object targetObject){
		this.context = finder;
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
		adapterMap.clear();
		viewHash.clear();
	}

	public void reset(Dialog finder, Object targetObject){
		this.context = finder.getContext();
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
		adapterMap.clear();
		viewHash.clear();
	}

	protected void setAdapterList(AccessibleObject[] abs, ArrayList<MapperAdapter<?, ?>> adapterList){
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
	protected void setClassAdapter(ArrayList<Class<?>> classArrayList, ArrayList<MapperAdapter<?, ?>> adapterList){
		if(adapterList!=null) {
			for (Class<?> ab : classArrayList) {
				Annotation[] annotations = ab.getDeclaredAnnotations();
				if (annotations != null && annotations.length > 0) {
					for (Annotation annotation : annotations) {
						for (MapperAdapter adapter : adapterList) {
							if (annotation.annotationType().equals(adapter.getAnnotationClass())) {
								adapter.inject(ab.getAnnotation(adapter.getAnnotationClass()), targetClass, adapterList);
							}
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
			if(types==null || types.length == 0){
				types = new Class[adapterMap.size()];
				types = adapterMap.keySet().toArray(types);
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
			setClassAdapter(getClasses(targetClass), clzList);
			if (methods == null) {
				methods = getMethods(targetClass);
			}
			setAdapterList(methods, methodList);
			if (fields == null) {
				fields = getFields(targetClass);
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

	public View findViewById(int id){
		return finder.findViewById(id);
	}

	Class<?> parentsClass;
	public void setInjectParents(Class<?> parentsClass){
		this.parentsClass = parentsClass;
	}

	private ArrayList<Class<?>> getClasses(Class<?> targetClass){
		ArrayList<Class<?>> classesList = new ArrayList();
		if(parentsClass!=null){
			Class<?> tempClass = targetClass;
			Class<?> superClass = tempClass.getSuperclass();
			while(parentsClass.equals(superClass) || !Object.class.equals(superClass)){
				classesList.add(0, superClass);
				tempClass = superClass;
				superClass = tempClass.getSuperclass();
			}

		}
		classesList.add(targetClass);
		return classesList;
	}

	private Method[] getMethods(Class<?> targetClass){
		if(parentsClass!=null){
			Class<?> superClass = targetClass.getSuperclass();
			if(!parentsClass.equals(superClass) || Object.class.equals(superClass)){
				Method[] superMethods = getMethods(superClass);
				Method[] targetMethods = targetClass.getDeclaredMethods();
				Method[] allArray = new Method[targetMethods.length+superMethods.length];
				System.arraycopy(targetMethods, 0, allArray, 0, targetMethods.length);
				System.arraycopy(superMethods, 0, allArray, targetMethods.length, superMethods.length);
				return allArray;
			}
		}
		return targetClass.getDeclaredMethods();
	}

	private Field[] getFields(Class<?> targetClass){
		if(parentsClass!=null){
			Class<?> superClass = targetClass.getSuperclass();
			if(!superClass.equals(parentsClass) || Object.class.equals(superClass)) {
				Field[] superMethods = getFields(superClass);
				Field[] targetMethods = targetClass.getDeclaredFields();
				Field[] allArray = new Field[targetMethods.length + superMethods.length];
				System.arraycopy(targetMethods, 0, allArray, 0, targetMethods.length);
				System.arraycopy(superMethods, 0, allArray, targetMethods.length, superMethods.length);
				return allArray;
			}
		}
		return targetClass.getDeclaredFields();
	}
}
