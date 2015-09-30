package com.markjmind.uni.hub;

import java.util.ArrayList;


public class JDhub {
	public String sendString(Store store){
		String[] keys = store.getKeys();
		String msg = "";
		for(int j=0;j<keys.length;j++){
			String value = (store.getString(keys[j]).replaceAll(";", "；")).replaceAll("\n", "　");
			msg = msg+keys[j]+"="+value;
			msg=msg+";";
		}
		return msg;
	}
	
	public String sendString(StoreList list){
		String msg = "";
		for(int i=0;i<list.size();i++){
			Store store = list.getStore(i);
			msg=msg+sendString(store)+"\n";
		}
		return msg;
	}
	
	
	public Store receiveStore(String msg){		
		Store store = new Store();
		if(msg.indexOf(";")>0){
			String[] split = msg.split(";");
			for(int i=0;i<split.length;i++){
				int start  = split[i].indexOf("=");
				String key = split[i].substring(0,start);
				String value = split[i].substring(start+1,split[i].length());
				value = (value.replaceAll("；",";")).replaceAll("　", "\n");
				store.add(key, value);
			}
		}else{
			return null;
		}
		if(store.size()==0){
			return null;
		}
		return store;
	}
	
	public StoreList receiveStoreList(String msg){		
		StoreList list = new StoreList();
		if(msg.length()>0){
			String[] split = msg.split("\n");
			for(int i=0;i<split.length;i++){
				Store store = receiveStore(split[i]);
				if(store!=null){
					list.add(store);
				}
			}
		}else{
			return null;
		}
		if(list.size()==0){
			return null;
		}
		return list;
	}
	
	//하나의 키에 다수의 값을 가지는 데이터를 가지고온다
	public String[] getMultyData(Store param,String key){
		String[] datas = param.getString(key).split(",");
		for(int i=0;i<datas.length;i++){
			datas[i]=datas[i].replaceAll("，",",");
		}
		return datas;
	}
	
	//하나의 키에 다수의 값을 가지는 데이터를 셋팅한다
	public String setMultyData(String[] values){
		String msg ="";
		for(int i=0;i<values.length;i++){
			if(i==0){
				msg = msg+values[i].replaceAll(",", "，");
			}else{
				msg = msg+","+values[i].replaceAll(",", "，");
			}
		}
		return msg;
	}
	
	public String setMultyData(StoreList values,String key){
		ArrayList list = new ArrayList();
		for(int i=0;i<values.size();i++){
			list.add(values.getString(key, i));
		}
		if(list.size()<=0){
			return "";
		}
		String[] result = new String[list.size()];
		for(int i=0;i<list.size();i++){
			result[i] = (String)list.get(i);
		}
		return setMultyData(result);
	}
	
	
}
