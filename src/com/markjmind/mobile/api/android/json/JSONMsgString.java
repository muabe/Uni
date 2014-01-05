package com.markjmind.mobile.api.android.json;

import org.json.JSONObject;

/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public class JSONMsgString extends JwJSONReader{
	private StringBuffer str =new StringBuffer();
	public JSONMsgString(){
		setElementSort(true);
	}
	
	@Override
	public void work(JSONObject json, String name, String arrayName, Object parentResult) {
		int dep=getParentDep(parentResult);
		str.append(getDep(dep)+"-"+name+":"+json.optString(name, null)+"\n");
	}

	@Override
	public Object startArrayWork(String name, Object parentResult) {
		int dep=getParentDep(parentResult);
		str.append(getDep(dep)+"<"+name+">\n");
		return dep+1;
	}
	@Override
	public void endArrayWork(String name, Object parentResult) {
		int dep=getParentDep(parentResult);
		str.append(getDep(dep)+"</"+name+">\n");
	}
	
	private int getParentDep(Object parentResult){
		int dep =0;
		if(parentResult!=null){
			dep=(Integer)parentResult;
		}
		return dep;
	}
	
	private String getDep(int dep){
		String p="";
		for(int i=0;i<dep;i++){
			p +="   ";
		}
		return p;
	}
	
	public String toString(){
		return str.toString();
	}
	public void reset(){
		str.setLength(0);
	}
}
