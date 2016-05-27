package com.markjmind.uni.thread.aop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-05-27
 */
public interface CancelAop {
    void beforeOnCancel(boolean isAttached);
    void afterOnCancel(boolean isAttached);
}
