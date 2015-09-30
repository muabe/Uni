package com.markjmind.uni.ui;


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
