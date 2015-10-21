package com.markjmind.uni;

import android.view.View;

import com.markjmind.uni.annotiation.Box;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.annotiation.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */

public class JwMemberMapper {
	
	public static String[] injectionBox(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Box.class)){
			Box par = viewerClass.getAnnotation(Box.class);
			return par.value();
		}else{
			throw new JwMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @params을 지정하는 annotation의 value가 잘못되었습니다..",null);
		}
	}
	
	public static int injectionLayout(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Layout.class)){
			Layout lytId = viewerClass.getAnnotation(Layout.class);
			return lytId.value();
		}else{
			throw new JwMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @layout을 지정하는 annotation의 value가 잘못되었습니다.",null);
		}
		
	}
	
	public static void injectField(Viewer obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			if(fields[i].isAnnotationPresent(GetView.class)){
				GetView ab = fields[i].getAnnotation(GetView.class);
				int id = ab.value();
				if(id==-1){
					id = JwStringID.getID(fields[i].getName(), obj.getActivity().getApplication());
				}
				View view = setField(obj, id, fields[i]);
			}
		}
	}

	public static void injectionMethod(Viewer obj){
		Method[] methods = obj.getClass().getDeclaredMethods();
		for(int i=0;i<methods.length;i++){
			if(methods[i].isAnnotationPresent(OnClick.class)){
				OnClick oc = methods[i].getAnnotation(OnClick.class);
				int[] list = oc.ids();
				if(list.length==0){
					int id = oc.value();
					if(id==-1) {
						id = JwStringID.getID(methods[i].getName(), obj.getActivity().getApplication());
					}
					if(true) { //TODO 이미 field에서 injection된 객체가 없다면
						obj.setOnClickListener(obj.getView(id), methods[i]);
					}
				}else{
					for(int k=0;k<list.length;k++){
						int id = list[k];
						if(true) { //TODO 이미 field에서 injection된 객체가 없다면
							obj.setOnClickListener(obj.getView(id), methods[i]);
						}
					}
				}
			}
		}
	}
	
	private static View setField(Viewer obj, int id, Field field){
		View v = obj.getView(id);
		if(v==null){
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", Filed에 해당하는 ID(Null)가 잘못 지정되었습니다.",null);
		}
		try {
			field.setAccessible(true);
			field.set(obj, v);
		} catch (IllegalArgumentException e) {
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 잘못된 Field에 해당하는 ID를 찾을수 없습니다.",e);
		} catch (IllegalAccessException e) {
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 접근권한이 없는 필드입니다.",e);
		}
		return v;
	}
	
	
	

}
