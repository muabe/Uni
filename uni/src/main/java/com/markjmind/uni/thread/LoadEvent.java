package com.markjmind.uni.thread;

import com.markjmind.uni.exception.UniLoadFailException;

/**
 * Created by codemasta on 2015-09-10.
 */
public interface LoadEvent {
    void update(Object value);
    void fail(String message) throws UniLoadFailException;
    void fail(String message, Object arg) throws UniLoadFailException;
}
