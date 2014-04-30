package com.markjmind.mobile.api.android.controller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.markjmind.mobile.api.hub.Store;

/**
 * 작업중
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @date 2014. 4. 30.
 */
public class JwAsyncTask extends AsyncTask<Object, Integer, Object>{
	private final static Store<JwAsyncTask> asyncTaskPool = new Store<JwAsyncTask>();
	private String taskKey;
	protected boolean isStop = false;
	
	
	public JwAsyncTask(String taskKey){
		this.taskKey = taskKey;
		this.isStop = false;
	}
	
	public String getTaskKey(){
		return taskKey;
	}
	
	public void stop(){
		isStop = true;
		removeTask();
		Log.e("JwViewer","Stop AsyncTask : "+taskKey);
	}
	
	@Override
	protected void onPreExecute() {
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
	}
	@Override
	protected Object doInBackground(Object... params) {
		return true; 
	}
	@Override
	protected void onPostExecute(Object result) {
		if(isStop){
			return;
		}
		removeTask();
	}
	
	private void removeTask(){
		if(taskKey!=null){
			asyncTaskPool.remove(taskKey);
		}
	}
	
	public static void cancelTask(String taskKey){
		JwAsyncTask ref = asyncTaskPool.get(taskKey);
		if(ref!=null){
			ref.stop();
		}
	}
	
	@SuppressLint("NewApi") 
	public void excute(){
		cancelTask(taskKey);
		JwAsyncTask ref = new JwAsyncTask(taskKey);
		asyncTaskPool.add(taskKey, ref);
		
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
			ref.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			ref.execute();
		}
	}
}
