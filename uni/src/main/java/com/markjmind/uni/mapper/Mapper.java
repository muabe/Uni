package com.markjmind.uni.mapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Mapper {
	protected Context context;
	public Finder finder;
	protected Object targetObject;
	protected Class<?> targetClass;
	protected HashMap<Integer, View> viewHash = new HashMap<>();

	private Method[] methods;
	private Field[] fields;
	private ArrayList<MapperAdapter<?, Class<?>>> classObList = new ArrayList<>();
	private ArrayList<MapperAdapter<?, AccessibleObject>> fieldObList = new ArrayList<>();
	private ArrayList<MapperAdapter<?, AccessibleObject>> methodObList = new ArrayList<>();


	public Mapper(View finder){
		this(finder, finder);
	}

	public Mapper(Activity finder){
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
		initAdapter();
	}

	public Mapper(Activity finder, Object targetObject){
		this.context = finder;
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
		initAdapter();
	}

	public Mapper(Dialog finder, Object targetObject){
		this.context = finder.getContext();
		this.targetObject = targetObject;
		this.finder = new Finder(finder);
		this.targetClass = targetObject.getClass();
		initAdapter();
	}

	private void initAdapter(){
		addAdapter(new GetViewAdapter());
		addAdapter(new GetViewAdapter());
	}

	private void addAdapter(MapperAdapter adapter, ArrayList<MapperAdapter<?, Class<?>>> clz, ArrayList<MapperAdapter<?, AccessibleObject>> field, ArrayList<MapperAdapter<?, AccessibleObject>> method){
		adapter.setMapper(this);
		if(adapter.getType().equals(Class.class)){
			clz.add(adapter);
		}else if(adapter.getType().equals(Field.class)) {
			field.add(adapter);
		}else if(adapter.getType().equals(Method.class)){
			method.add(adapter);
		}
	}

	private void setAdapterList(AccessibleObject[] abs, ArrayList<MapperAdapter<?, AccessibleObject>> adapterList){
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
	private void setClassAdapter(ArrayList<MapperAdapter<?, Class<?>>> classList){
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

	public void clear(){
		viewHash.clear();
		fieldObList.clear();
	}

	public void clearAdapter(){
		classObList.clear();
		fieldObList.clear();
		methodObList.clear();
	}

	public void injectAll(){
		setClassAdapter(classObList);

		if(methods==null) {
			methods = targetClass.getDeclaredMethods();
		}
		setAdapterList(methods, methodObList);

		if(fields==null) {
			fields = targetClass.getDeclaredFields();
		}
		setAdapterList(fields, fieldObList);
	}

	public void inject(MapperAdapter... adapters){
		ArrayList<MapperAdapter<?, Class<?>>> clzList = new ArrayList<>();
		ArrayList<MapperAdapter<?, AccessibleObject>> fieldList = new ArrayList<>();
		ArrayList<MapperAdapter<?, AccessibleObject>> methodList = new ArrayList<>();
		for(MapperAdapter adapter : adapters){
			adapter.setMapper(this);
			addAdapter(adapter, clzList, fieldList, methodList);
		}

		setClassAdapter(clzList);
		if(methods==null) {
			methods = targetClass.getDeclaredMethods();
		}
		setAdapterList(methods, methodList);
		if(fields==null) {
			fields = targetClass.getDeclaredFields();
		}
		setAdapterList(fields, fieldList);
	}

	public void addAdapter(MapperAdapter adapter){
		addAdapter(adapter, classObList, fieldObList, methodObList);
	}

//	public String[] injectBox(){
//		if(targetClass.isAnnotationPresent(Box.class)){
//			Box par = targetClass.getAnnotation(Box.class);
//			return par.value();
//		}else{
//			throw new UinMapperException(ErrorMessage.Runtime.box(targetClass),null);
//		}
//	}

//	public int injectLayout(){
//		if(hasLayout()){
//			Layout lytId = targetClass.getAnnotation(Layout.class);
//			return lytId.value();
//		}else{
//			throw new UinMapperException(ErrorMessage.Runtime.injectLayout(targetClass),null);
//		}
//	}
//
//	public boolean hasLayout(){
//		return targetClass.isAnnotationPresent(Layout.class);
//	}

}
