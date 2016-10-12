package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public interface UniInterface extends UniLoadFail{

    void onBind();

    void onPre();

    void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception;

    void onUpdate(Object value, CancelAdapter cancelAdapter);

    void onPost();

    void onPostFail(String message, Object arg);

}
