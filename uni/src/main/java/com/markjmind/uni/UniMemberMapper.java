package com.markjmind.uni;

import android.view.View;

import com.markjmind.uni.annotiation.Box;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.annotiation.OnClick;

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
			throw new UinMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @params을 지정하는 annotation의 value가 잘못되었습니다..",null);
		}
	}
	
	public static int injectionLayout(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Layout.class)){
			Layout lytId = viewerClass.getAnnotation(Layout.class);
			return lytId.value();
		}else{
			throw new UinMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @layout을 지정하는 annotation의 value가 잘못되었습니다.",null);
		}
		
	}
	
	public static void injection(Viewer obj){
		/** method **/
		HashMap<Integer, View> viewHash = new HashMap<>();
		Method[] methods = obj.getClass().getDeclaredMethods();
		for(Method method:methods){
			if(method.isAnnotationPresent(OnClick.class)){ // onClick의 경우
				OnClick oc = method.getAnnotation(OnClick.class);
				int[] list = oc.ids();
				if(list.length==0){
					int id = oc.value();
					if(id==-1) {
						id = JwStringID.getID(method.getName(), obj.getContext());
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
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field:fields){
			if(field.isAnnotationPresent(GetView.class)){ // GetView의 경우
				GetView ab = field.getAnnotation(GetView.class);
				int id = ab.value();
				if(id==-1){
					id = JwStringID.getID(field.getName(), obj.getContext());
				}
				setField(obj, id, field, viewHash);
			}
		}
	}

	private static View setMethod(Viewer obj, Integer id, Method method){
		View view = obj.findViewById(id);
		if(view==null){ // view가 없을경우
			throw new UinMapperException("\n["+obj.getClass().getName()+""+", method에 해당하는 ID(Null)가 잘못 지정되었습니다.",null);
		}
		obj.setOnClickListener(view, method);
		return view;
	}

	private static View setField(Viewer obj, int id, Field field, HashMap<Integer, View> viewHash){
		View v;
		if(viewHash.containsKey(id)){
			v = viewHash.get(id);
		}else{
			v = obj.findViewById(id);
		}

		if(v==null){
			throw new UinMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", Filed에 해당하는 ID(Null)가 잘못 지정되었습니다.",null);
		}
		try {
			field.setAccessible(true);
			field.set(obj, v);
		} catch (IllegalArgumentException e) {
			throw new UinMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 잘못된 Field에 해당하는 ID를 찾을수 없습니다.",e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 접근권한이 없는 필드입니다.",e);
		}
		return v;
	}
	
	
	

}
