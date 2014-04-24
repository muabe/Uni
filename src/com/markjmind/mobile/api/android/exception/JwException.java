package com.markjmind.mobile.api.android.exception;

import java.util.Hashtable;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwException extends Exception{
	
	private final long serialVersionUID = 7798690413547258894L;
	
	public Hashtable<String,JwErrorCode> errorMsg = new Hashtable<String,JwErrorCode>();
	
	public String code;
	public String subCode;
	public String message;
	public String detailMsg;
	public String stackTrace;
	public Throwable exception; 
	
    public JwException(String code, String subCode, String message,String detailMsg,Throwable exception) {
        super(message);
        this.code = code;
        this.subCode = subCode;
        this.message = message;
        this.detailMsg = detailMsg;
        if(exception==null){
        	stackTrace="";
        }else{
        	stackTrace = exception.toString();
        	this.exception = exception;
        	super.initCause(exception);
        }
    }
//    
//    public static void init(){
//    	errorMsg.clear();
//    }
//    
//    public static void addErrorCode(String code, String message){
//    	errorMsg.put(code, new JwErrorCode(code,message));
//    }
//    
	public JwException getException (Throwable exception, String ErrorCode, String subCode, String detailMsg){
		JwErrorCode gec = errorMsg.get(ErrorCode);
		JwException gne = new JwException(ErrorCode, subCode, gec.message, detailMsg, exception) ;
		gec.detailMsg=detailMsg;
		gec.exception=exception;
		gec.stackTrace = gne.stackTrace;
		return gne;
	}
	
	public JwException getException (Throwable exception, String ErrorCode){
		return getException(exception,ErrorCode,null,null);
	}
}
