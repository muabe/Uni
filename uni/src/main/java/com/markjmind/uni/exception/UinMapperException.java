package com.markjmind.uni.exception;

/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public class UinMapperException extends RuntimeException {

    Exception exception;
    public UinMapperException(String message){
        super(message);
    }

    public UinMapperException(String message, Exception exception) {
        super(message, exception);
        this.exception = exception;
    }


}
