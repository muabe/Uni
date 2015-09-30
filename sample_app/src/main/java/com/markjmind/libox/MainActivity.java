package com.markjmind.libox;

import android.app.Activity;
import android.os.Bundle;

import com.markjmind.libox.login.LoginViewer;
import com.markjmind.uni.Viewer;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);

		Viewer.build(LoginViewer.class, this)
				.setAsync(false)
				.change(R.id.frame);


	}

}
