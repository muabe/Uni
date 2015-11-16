package com.markjmind.uni;

/**
 * Created by codemasta on 2015-11-16.
 */
public abstract class ViewerListener {

    /**
     *
     * @param requestCode
     * @param build
     */
    public void onBind(int requestCode, ViewerBuilder build, Viewer viewer){}

    /**
     * Thread가 시작되기 전에 화면을 정의한다.
     * @param requestCode
     */
    public void onPre(int requestCode, Viewer viewer){}

    /**
     * 네트워크등 Thread를 정의한다.
     * @param requestCode
     * @param event
     * @return
     * @throws Exception
     */
    public abstract boolean onLoad(int requestCode, UpdateEvent event, Viewer viewer) throws Exception;

    /**
     *
     * @param requestCode
     * @param value
     */
    public void onUpdate(int requestCode, Object value, Viewer viewer){}

    /**
     * onLoad 리턴값이 true 일때 호출된다.<br>
     * Viewer를 재정의해야할 함수<br>
     * Viewer에 데이터값이나 셋팅하거나 view표현되어야할 값들 셋팅한다.
     * @param requestCode
     */
    public abstract void onPost(int requestCode, Viewer viewer);

    /**
     * onLoad의 리턴값이 false일때 호출된다.<br>
     * 로딩이 실패했을 경우 표현해야할 화면을 정의한다.
     * @param requestCode
     * @param e
     */
    public void onFail(int requestCode, Exception e, Viewer viewer){}

    /**
     *
     * @param requestCode
     */
    public void onCancelled(int requestCode, Viewer viewer){}
}
