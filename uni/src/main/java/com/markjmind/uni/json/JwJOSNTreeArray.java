package com.markjmind.uni.json;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.markjmind.uni.hub.Store;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwJOSNTreeArray extends JwJSONReader<Store>{
	
	ArrayList<Store> data = new ArrayList<Store>();
	Store root = new Store();
	String rootName;
	String targetJsonName;
	public ArrayList<Store> getData(){
		return data;
	}
	public  JwJOSNTreeArray(String targetJsonName){
		this.targetJsonName = targetJsonName;
	}
	
	public  JwJOSNTreeArray(String rootName, String targetJsonName){
		this.rootName = rootName;
		this.targetJsonName = targetJsonName;
	}
	
	@Override
	public void work(JSONObject json, String name, String parentName, Store parentResult) {
		if(parentResult==null){
			root.add(name, json.optString(name));
			return;
		}
		Store temp = (Store)parentResult.get(parentName);
		if(temp==null)
			return;
		temp.add(name, json.optString(name));
	}

	@Override
	public Store startArrayWork(String name, Store parentResult) {
		if(parentResult==null){
			parentResult= new Store();
		}
		parentResult.add(name, new Store());
		return parentResult;
	}

	@Override
	public void endArrayWork(String name, Store parentResult) {
		if(targetJsonName.equals(name)){
			if(rootName!=null){
				parentResult.add(rootName, root).clone();
			}else{
				parentResult.putAll(root);
			}
			data.add((Store)parentResult.clone());
			parentResult.remove(name);
		}
	}
}
