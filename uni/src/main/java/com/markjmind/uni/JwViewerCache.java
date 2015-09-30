package com.markjmind.uni;

import android.view.View;

import com.markjmind.uni.hub.Store;

class JwViewerCache {
	private static final Store<Viewer> cache = new Store<Viewer>();
	
	public static void put(Viewer jv){
		cache.add(getChasheKey(jv.getLayoutId(), jv.getParent()), jv);
	}
	public static Viewer get(int layoutId, View parents){
		Viewer jv = cache.get(getChasheKey(layoutId, parents));
		return jv;
	}
	private static String getChasheKey(int layoutId, View parents){
		return layoutId+"_"+parents.hashCode();
	}
	public static void clearCache(){
		cache.clear();
	}
	
	public static boolean contain(int layoutId, View parents){
		return cache.containsKey(getChasheKey(layoutId,parents));
	}
	
	public static void clear(){
		cache.clear();
	}
	
	public static void clear(int layoutId, View parents){
		cache.remove(getChasheKey(layoutId,parents));
	}
	
}
