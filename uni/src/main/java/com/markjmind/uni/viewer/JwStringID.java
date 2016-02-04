package com.markjmind.uni.viewer;

import android.content.Context;

import com.markjmind.uni.exception.UinMapperException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * start : 2012.08.21<br>
 * id를 id명의 일부 네이밍 규칙으로 그룹화한다.<br>
 *
 * <br>捲土重來<br>
 * @author 오재웅
 * @version 2013.11.17
 *
 */
public class JwStringID {
	private ArrayList<String> ids = new ArrayList<String>();
	private Hashtable<String, Integer> idMap = new Hashtable<String, Integer>();
	private boolean isInit=false;
	
	public void init(Context app){
		Class cls = getRClass("id", app);
		Object obj;
		try {
			obj = cls.newInstance();
			
			Field[] field = cls.getDeclaredFields();
			for(int i=0; i<field.length;i++){
				String fieldName = field[i].getName();
				int value = field[i].getInt(obj);
				idMap.put(fieldName, value);
				ids.add(fieldName);
			}
			isInit = true;
		} catch (IllegalAccessException e) {
			throw new UinMapperException("해당 ID를 가져올수 없습니다.", e);
		} catch (InstantiationException e) {
			throw new UinMapperException("R.layout의 인스턴스를 생성할수 없습니다.", e);
		}
	}
	
	
	//키에대한 아이디값을 리턴한다.
	public int get(String key){
		int id=idMap.get(key);
		return id;
	}
	
	//key 이름을 포함하는 아이디를 모두 찾아 그룹을 리턴한다.
	public ArrayList<String> containKeys(String key){
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i=0;i<ids.size();i++){
			if((ids.get(i)).indexOf(key)>=0){
				result.add(ids.get(i));
			}
		}
		if(result.size()==0)
			return null;
		return result;
	}
	
	//key 이름으로 시작하는 아이디를 모두 찾아 그룹을 리턴한다.
	public ArrayList<String> startContainKeys(String key){
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i=0;i<ids.size();i++){
			if(((String)ids.get(i)).indexOf(key)==0){
				result.add(ids.get(i));
			}
		}
		if(result.size()==0)
			return null;
		return result;
	}
	
	public static int getLayoutID(String idName, Context app) throws UinMapperException {
		Class cls = getRClass("layout",app);
		Field field;
		try {
			field = cls.getDeclaredField(idName);
			int value = field.getInt(null);
			return value;
		} catch (SecurityException e) {
			throw new UinMapperException("[R.layout."+idName+"] SecurityException",e);
		} catch (NoSuchFieldException e) {
			throw new UinMapperException("[R.layout."+idName+"] 필드가 존재하지 않습니다.",e);
		} catch (IllegalArgumentException e) {
			throw new UinMapperException("[R.layout."+idName+"] IllegalArgumentException",e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException("[R.layout."+idName+"] 접근권한이 없는 필드입니다.",e);
		}
	}
	
	public static int getID(String idName, Context app) throws UinMapperException {
		Class cls = getRClass("id",app);
		Field field;
		try {
			field = cls.getDeclaredField(idName);
			int value = field.getInt(null);
			return value;
		} catch (SecurityException e) {
			throw new UinMapperException("[R.id."+idName+"] SecurityException",e);
		} catch (NoSuchFieldException e) {
			throw new UinMapperException("[R.id."+idName+"] 필드가 존재하지 않습니다.",e);
		} catch (IllegalArgumentException e) {
			throw new UinMapperException("[R.id."+idName+"] 잘못된 Field가 지정되었습니다.",e);
		} catch (IllegalAccessException e) {
			throw new UinMapperException("[R.id."+idName+"] 접근권한이 없는 필드입니다.",e);
		}
	}

	public static Class getRClass(String innerClassName, Context app) throws UinMapperException {
		String fullName = app.getPackageName()+".R$"+innerClassName;
		try {
			return Class.forName(fullName);
		} catch (ClassNotFoundException e) {
			throw new UinMapperException("-"+ fullName+"- [R."+innerClassName+"] 클래스가 존재하지 않습니다.",e);
		}
	}
}
