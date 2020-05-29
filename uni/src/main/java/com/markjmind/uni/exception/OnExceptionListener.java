package com.markjmind.uni.exception;

public interface OnExceptionListener<ExceptionType extends Exception> {
    boolean onException(ExceptionType e);
}
