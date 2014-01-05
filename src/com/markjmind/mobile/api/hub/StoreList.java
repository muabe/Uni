package com.markjmind.mobile.api.hub;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreList extends ArrayList{
	
	
    public Object get(String key, int index){
		Store store = (Store)super.get(index);
		return store.get(key);
    }
    public String getString(String key, int index){
    	Store store = (Store)super.get(index);
    	return store.getString(key);
    }
    
    public int getInt(String key, int index){
    	Store store = (Store)super.get(index);
    	return store.getInt(key);
    }
    
    public long getLong(String key, int index){
    	Store store = (Store)super.get(index);
    	return store.getLong(key);
    }
    
    public Store getStore(int index){
    	return (Store)this.get(index);
    }
    
    public Store[] toStoreArray(){	
	return (Store[])super.toArray();
    }
    
    public String[] getKeys(){
    	if(this.size()>0){
    		Store store =this.getStore(0);
    		return store.getKeys();
    	}else{
    		return null;
    	}
    }
    
    public void storeAdd(String key, Object value, int index){
    	Store store = this.getStore(index);
    	store.add(key, value);
    }
    
    public void storeRomve(String key, int index){
    	Store store = this.getStore(index);
    	store.remove(key);
    }
       
    public void sort(String sortKey){
    	for(int i=0;i<this.size();i++){
    		Store temp = this.getStore(i);
    		temp.setCompare(sortKey);
    	}
    	Collections.sort(this);
    }
    
    public void sort(String sortKey, boolean asc){
    	for(int i=0;i<this.size();i++){
    		Store temp = this.getStore(i);
    		temp.setCompare(sortKey,asc);
    	}
    	Collections.sort(this);
    }
    
	public StoreList search(String storeKey, String value){
		StoreList result = new StoreList();
		String[] keys = this.getKeys();
		for(int i=0;i<this.size();i++){
			Store temp = this.getStore(i);
			if(temp.containsKey(storeKey)){
				if(value.equals(temp.getString(storeKey))){
					result.add(keys[i]);
				}
			}
		}
		if(result.size()==0){
			return null;
		}else{
			return result;
		}
	}
	
	public StoreList searchToSort(String storeKey, String value,String sortKey){
		StoreList result = this.search(storeKey, value);
		if(result.size()==0){
			return null;
		}else{
			result.sort(sortKey);
			return result;
		}
	}
	
	public StoreList searchToSort(String storeKey, String value,String sortKey, boolean ASC){
		StoreList result = this.search(storeKey, value);
		if(result.size()==0){
			return null;
		}else{
			result.sort(sortKey,ASC);
			return result;
		}
	}
}
