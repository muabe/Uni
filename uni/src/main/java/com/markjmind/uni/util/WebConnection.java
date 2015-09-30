package com.markjmind.uni.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.markjmind.uni.hub.ArrayJDhub;
import com.markjmind.uni.hub.JDhub;
import com.markjmind.uni.hub.Store;
import com.markjmind.uni.hub.StoreList;

public class WebConnection {
	HttpURLConnection urlConnection;
	public String cookie = null;
	public boolean setCookie = false;
	
	public String getHtml(String urlName) throws MalformedURLException, ProtocolException, IOException {
		return this.getHtml(urlName, "GET", null);
	}
	
	public String getHtml(String urlName,String method,String para) throws MalformedURLException, ProtocolException, IOException {
		InputStream in;
		if(para==null){
			para="";
		}
		if(method.equals("GET")){
			in = getInputStreamGet(urlName+para);
		}else if(method.equals("POST")){
			in = getInputStreamPost(urlName,para);
		}else{
			return null;
		}
		
		String tempMsg="";
		int bufSize = 1024;
		boolean overBuf = false;
		int totalCount=0;
		int readCount;
		int lastReadCount=0;
		ArrayList byteArray = new ArrayList();
		byte[] buf = new byte[bufSize];
		while((readCount=in.read(buf))!=-1){
        	totalCount = totalCount+readCount;
        	if(totalCount>=bufSize){
        		ByteEl be = new ByteEl(buf.clone(), readCount);
            	byteArray.add(be);
        	}else{
        		lastReadCount = readCount;
        	}
        }
		if(totalCount>=bufSize){
			overBuf = true;
		}
		if(overBuf){
			int bufCount = 0;
			byte[] buffer = new byte[totalCount];
			for(int i=0;i<byteArray.size();i++){
				ByteEl el = (ByteEl)byteArray.get(i);
				for(int j=0;j<el.readCount;j++){
					buffer[bufCount] = el.bytes[j];
					bufCount++;
				}
			}
			tempMsg = new String(buffer,"UTF-8");
		}else{
			tempMsg = new String(buf,0,lastReadCount,"UTF-8");
		}
		disconnect();
		in.close();
		return tempMsg;
	}
	private class ByteEl{
		public int readCount;
		public byte[] bytes;
		public ByteEl(byte[] bytes,int readCount){
			this.readCount =readCount;
			this.bytes = bytes;
		}
	}
	
	
	public void disconnect(){
		urlConnection.disconnect();
	}
	
	public InputStream getInputStreamGet(String urlName) throws MalformedURLException,IOException,ProtocolException{
		URL url = new URL(urlName) ;
		InputStream is=null;
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setConnectTimeout(1000 * 15);
		urlConnection.setReadTimeout(1000 * 15);
		if(cookie!=null){
			urlConnection.setRequestProperty("Cookie", cookie);
		}
		if(setCookie){
			setCookie();
		}
		is = urlConnection.getInputStream();
		return is;
	}
	
	public InputStream getInputStreamPost(String urlName, String para) throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(urlName);
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setAllowUserInteraction(true);
		urlConnection.setReadTimeout(1000 * 15);
		urlConnection.setConnectTimeout(1000 * 15);
		urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
		if(cookie!=null){
			urlConnection.setRequestProperty("Cookie", cookie);
		}
		OutputStream out = urlConnection.getOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
		pw.write(para);
		pw.flush();
		// System.out.println(urlName+"?"+para);
		if(setCookie){
			setCookie();
		}
		InputStream is = urlConnection.getInputStream();
		return is;
	}
	
	private void setCookie(){
		if(cookie==null){
			cookie="";
		}
		Map m = urlConnection.getHeaderFields();
		if (m.containsKey("Set-Cookie")) {
			Collection c = (Collection) m.get("Set-Cookie");
			for (Iterator i = c.iterator(); i.hasNext();) {
				cookie = cookie + (String) i.next() + ";";
			}
		}
//		Global.cookie = cookie;
	}
	
	public StoreList getJDList(String urlName) throws MalformedURLException, ProtocolException, IOException{
			String tempMsg = getHtml(urlName);
	        JDhub jd = new JDhub();
	        StoreList list = jd.receiveStoreList(tempMsg);
	        return list;
	}
	
	public StoreList getJDList(String urlName,String method, String param) throws MalformedURLException, ProtocolException, IOException{
		String tempMsg = getHtml(urlName,"POST", param);
        JDhub jd = new JDhub();
        StoreList list = jd.receiveStoreList(tempMsg);
        return list;
	}
	
	public Store getJD(String urlName){
		try{
	        String tempMsg = getHtml(urlName);
	        JDhub jd = new JDhub();
	        Store store = jd.receiveStore(tempMsg);
	        return store;
		}catch(Exception e){
			return null;
		}
	}
	
	public Store getArrayJDList(String urlName)throws MalformedURLException, ProtocolException, IOException{
		String tempMsg = getHtml(urlName);
		ArrayJDhub jd = new ArrayJDhub();
		return jd.getRvStore(tempMsg);
	}
	
	public Store getArrayJDList(String urlName,String method,String strParam)throws MalformedURLException, ProtocolException, IOException{
		String tempMsg = getHtml(urlName,method,strParam);
		ArrayJDhub jd = new ArrayJDhub();
		return jd.getRvStore(tempMsg);
	}
	
	public Drawable loadDrawable(String imgUrl) {
        try {
        		URL url = new URL(imgUrl) ;
        		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection() ;
        		urlConnection.setRequestMethod("GET") ;
        		Drawable d = Drawable.createFromStream(urlConnection.getInputStream(), "none");
        		return d;
        } catch (Exception e) {
            return null;
        }
    }
	public Drawable loadDrawable(String imgUrl, int REQUIRED_SIZE) {
        try {
        		URL url = new URL(imgUrl) ;
        		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection() ;
        		urlConnection.setRequestMethod("GET") ;
//        		Drawable d = Drawable.createFromStream(urlConnection.getInputStream(), "none");
//        		return d;
    			// decode image size
    			BitmapFactory.Options o = new BitmapFactory.Options();
    			o.inJustDecodeBounds = true;
    			Bitmap bmw = BitmapFactory.decodeStream(urlConnection.getInputStream());
    			urlConnection.getInputStream().close();
        		urlConnection.disconnect();
    			// Find the correct scale value. It should be the power of 2.

    			int width_tmp = o.outWidth, height_tmp = o.outHeight;
    			int scale = 1;
    			while (true) {
    				// if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
    				// break;
    				if (width_tmp / 2 < REQUIRED_SIZE)
    					break;
    				width_tmp /= 2;
    				height_tmp /= 2;
    				scale *= 2;
    			}

    			// decode with inSampleSize
    			BitmapFactory.Options o2 = new BitmapFactory.Options();
    			o2.inSampleSize = scale;
    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
    			bmw.compress(CompressFormat.PNG, 100, stream);
    			byte[] data = stream.toByteArray();
    			bmw.recycle();
    			bmw = null;
    			bmw = BitmapFactory.decodeByteArray(data, 0, data.length, o2);
//    			Drawable drawable = new BitmapDrawable(Global.context.getResources(),bmw);
//    			return drawable;
    			return null;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
	
	
	private DataOutputStream dataStream = null;
	String boundary = "*****mgd*****"; 
	String CRLF = "\r\n"; 
	String twoHyphens = "--"; 
	
	public String uploadPicture(File uploadFile,String postUrl,Store param,Store fileField) {
		if (uploadFile.exists())
			try {
				FileInputStream fileInputStream = new FileInputStream(uploadFile);
				URL connectURL = new URL(postUrl);
				urlConnection = (HttpURLConnection) connectURL.openConnection();
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				urlConnection.setUseCaches(false);
				urlConnection.setRequestMethod("POST");
				urlConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
				urlConnection.connect();
				dataStream = new DataOutputStream(urlConnection.getOutputStream());
				
				String[] keys = param.getKeys();
				for(int i=0;i<param.size();i++){
					writeFormField(keys[i],param.getString(keys[i]));
				}
				
				String[] fkeys = fileField.getKeys();
				for(int i=0;i<fileField.size();i++){
					writeFileField(fkeys[i], fileField.getString(fkeys[i]), "image/*", fileInputStream);
				}
				
				// final closing boundary line
				dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);
				fileInputStream.close();
				dataStream.flush();
				dataStream.close();
				dataStream = null;
				
				
				String response = getResponse(urlConnection);
				return response;
//				int responseCode = conn.getResponseCode();
//				if (response.conains("uploaded successfully")){
////					return ReturnCode.http201;
//					return null;
//				}
//				else{
//					// for now assume bad name/password
////					return ReturnCode.http401;
//					return null;
//				}
			} catch (MalformedURLException mue) {
				// Log.e(Tag, "error: " + mue.getMessage(), mue);
				System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: "+ mue.getMessage());
				// return ReturnCode.http400;
				return null;
			} catch (IOException ioe) {
				// Log.e(Tag, "error: " + ioe.getMessage(), ioe);
				System.out.println("GeoPictureUploader.uploadPicture: IOE: "
						+ ioe.getMessage());
				// return ReturnCode.http500;
				return null;
			} catch (Exception e) {
				// Log.e(Tag, "error: " + ioe.getMessage(), ioe);
				System.out
						.println("GeoPictureUploader.uploadPicture: unknown: "
								+ e.getMessage());
				// return ReturnCode.unknown;
				return null;
			}
		else {
			// return ReturnCode.noPicture;
			return null;

		}
	}
	
	public String uploadPicture(byte[] byteArray,String postUrl,Store param,Store fileField) {
			try {
				URL connectURL = new URL(postUrl);
				urlConnection = (HttpURLConnection) connectURL.openConnection();
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				urlConnection.setUseCaches(false);
				urlConnection.setRequestMethod("POST");
				urlConnection.setRequestProperty("User-Agent", "myGeodiary-V1");
				urlConnection.setRequestProperty("Connection", "Keep-Alive");
				urlConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
				urlConnection.connect();
				dataStream = new DataOutputStream(urlConnection.getOutputStream());
				String[] keys = param.getKeys();
				for(int i=0;i<param.size();i++){
					writeFormField(keys[i],param.getString(keys[i]));
				}
				
				String[] fkeys = fileField.getKeys();
				for(int i=0;i<fileField.size();i++){
					writeFileField(fkeys[i], fileField.getString(fkeys[i]), "image/jpg", byteArray);
				}
				
				// final closing boundary line
				dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);
				dataStream.flush();
				dataStream.close();
				dataStream = null;
				
				String response = getResponse(urlConnection);
				return response;
//				int responseCode = conn.getResponseCode();
//				if (response.conains("uploaded successfully")){
////					return ReturnCode.http201;
//					return null;
//				}
//				else{
//					// for now assume bad name/password
////					return ReturnCode.http401;
//					return null;
//				}
			} catch (MalformedURLException mue) {
				// Log.e(Tag, "error: " + mue.getMessage(), mue);
				System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: "+ mue.getMessage());
				// return ReturnCode.http400;
				return null;
			} catch (IOException ioe) {
				// Log.e(Tag, "error: " + ioe.getMessage(), ioe);
				System.out.println("GeoPictureUploader.uploadPicture: IOE: "
						+ ioe.getMessage());
				// return ReturnCode.http500;
				return null;
			} catch (Exception e) {
				// Log.e(Tag, "error: " + ioe.getMessage(), ioe);
				System.out
						.println("GeoPictureUploader.uploadPicture: unknown: "
								+ e.getMessage());
				// return ReturnCode.unknown;
				return null;
			}
	}
	
	private void writeFileField(String fieldName, String fieldValue,String type, FileInputStream fis) {
		try {
			// opening boundary line
			dataStream.writeBytes(twoHyphens + boundary + CRLF);
			dataStream.writeBytes("Content-Disposition: form-data; name=\""
					+ fieldName + "\";filename=\"" + fieldValue + "\"" + CRLF);
			dataStream.writeBytes("Content-Type: " + type + CRLF);
			dataStream.writeBytes(CRLF);
			// create a buffer of maximum size
			int bytesAvailable = fis.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
			// read file and write it into form...
			int bytesRead = fis.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dataStream.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}
			// closing CRLF
			dataStream.writeBytes(CRLF);
		} catch (Exception e) {
			System.out.println("GeoPictureUploader.writeFormField: got: "
					+ e.getMessage());
			// Log.e(TAG, "GeoPictureUploader.writeFormField: got: " +
			// e.getMessage());
		}
	}
	
	private void writeFileField(String fieldName, String fieldValue,String type, byte[] byteArray) {
		try {
			// opening boundary line
			dataStream.writeBytes(twoHyphens + boundary + CRLF);
			dataStream.writeBytes("Content-Disposition: form-data; name=\""
					+ fieldName + "\";filename=\"" + fieldValue + "\"" + CRLF);
			dataStream.writeBytes("Content-Type: " + type + CRLF);
			dataStream.writeBytes(CRLF);
			// create a buffer of maximum size
			dataStream.write(byteArray, 0, byteArray.length);
			// closing CRLF
			dataStream.writeBytes(CRLF);
		} catch (Exception e) {
			System.out.println("GeoPictureUploader.writeFormField: got: "
					+ e.getMessage());
			// Log.e(TAG, "GeoPictureUploader.writeFormField: got: " +
			// e.getMessage());
		}
	}
	
	private void writeFormField(String fieldName, String fieldValue) {
		try {
			dataStream.writeBytes(twoHyphens + boundary + CRLF);
			dataStream.writeBytes("Content-Disposition: form-data; name=\""
					+ fieldName + "\"" + CRLF);
			dataStream.writeBytes(CRLF);
			dataStream.writeBytes(fieldValue);
			dataStream.writeBytes(CRLF);
		} catch (Exception e) {
			System.out.println("GeoPictureUploader.writeFormField: got: "
					+ e.getMessage());
			// Log.e(TAG, "GeoPictureUploader.writeFormField: got: " +
			// e.getMessage());
		}
	}
			
		
	private String getResponse(HttpURLConnection conn) {
		try {
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			byte[] data = new byte[1024];
			int len = dis.read(data, 0, 1024);
			dis.close();
			int responseCode = conn.getResponseCode();
			if (len > 0)
				return new String(data, 0, len);
			else
				return "";
		} catch (Exception e) {
			System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
			// Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
			return "";
		}
	}
			
	public String getStrParam(Store param){
		String result = "";
		String[] keys = param.getKeys();
		for(int i=0;i<keys.length;i++){
			result = result+"&"+keys[i]+"="+param.getString(keys[i]);
		}
		
		return result;
	}
					
	
	
}
