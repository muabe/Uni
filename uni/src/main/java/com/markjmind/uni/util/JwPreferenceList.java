package com.markjmind.uni.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.markjmind.uni.hub.JDhub;
import com.markjmind.uni.hub.Store;
import com.markjmind.uni.hub.StoreList;

public class JwPreferenceList {
	Context context;
	String preference;
	SharedPreferences sp;
	JDhub jd = new JDhub();
	
	public JwPreferenceList(Context context,String preference_name){
		this.context = context;
		this.preference = preference_name;
		sp = context.getSharedPreferences(preference, context.MODE_WORLD_READABLE|context.MODE_WORLD_WRITEABLE);
		
	}
	

	
	public synchronized void setStoreList(StoreList storelist){
		Editor editor = sp.edit();
		String msg = jd.sendString(storelist);
		editor.putString(preference, msg);
		editor.commit();
	}
	
	public StoreList getStoreList(){
		String msg=sp.getString(preference, "");
		if(msg==null){
			Editor editor = sp.edit();
			editor.putString(preference, "");
			editor.commit();
			return new StoreList();
		}
		StoreList result = jd.receiveStoreList(msg);
		return result;
	}
	
	
	public synchronized void removeAll(){
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	
	public synchronized void remove(int index){
		StoreList storelist = this.getStoreList();
		storelist.remove(index);
		this.setStoreList(storelist);		
	}
	
	public synchronized void remove(String key,int index){
		StoreList storelist = this.getStoreList();
		Store store = storelist.getStore(index);
		store.remove(key);
		this.setStoreList(storelist);		
	}
	
	public synchronized void addValue(String key, String value,int index){
		StoreList storelist = this.getStoreList();
		Store store = storelist.getStore(index);
		store.add(key, value);
		this.setStoreList(storelist);	
	}
	
	public synchronized void addStore(Store store){
		StoreList storelist = this.getStoreList();
		storelist.add(store);
		this.setStoreList(storelist);	
	}
	
	public synchronized void addStore(Store store,int index){
		StoreList storelist = this.getStoreList();
		storelist.add(index, store);
		this.setStoreList(storelist);	
	}
	

	public String getValue(String key,int index){
		StoreList storelist = this.getStoreList();
		Store store = storelist.getStore(index);
		return store.getString(key);
	}
	
	public Store getStore(int index){
		StoreList storelist = this.getStoreList();
		Store store = storelist.getStore(index);
		return store;
	}
	
}
