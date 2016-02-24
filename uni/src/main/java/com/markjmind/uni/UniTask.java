package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.UpdateEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public interface UniTask {
    void onBind();

    void onPre();

    void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception;

    void onUpdate(Object value, CancelAdapter cancelAdapter);

    void onPost();

    void onFail(boolean isException, String message, Exception e);

    void onCancelled(boolean attached);

}
