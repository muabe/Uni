package com.markjmind.libox;

import android.app.Activity;
import android.os.Bundle;

import com.markjmind.libox.login.LoginViewer;
import com.markjmind.uni.viewer.Viewer;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);

		Viewer.build(LoginViewer.class, this)
				.setAsync(true)
				.setPreLayout(true)
				.change(R.id.frame);


	}

}
