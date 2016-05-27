package com.markjmind.uni.thread.aop;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-05-27
 */
public class UniAop {
    private CancelAop cancelAop;

    public CancelAop getCancelAop() {
        return cancelAop;
    }

    public void setCancelAop(CancelAop cancelAop) {
        this.cancelAop = cancelAop;
    }
}
