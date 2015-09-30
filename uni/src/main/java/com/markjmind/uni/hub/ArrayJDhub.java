package com.markjmind.uni.hub;

import java.util.ArrayList;

/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
//MultyDataHub 
public class ArrayJDhub {
	private StoreList uriList = new StoreList();
	private Store rvList = new Store();
	public final static String type = "mdType";
	ArrayList uriMdType = new ArrayList();
	ArrayList rvMdType = new ArrayList();
	
//	//하나의 키에 다수의 값을 가지는 데이터를 가지고온다
//	public String[] getMultyData(Store param,String key){
//		String[] datas = param.getString(key).split(",");
//		for(int i=0;i<datas.length;i++){
//			datas[i]=datas[i].replaceAll("，",",");
//		}
//		return datas;
//	}
//	
//	//하나의 키에 다수의 값을 가지는 데이터를 셋팅한다
//	public String setMultyData(String[] values){
//		String msg ="";
//		for(int i=0;i<values.length;i++){
//			if(i==0){
//				msg = msg+values[i].replaceAll(",", "，");
//			}else{
//				msg = msg+","+values[i].replaceAll(",", "，");
//			}
//		}
//		return msg;
//	}
	
	//GBN으로 묶인 데이터들은 정리해서 가져온다. 
	public StoreList getUriMDStoreList(Store param){
		String[] types = new JDhub().getMultyData(param,ArrayJDhub.type);
		Store[] temps = new Store[types.length];
	
		StoreList dataList = new StoreList();
		String[] keys = param.getKeys();
		for(int i=0;i<temps.length;i++){
			temps[i] = new Store();
			temps[i].add(ArrayJDhub.type,types[i]);
			dataList.add(temps[i]);

		}
		
		for(int j=0;j<keys.length;j++){
			String[] result = getUriTypeData(keys[j]);
			if(result==null){
				continue;
			}
			String type = result[0];
			String dataKey = result[1];
			for(int i=0;i<types.length;i++)
			{
				if(types[i].equals(type)){
					temps[i].add(dataKey, param.getString(keys[j]));
					break;
				}
				
			}
		}
		return dataList;
	}
	
	
	public Store getUriMDStore(Store param){
		String[] types = new JDhub().getMultyData(param,ArrayJDhub.type);
		Store[] temps = new Store[types.length];
	
		Store dataList = new Store();
		String[] keys = param.getKeys();
		for(int i=0;i<temps.length;i++){
			temps[i] = new Store();
			dataList.add(types[i], temps[i]);
		}
		
		for(int j=0;j<keys.length;j++){
			String[] result = getUriTypeData(keys[j]);
			if(result==null){
				continue;
			}
			String type = result[0];
			String dataKey = result[1];
			
			for(int i=0;i<types.length;i++)
			{
				if(types[i].equals(type)){
					temps[i].add(dataKey, param.getString(keys[j]));
					break;
				}
				
			}
		}
		return dataList;
	}
	
	private boolean isSameType(String[] types, String type){
		for(int i=0;i<types.length;i++){
			if(types[i].equals(type)){
				return true;
			}
		}
		return false;
	}
	//gbn들을 가지고온다.
	public String[] getUriMDKeys(Store param){
		String[] gbns = new JDhub().getMultyData(param, ArrayJDhub.type);
		return gbns;
	}
	public String[] getUriTypeData(String data){
		String values[] = new String[2];
		int start = data.indexOf("_");
		if(start<0){
			return null;
		}
		values[0] = data.substring(0,start);
		values[1] = data.substring(start+1,data.length()); 
		return values;
	}
	
	public void addUri(String MDhubType_name,Store param){
		String[] keys = param.getKeys();
		Store temp = new Store();
		uriMdType.add(MDhubType_name);
		for(int i=0;i<keys.length;i++){
			String gbnKeys = MDhubType_name+"_"+keys[i];
			temp.add(gbnKeys, param.get(keys[i]));
		}
		uriList.add(temp);
	}
	public String getUriString(){
		
		String[] types = new String[uriMdType.size()];
		for(int i=0;i<types.length;i++){
			types[i] = (String)uriMdType.get(i);
		}
		Store store = new Store();
		JDhub jd = new JDhub();
		store.add(ArrayJDhub.type,jd.setMultyData(types));
		uriList.add(0,store);
		String parameters = "";
		for(int i=0;i<this.uriList.size();i++){
			parameters = parameters+this.sendString(uriList.getStore(i));
			if(i+1!=this.uriList.size()){
				parameters = parameters+"&";
			}
		}
		return parameters;
	}
	private String sendString(Store store){
		String[] keys = store.getKeys();
		String msg = "";
		for(int j=0;j<keys.length;j++){
			msg = msg+keys[j]+"="+store.getString(keys[j]);
			if(j+1!=keys.length){
				msg=msg+"&";
			}
		}
		return msg;
	}
	
	//받을 데이터를 입력
	public void addRv(String MDhubType_name,Store store){
		StoreList temp = new StoreList();
		temp.add(store);
		rvList.add(MDhubType_name, temp);
	}
	public void addRv(String MDhubType_name,StoreList sl){
		rvList.add(MDhubType_name, sl);
	}
	
	public String getRvString(){
		String msg = "";
		JDhub jd = new JDhub();
		StoreList tempList = new StoreList();
		String[] rvKeys =  rvList.getKeys();
		if(rvList.size()>0){
			Store mdKeys = new Store();
			mdKeys.add("mdKeys",jd.setMultyData(rvKeys));
			tempList.add(mdKeys);
		}else{
			return "";
		}
		for(int i=0;i<rvKeys.length;i++){
			StoreList sl = (StoreList)rvList.get(rvKeys[i]);
			for(int j=0;j<sl.size();j++){
				Store store = new Store();
				store.add(ArrayJDhub.type,rvKeys[i]);
				store.putAll(sl.getStore(j));
				tempList.add(store);
				
			}
		}
		
		msg = jd.sendString(tempList);
		return msg;
	}
	
	public Store getRvStore(String msg){
		JDhub jd = new JDhub();
		StoreList result = jd.receiveStoreList(msg);
		if(result==null || result.size()<=0){
			return null;
		}
		String[] mdKeys = jd.getMultyData(result.getStore(0), "mdKeys");
		StoreList[] slList = new StoreList[mdKeys.length];
		for(int i=0;i<slList.length;i++){
			slList[i]= new StoreList();
		}
		for(int i=1;i<result.size();i++){
			Store temp = result.getStore(i);
			for(int j=0;j<mdKeys.length;j++){
				if(mdKeys[j].equals(temp.getString(ArrayJDhub.type))){
					temp.remove(ArrayJDhub.type);
					slList[j].add(temp);
					break;
				}
			}
		}
		Store returnValue = new Store();
		for(int i=0;i<mdKeys.length;i++){
			returnValue.add(mdKeys[i], slList[i]);
		}
		return returnValue;
	}
	
		
}
