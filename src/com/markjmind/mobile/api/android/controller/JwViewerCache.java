package com.markjmind.mobile.api.android.controller;

import android.view.View;

import com.markjmind.mobile.api.hub.Store;

class JwViewerCache {
	private static final Store<JwViewer> cache = new Store<JwViewer>();
	
	public static void put(JwViewer jv){
		cache.add(getChasheKey(jv.getLayoutId(), jv.getParent()), jv);
	}
	public static JwViewer get(int layoutId, View parents){
		JwViewer jv = cache.get(getChasheKey(layoutId, parents));
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
