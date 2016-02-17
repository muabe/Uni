package com.markjmind.uni;

import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.viewer.UpdateEvent;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public interface UniInterface {
    public void onBind();

    public void onPre();

    public void onLoad(UpdateEvent event, CancelAdapter cancelAdapter) throws Exception;

    public void onUpdate(Object value, CancelAdapter cancelAdapter);

    public void onPost();

    public void onFail(boolean isException, String message, Exception e);

    public void onCancelled(boolean attached);

}
