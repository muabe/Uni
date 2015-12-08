package com.markjmind.uni;

/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public class UinMapperException extends RuntimeException {
    public static final String RESULT_CAST="Result에 대한 Class Type 지정이 잘못되었습니다.";

	
	public String message;
	private Throwable exception;
	
    public UinMapperException(String message, Throwable exception) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
    }
    
    public UinMapperException(String message) {
        super(message);
    }
    public Throwable getCause() {
        return this.exception;
    }
}
