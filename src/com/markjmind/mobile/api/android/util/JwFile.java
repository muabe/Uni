package com.markjmind.mobile.api.android.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class JwFile {
	Context context;
	public JwFile(Context context){
		this.context=context;
	}
	public void saveWebFile(String urlName,String fileName) throws MalformedURLException, ProtocolException, FileNotFoundException,IOException {
			WebConnection wc = new WebConnection();	
			InputStream in = wc.getInputStreamGet(urlName);
	        saveFile(in,fileName);
	}
	
	public void saveFile(InputStream in, String fileName) throws FileNotFoundException,IOException  {
		int readCount = 0;
        byte[] buf = new byte[1024];

        FileOutputStream out = context.openFileOutput(fileName,Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
	    
        while((readCount=in.read(buf))!=-1){
        	out.write(buf,0,readCount);
		   
        }   
        in.close();
        out.close();

	}
	
	public Drawable getDrawable(String fileName) throws FileNotFoundException,IOException{
		if(fileName==null || fileName.equals("")){
			return null;
		}
		FileInputStream in = getFileInputStream(fileName);
		Drawable d = Drawable.createFromStream(in, "none");
		in.close();
		return d;
	}
	
	public FileInputStream getFileInputStream(String fileName) throws FileNotFoundException{
		if(fileName==null || fileName.equals("")){
			return null;
		}
		FileInputStream in = context.openFileInput(fileName);
		return in;
	}
	
	public View drawView(String fileName,View iv){		
		try {
			if(fileName!=null && !fileName.equals("")){
				Drawable d = getDrawable(fileName);
				if(d==null){
					return null;
				}
				iv.setBackgroundDrawable(d);
				return iv;
			}else{
				return null;
			}
			
		} catch (Exception e) {
			Log.d("에러 ",e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	public View setImgDrawView(String fileName,ImageView iv){		
		try {
			Drawable d = getDrawable(fileName);
			if(d==null){
				return null;
			}
			iv.setImageDrawable(d);
			return iv;
			
		} catch (Exception e) {
			Log.d("에러 ",e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
			
	
}
