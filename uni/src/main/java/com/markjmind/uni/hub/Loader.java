package com.markjmind.uni.hub;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;



import android.util.Log;

import com.markjmind.uni.util.WebConnection;

public class Loader {
	
	 ArrayJDhub ajd = new ArrayJDhub();
	 String defaultUrl;
	 public Loader(String defaultUrl){
		 this.defaultUrl = defaultUrl;
	 }
	 public Store dataSend(){
		 String url = defaultUrl;
		 String mdParameters = ajd.getUriString();
		 url = url+mdParameters;
		 Log.d("개발자:dataSend",url);
		 WebConnection wc = new WebConnection();
		 Store wc_list=null;
		 try {
			wc_list = wc.getArrayJDList(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wc_list;
	 }
	 
	 public Store dataSend(boolean isCookie){
		 String url = defaultUrl;
		 String mdParameters = ajd.getUriString();
		 Log.d("개발자:dataSend",url);
		 WebConnection wc = new WebConnection();
		 if(isCookie==true){
//			 wc.cookie = Global.cookie;
			 wc.setCookie = true;
		 }
		 Store wc_list=null;
		 try {
			wc_list = wc.getArrayJDList(url,"POST",mdParameters);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wc_list;
	 }
	 

	 
	 public void add(String mdType,Store param){
		 ajd.addUri(mdType, param);
	 }
	 

	 
	 
		     
}
