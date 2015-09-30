package com.markjmind.uni.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.content.Context;

/**
 * start : 2013.11.16<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.16
 */
public class JwXmlResoure {
	public static String charSet = "UTF-8";
	
	public static InputStream getStream(Context context, int R_raw_id){
		return  context.getResources().openRawResource(R_raw_id);
	}
	
	public static InputStream getStream(String xml, String charSet){
		try {
			return new ByteArrayInputStream(xml.getBytes(charSet));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static InputStream getStream(String xml){
		return getStream(xml, charSet);
	}
	
	public static String getString(InputStream in, String charSet) throws IOException{
		byte[] buffer = new byte[in.available()];
		in.read(buffer);
		return  new String(buffer,charSet);
	}
	public static String getString(InputStream in) throws IOException{
		return  getString(in,charSet);
	}
	
	public static String getString(Context context, int R_raw_id, String charSet) throws IOException{
		InputStream in = getStream(context, R_raw_id);
		return  getString(in,charSet);
	}
	
	public static String getString(Context context, int R_raw_id) throws IOException{
		return  getString(context,R_raw_id, charSet);
	}
	
	
	
}
