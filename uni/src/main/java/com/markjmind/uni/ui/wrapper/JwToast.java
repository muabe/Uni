package com.markjmind.uni.ui.wrapper;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.widget.Toast;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwToast{
	
	
	public static void print(Context con, String msg,boolean isLong){
		ToastRun t = new ToastRun(con,msg,isLong);
		new Handler().post(t);
    }
	

	static class ToastRun implements Runnable{
		Context con;
		String msg;
		boolean isLong;
		
		public ToastRun(Context con1, String msg1,boolean isLong1){
			con = con1;
			msg = msg1;
			isLong = isLong1;
		}
		
		public void run(){
			int d = Toast.LENGTH_LONG;
	    	if(!isLong){
	    		d = Toast.LENGTH_SHORT;
	    	}
	    	Toast t = Toast.makeText(con, msg, d);
	    		t.setText(msg);
	        t.show();
		}
	}
   
    public static void longPrint(Context con,String msg,boolean closeAndShow){
    	print(con,msg,true);
    }
    public static void shotPrint(Context con,String msg,boolean closeAndShow){
    	print(con,msg,false);
    }
    
    public static void longPrint(Context con,String msg){
    	longPrint(con,msg,true);
    }
    public static void shotPrint(Context con,String msg){
    	shotPrint(con,msg,true);
    }
    
    
    public static void cancel(){

    }

    public static void popup(Context context, String title, String msg, String btnMsg){
		Builder b =new Builder(context);
		if(title!=null){
			b.setTitle(title);
		}
		b.setMessage(msg);
		if(btnMsg==null){
			btnMsg = "Close";
		}
		b.setPositiveButton(btnMsg, new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		b.show();
	}
    
    public static void popup(Context context, String msg,Handler handler){
    	handler.post(new HandlerPopup(context,msg));
		
	}
 
    
    
	public static void popup(Context context, String msg){
		popup(context, null, msg, null);
	}
	
	public static void popup(Context context, String msg, String btnMsg){
		popup(context, null, msg, btnMsg);
	}
	
	
    
	public static void errorPopup(Context context,Exception e){
		String errorMsg = "";
		for(int i=0;i<e.getStackTrace().length;i++){
			errorMsg=errorMsg+e.getStackTrace()[i].getClassName()+":"+e.getStackTrace()[i].getMethodName()
					+" at "+e.getStackTrace()[i].getFileName()+" "+e.getStackTrace()[i].getLineNumber()+"line"+"\n";
		}
		popup(context, errorMsg);
	}
}
class HandlerPopup implements Runnable{
	Context context;
	String msg;
	public HandlerPopup(Context context,String msg){
		this.context = context;
		this.msg = msg;
	}
	public void run(){
		Builder b =new Builder(context);
		b.setMessage(msg);
		String	btnMsg = "Close";
		b.setPositiveButton(btnMsg, new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		b.show();
	}
}
