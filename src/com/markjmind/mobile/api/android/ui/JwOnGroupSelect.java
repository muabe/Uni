package com.markjmind.mobile.api.android.ui;

import java.util.Hashtable;

import android.view.View;

public interface JwOnGroupSelect {
	public void selected(View v,String name,int index, Object param);
	public void deselected(View v,String name,int index, Object param);
}
