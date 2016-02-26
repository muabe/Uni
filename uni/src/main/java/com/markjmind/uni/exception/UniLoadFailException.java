package com.markjmind.uni.exception;

/**
 * Created by markj on 2015-12-07.
 */
public class UniLoadFailException extends Exception{
    private Object arg;
    public UniLoadFailException(String message, Object arg) {
        super(message);
        this.arg = arg;
    }

    public Object getArg(){
        return arg;
    }
}
