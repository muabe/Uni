package com.markjmind.uni.util;

import android.view.View;

public interface JwOnGroupSelect {
	public void selected(View v, String name, int index, Object param);
	public void deselected(View v, String name, int index, Object param);
}
