package com.markjmind.uni;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-05-10
 */
public interface UniLoadFail {
    void onException(Exception e);
    void onCancelled(boolean attached);
}
