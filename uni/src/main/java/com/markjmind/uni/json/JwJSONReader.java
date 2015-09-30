package com.markjmind.uni.json;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public abstract class JwJSONReader<ParentResult> {
	boolean isElementSort = true;
	public static enum JSONType{
		Object,
		JSONObject,
		JSONArray
	}
	public void setElementSort(boolean isElementSort){
		this.isElementSort = isElementSort;
	}
	public void read(JSONObject json) throws JSONException{
		read(json,null,null);
	}
	
	public void read(JSONObject json, String rootName) throws JSONException{
		read(json,null,rootName);
	}
	
	private void read(JSONObject json, ParentResult parentResult, String parentName) throws JSONException{
		Iterator iter = json.keys();
		ArrayList<ElementArraySort> jsonArrayList = new ArrayList<ElementArraySort>();
		ArrayList<ElementObjectSort> jsonObjectList = new ArrayList<ElementObjectSort>();
		JSONArray jsonArray;
		String name;
		while(iter.hasNext()){
			name = (String)iter.next();
			JSONType type = getJSONType(json, name);
			switch (type) {
			case Object:
				work(json, name, parentName, parentResult);
				break;
			case JSONObject:
				if(isElementSort){
					jsonObjectList.add(new ElementObjectSort(name, json.optJSONObject(name)));
				}else{
					recallWork(json.optJSONObject(name), name,parentResult,startArrayWork(name,parentResult));
				}
				
				break;
			case JSONArray:
				if(isElementSort){
					jsonArrayList.add(new ElementArraySort(name,getJSONArray(json, name)));
				}else{
					jsonArray =getJSONArray(json, name);
					for(int i=0;i<jsonArray.length();i++){
						recallWork(jsonArray.getJSONObject(i), name, parentResult, startArrayWork(name, parentResult));
					}
				}
				break;
			}
		}
		if(isElementSort){
			ElementObjectSort eos;
			for(int i=0;i<jsonObjectList.size();i++){
				eos =  jsonObjectList.get(i);
				recallWork(eos.json, eos.name, parentResult,startArrayWork(eos.name,parentResult));
			}
			ElementArraySort es;
			for(int j=0;j<jsonArrayList.size();j++){
				es =  jsonArrayList.get(j);
				for(int i=0;i<es.jsonArray.length();i++){
					recallWork(es.jsonArray.getJSONObject(i), es.name, parentResult, startArrayWork(es.name, parentResult));
				}
			}
		}
	}
	
	private class ElementObjectSort{
		public String name;
		public JSONObject json;
		public ElementObjectSort(String name, JSONObject json){
			this.name = name;
			this.json =json;
		}
	}
	private class ElementArraySort{
		public String name;
		public JSONArray jsonArray;
		public ElementArraySort(String name, JSONArray jsonArray){
			this.name = name;
			this.jsonArray =jsonArray;
		}
	}
	private void recallWork(JSONObject sub_json, String name, ParentResult parentResult,ParentResult result) throws JSONException{
		if(sub_json!=null){
			read(sub_json, result,name);
		}
		endArrayWork(name,parentResult);
	}
	
	public abstract void work(JSONObject json, String name,String parentName, ParentResult parentResult);
	public abstract  ParentResult startArrayWork(String name, ParentResult parentResult);
	public abstract void endArrayWork(String name, ParentResult parentResult);
	
	public static JSONType getJSONType(JSONObject json, String name) throws JSONException{
		JSONType type=null;
		Object object = json.get(name);
		if(object instanceof JSONObject){
			type = type.JSONObject;
		}else if(object instanceof JSONArray){
			type = type.JSONArray;
		}else{
			type = type.Object;
		}
		return type;
	}
	
	public static JSONArray getJSONArray(JSONObject json, String name) throws JSONException{
		JSONArray array =null;
		if(!json.isNull(name)){
			JSONType type = getJSONType(json,name);
			switch (type) {
			case Object:
				array = new JSONArray();
				array.put(json.get(name));
				break;
			case JSONObject:
				JSONObject gateway = json.getJSONObject(name);
				array = new JSONArray();
				array.put(gateway);
				break;
			case JSONArray:
				array = json.getJSONArray(name);
			}
		}
		return array;
	}
}
