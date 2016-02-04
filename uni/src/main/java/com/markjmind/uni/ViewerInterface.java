package com.markjmind.uni;

import com.markjmind.uni.viewer.UpdateEvent;
import com.markjmind.uni.viewer.ViewerBuilder;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public interface ViewerInterface {
    public void onBind(int requestCode, ViewerBuilder build);

    public void onPre(int requestCode);

    public void onLoad(int requestCode, UpdateEvent event) throws Exception;

    public void onUpdate(int requestCode, Object value);

    public void onPost(int requestCode);

    public void onFail(int requestCode, boolean isException, String message, Exception e);

    public void onCancelled(int requestCode);
}
