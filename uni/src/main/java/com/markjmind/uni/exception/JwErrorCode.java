package com.markjmind.uni.exception;

/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwErrorCode {
	public String code;
	public String subCode;
	public String message;
	public String detailMsg;
	public String stackTrace;
	public Throwable exception; 
	
	public JwErrorCode(String code, String message){
		this.code = code;
		this.message = message;
	}
    
    public String getFullMessage(){
    	String result = message+"\n("+code+":"+subCode+")";
    	result+="\n"+detailMsg+stackTrace;
    	return result;
    }
    
    public String getCode(){
    	return code;
    }
    
    
    public String getMessage(){
    	return message;
    }
}
