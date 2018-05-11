package com.markjmind.uni.thread.aop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-05
 */

public interface AopListener {
    void beforeOnPre();
    void afterOnPre();

    void beforeOnLoad();
    void afterOnLoad();

    void beforeOnPost();
    void afterOnPost();

    void beforeOnCancel();
    void afterOnCancel();

    void beforeOnException(Exception e);
    void afterOnException(Exception e);

}
