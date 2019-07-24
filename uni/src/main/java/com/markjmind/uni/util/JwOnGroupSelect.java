package com.markjmind.uni.util;

import android.view.View;

public interface JwOnGroupSelect {
	void selected(View v, String name, int index, Object param);
	void deselected(View v, String name, int index, Object param);
}
