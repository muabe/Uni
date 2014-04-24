package com.markjmind.mobile.api.android.ui;

import com.markjmind.mobile.api.hub.Store;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public interface JwBackHistory {
	public Store param = new Store();
	public void display(String historyKey);
}
