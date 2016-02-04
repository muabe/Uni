package com.markjmind.uni.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.markjmind.uni.hub.JDhub;
import com.markjmind.uni.hub.Store;
import com.markjmind.uni.hub.StoreList;

public class JwPreference {
	Context context;
	String preference;
	SharedPreferences sp;
	JDhub jd = new JDhub();
	Editor editor;
	
	public JwPreference(Context context,String preference_name){
		this.context = context;
		this.preference = preference_name;
		sp = context.getSharedPreferences(preference, context.MODE_WORLD_READABLE|context.MODE_WORLD_WRITEABLE);
		editor = sp.edit();
		
	}
	
	public synchronized void setStore(String key, Store store){
		
		String msg = jd.sendString(store);
		editor.putString(key, msg);
		editor.commit();
	}
	
	public Store getStore(String key){
		String msg=sp.getString(key, "");
		if(msg==null){
			return null;
		}
		Store result = jd.receiveStore(msg);
		return result;
	}		
	
	public int size(){
		Map map = sp.getAll();
		return map.size();
	}
	
	public String[] getKeys(){
		Map map = sp.getAll();
		if(map.size()==0){
			return null;
		}
		Set set = map.keySet();
		Iterator iter = set.iterator();
		String[] keys = new String[set.size()];
		int index = 0;
		while(iter.hasNext()){
			keys[index]=(String)iter.next();
			index++;
		}
		return keys;
	}
	
	public StoreList getStoreList(){
		StoreList list = new StoreList();
		String[] keys = this.getKeys();
		for(int i=0;i<keys.length;i++){
			list.add(this.getStore(keys[i]));
		}
		return list;
	}
	
	// 정렬된 storelist를 가져온다.
	public StoreList getStoreList(String sortKey){
		StoreList sl = this.getStoreList();
		sl.sort(sortKey);
		return sl;
	}
	
	// 정렬된 storelist를 가져온다.
		public StoreList getStoreList(String sortKey,boolean ASC){
			StoreList sl = this.getStoreList();
			sl.sort(sortKey, ASC);
			return sl;
		}
		
	public StoreList search(String storeKey, String value){
//		ArrayList result = new ArrayList();
//		String[] keys = this.getIds();
//		for(int i=0;i<keys.length;i++){
//			Store temp = this.getStore(keys[i]);
//			if(temp.containsKey(storeKey)){
//				if(value.equals(temp.getString(storeKey))){
//					result.add(keys[i]);
//				}
//			}
//		}
//		if(result.size()==0){
//			return null;
//		}else{
//			return result;
//		}
		StoreList list = getStoreList();
		return list.search(storeKey, value);
	}

	public void updateAll(String storeKey, String value){
		String[] keys = this.getKeys();
		for(int i=0;i<keys.length;i++){
			Store temp = this.getStore(keys[i]);
			if(temp.containsKey(storeKey)){
				temp.add(storeKey, value);
			}
		}
	}
	
	public void update(String key, String storeKey, String value){
		Store temp = this.getStore(key);
		if(temp.containsKey(storeKey)){
			temp.add(storeKey, value);
		}
	}
	
	public void remove(String key){
		editor.remove(key);
		editor.commit();
	}
	
	public void remove(String key,String storeKey){
		this.getStore(key).remove(key);
	}

}
