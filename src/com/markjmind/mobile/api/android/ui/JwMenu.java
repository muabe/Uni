package com.markjmind.mobile.api.android.ui;


public class JwMenu extends JwGroup{
	
	public JwMenu(){
		super();
		setDefalutHistory(true);
	}
	
	public JwMenu(JwOnGroupSelect onGroupSelect){
		super(onGroupSelect);
		setDefalutHistory(true);
	}
}
